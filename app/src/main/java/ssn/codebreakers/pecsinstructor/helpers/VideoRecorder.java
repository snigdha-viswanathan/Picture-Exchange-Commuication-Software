package ssn.codebreakers.pecsinstructor.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static android.content.Context.CAMERA_SERVICE;

public class VideoRecorder
{
    protected static final String TAG = "VideoProcessing";

    private static final int PERMISSION_REQUEST = 301;

    protected CameraDevice cameraDevice = null;
    protected CameraCaptureSession session = null;
    protected ImageReader imageReader = null;

    Callback previewStartedhandler = null;
    Callback recordingStartedHandler = null;
    Callback permissionCallback = null;

    TextureView textureView = null;
    Context context = null;

    boolean startedRecording = false;
    private Size previewSize;
    private Size videoSize;

    Range fpsRange = null;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private Integer sensorOrientation;
    MediaRecorder mediaRecorder = new MediaRecorder();
    File outputFile = null;
    RecordSettings recordSettings;


    public VideoRecorder(Context context, File outputFile, RecordSettings recordSettings)
    {
        this.context = context;
        this.outputFile = outputFile;
        this.recordSettings = recordSettings;
    }

    public void checkPermission(Activity activity, Callback permissionCallback)
    {
        this.permissionCallback = permissionCallback;
        PackageManager pm = activity.getPackageManager();
        int hasCameraPerm = pm.checkPermission(Manifest.permission.CAMERA, activity.getPackageName());
        int hasAudioPerm = pm.checkPermission(Manifest.permission.RECORD_AUDIO, activity.getPackageName());
        if (hasCameraPerm == PackageManager.PERMISSION_GRANTED && hasAudioPerm == PackageManager.PERMISSION_GRANTED)
            permissionCallback.onSuccess("already granted");
        else
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST);
    }

    public void startPreview(TextureView textureView, Callback previewStartedHandler)
    {
        this.previewStartedhandler = previewStartedHandler;
        this.textureView = textureView;
        readyCamera(context);
    }

    public void stop()
    {
        System.out.println("stop preview called");
        try {
            mCameraOpenCloseLock.acquire();
            System.out.println("aquired lock");
            try {
                if(mediaRecorder != null)
                {
                    if(startedRecording)
                        mediaRecorder.stop();
                    mediaRecorder.reset();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
            }catch (Exception e){e.printStackTrace();}
            try {
                if (session != null) {
                    session.close();
                    session = null;
                    System.out.println("closed session");
                }
            }catch (Exception e){e.printStackTrace();}

            try {
                if (cameraDevice != null) {
                    System.out.println("clearing camera");
                    cameraDevice.close();
                    cameraDevice = null;
                    System.out.println("cleared camera");
                }
            }catch (Exception e) {e.printStackTrace();}

            try {
                if (imageReader != null) {
                    System.out.println("clearing reaer");
                    imageReader.close();
                    imageReader = null;
                    System.out.println("cleared reaer");
                }
            }catch (Exception e) {e.printStackTrace();}

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
        System.out.println("stopped preview!!!!!!");
    }

    public void startRecording(Callback recordingStartedHandler)
    {
        if(startedRecording)
            return;
        this.recordingStartedHandler = recordingStartedHandler;
        try{
            mediaRecorder.start();
            recordingStartedHandler.onSuccess(null);
            startedRecording = true;
        }catch (Exception e)
        {
            e.printStackTrace();
            recordingStartedHandler.onError("unknown error");
        }
    }

    protected CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.i(TAG, "CameraDevice.StateCallback onOpened");
            mCameraOpenCloseLock.release();
            cameraDevice = camera;

            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            if(recordSettings.isRecordAudio())
            {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT );
            }
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            if(recordSettings.isRecordAudio())
            {
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            }
            mediaRecorder.setVideoEncodingBitRate(recordSettings.getBitRate());
            mediaRecorder.setVideoFrameRate(recordSettings.getFrameRate());

            mediaRecorder.setVideoSize(previewSize.getWidth(), previewSize.getHeight());

            mediaRecorder.setMaxFileSize(0);
            mediaRecorder.setOrientationHint(sensorOrientation);
            mediaRecorder.setOutputFile(outputFile.getAbsolutePath());
            //mediaRecorder.setVideoFrameRate(24);

            try {

                mediaRecorder.prepare();

                List outputSurfaces = new ArrayList<>();
                outputSurfaces.add(imageReader.getSurface());
                outputSurfaces.add(mediaRecorder.getSurface());
                if(textureView != null)
                {
                    outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
                }
                System.out.println("total surfaces " +outputSurfaces.size());
                System.out.println("testing size here " +previewSize.getWidth()+":"+previewSize.getHeight());
                if(textureView != null)
                {
                    SurfaceTexture texture = textureView.getSurfaceTexture();
                    texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
                }
                cameraDevice.createCaptureSession(outputSurfaces, sessionStateCallback, null);
            } catch (CameraAccessException e){
                Log.e(TAG, e.getMessage());
                previewStartedhandler.onError("camera access error");
            } catch (Exception e)
            {
                e.printStackTrace();
                previewStartedhandler.onError("media recorder error");
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.w(TAG, "CameraDevice.StateCallback onDisconnected");
            mCameraOpenCloseLock.release();
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.e(TAG, "CameraDevice.StateCallback onError " + error);
            mCameraOpenCloseLock.release();
            camera.close();
            cameraDevice = null;
            previewStartedhandler.onError(error);
        }
    };

    protected CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.i(TAG, "CameraCaptureSession.StateCallback onConfigured");
            VideoRecorder.this.session = session;
            try {
//                HandlerThread thread=new HandlerThread("changed Preview");
//                thread.start();
//                Handler handler=new Handler(thread.getLooper());
                session.setRepeatingRequest(createCaptureRequest(), null, null);
                previewStartedhandler.onSuccess(null);
            } catch (CameraAccessException e){
                Log.e(TAG, e.getMessage());
                previewStartedhandler.onError("camera access error");
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            previewStartedhandler.onError("camera configure failed");
        }
    };

    protected ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //if(doRecording)
            {

                Image img = reader.acquireLatestImage();
                if (img != null)
                {
                    processImage(img);
                    img.close();
                }
            }
        }
    };

    public void readyCamera(Context context)
    {
        CameraManager manager = (CameraManager) context.getSystemService(CAMERA_SERVICE);

        try
        {
            String pickedCamera = CameraUtils.getFrontCameraId(manager);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(pickedCamera);
            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            fpsRange = CameraUtils.chooseFPS(characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            previewSize = CameraUtils.chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), new Size(recordSettings.getScreenWidth(), recordSettings.getScreenHeight()), recordSettings.getVideoResolution());
            videoSize = CameraUtils.chooseVideoSize(map.getOutputSizes(MediaRecorder.class), recordSettings.getVideoResolution(), (double) recordSettings.getScreenWidth()/recordSettings.getScreenHeight());
            System.out.println("video size "+videoSize.getWidth()+":"+videoSize.getHeight());

            System.out.println("preview size "+previewSize.getWidth()+":"+previewSize.getHeight());
            //textureView.setLayoutParams(new LinearLayout.LayoutParams(previewSize.getWidth(), previewSize.getHeight()));

            if ( ContextCompat.checkSelfPermission( context, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED )
            {
                //TODO : handle
            }else
            {
                if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS))
                {
                    previewStartedhandler.onError("camera lock acquire failed");
                    return;
                }
                manager.openCamera(pickedCamera, cameraStateCallback, null);
                imageReader = ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(), ImageFormat.YUV_420_888, 2 /* images buffered */);
                imageReader.setOnImageAvailableListener(onImageAvailableListener, null);
            }
            Log.i(TAG, "imageReader created");
        }catch (InterruptedException e){
            previewStartedhandler.onError("camera lock acquire failed");
        }catch (CameraAccessException e){
            previewStartedhandler.onError("camera access error");
        }
    }

    private void processImage(Image image)
    {
    }

    protected CaptureRequest createCaptureRequest()
    {
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            builder.addTarget(imageReader.getSurface());
            builder.addTarget(mediaRecorder.getSurface());
            if(textureView != null)
                builder.addTarget(new Surface(textureView.getSurfaceTexture()));
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            if(fpsRange != null)
                builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, fpsRange);
            //builder.set(CaptureRequest.JPEG_ORIENTATION, sensorOrientation);
//            builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_OFF);
//            builder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, 500l);
//            builder.set(CaptureRequest.SENSOR_SENSITIVITY, sensitivity);
//            builder.set(CaptureRequest.SENSOR_FRAME_DURATION, frameDuration);

            return builder.build();
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage());
            previewStartedhandler.onError("camera access error");
            return null;
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            permissionCallback.onSuccess("granted");
        else
            permissionCallback.onError("denied");
    }
}
