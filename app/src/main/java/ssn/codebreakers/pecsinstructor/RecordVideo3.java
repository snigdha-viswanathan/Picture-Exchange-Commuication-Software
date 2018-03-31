package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.CommonUtils;
import ssn.codebreakers.pecsinstructor.helpers.RecordSettings;
import ssn.codebreakers.pecsinstructor.helpers.VideoRecorder;

public class RecordVideo3 extends AppCompatActivity {

    TextureView cameraPreviewView;
    ImageView startRecordingButton;
    ImageView stopRecordingButton;
    VideoRecorder videoRecorder;

    boolean previewStarted = false;
    File destinationFile3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video3);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Incorrect video");
        actionBar.setDisplayShowHomeEnabled(true);

        cameraPreviewView = findViewById(R.id.cameraPreview);
        startRecordingButton = findViewById(R.id.startRecordButton);
        stopRecordingButton = findViewById(R.id.stopRecordButton);

        cameraPreviewView.setSurfaceTextureListener(surfaceTextureListener);

        //start recording the video. video will be saved to the destination file
        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecordingButton.setVisibility(View.VISIBLE);
                startRecordingButton.setVisibility(View.GONE);
                if(previewStarted)
                {
                    videoRecorder.startRecording(new Callback() {
                        @Override
                        public void onSuccess(Object result) {
                            System.out.println("started recording");
                        }

                        @Override
                        public void onError(Object error) {
                            System.out.println("couldnt start recording " +error);
                        }
                    });
                }
            }
        });

        //stop recording
        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoRecorder.stop();
                Intent intent = new Intent(RecordVideo3.this, PreviewVideo3.class);
                intent.putExtra("listOfCards",getIntent().getStringExtra("listOfCards"));
                intent.putExtra("categories",getIntent().getStringExtra("categories"));
                intent.putExtra("video_url1", getIntent().getStringExtra("video_url1"));
                intent.putExtra("video_url2", getIntent().getStringExtra("video_url2"));
                intent.putExtra("video_url3", destinationFile3.getAbsolutePath());
                intent.putExtra("word",getIntent().getStringExtra("word"));
                startActivity(intent);
                finish();
               // Toast.makeText(getApplicationContext(), "video recorded "+destinationFile3.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //start camera preview in the textureview
    private void startPreview()
    {
        File appFolder = CommonUtils.getAppFolder();
        destinationFile3 = new File(appFolder, "video"+CommonUtils.getUniqueRandomID()+".mp4");
        videoRecorder = new VideoRecorder(getApplicationContext(), destinationFile3, RecordSettings.GetDefault(this));
        videoRecorder.checkPermission(this, new Callback() {
            @Override
            public void onSuccess(Object result) {
                videoRecorder.startPreview(cameraPreviewView, new Callback() {
                    @Override
                    public void onSuccess(Object result) {
                        System.out.println("started preview");
                        previewStarted = true;
                    }

                    @Override
                    public void onError(Object error) {
                        System.out.println("starting preview error"+error);
                    }
                });
            }

            @Override
            public void onError(Object error) {
                System.out.println("cant start preview permission denied" + error);
            }
        });
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener=new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            startPreview();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
    };

    //must be added in the activity in which VideoRecorder is used
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        videoRecorder.onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}

