package ssn.codebreakers.pecsinstructor.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImagePicker
{
    private static final int PICK_IMAGE_CAMERA = 200;
    private static final int PICK_IMAGE_GALLERY = 201;
    private static final int PERMISSION_REQUEST = 300;

    private static final int MAX_WIDTH = 500;
    private static final int MAX_HEIGHT = 500;

    private Activity activity;
    private Callback callback;
    private File destinationFile;
    public ImagePicker(Activity activity)
    {
        this.activity = activity;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public void chooseImage(Callback callback)
    {
        this.callback = callback;
        try {
            PackageManager pm = activity.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, activity.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                builder.setTitle("Image Source");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item)
                    {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                        File destinationFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "pecsins");
                        if(!destinationFolder.exists())
                            destinationFolder.mkdirs();
                        destinationFile = new File(destinationFolder, "img_"+timeStamp+".jpg");

                        if (options[item].equals("Take Photo"))
                        {
                            Uri imageUri = Uri.fromFile(destinationFile);
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                            activity.startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery"))
                        {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activity.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        }
                    }
                });
                builder.show();
            } else
            {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST);
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE_CAMERA)
        {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(destinationFile.getAbsolutePath());
                Bitmap resized = resizeBitmap(bitmap);

                try(FileOutputStream fos = new FileOutputStream(destinationFile))
                {
                    resized.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                }
                bitmap.recycle();
                resized.recycle();
                callback.onSuccess(destinationFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY)
        {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);
                Bitmap resizedBitmap = resizeBitmap(bitmap);
                try(FileOutputStream fos = new FileOutputStream(destinationFile))
                {
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                }
                bitmap.recycle();
                resizedBitmap.recycle();
                callback.onSuccess(destinationFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(e.getMessage());
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap)
    {
        double width = bitmap.getWidth();
        double height = bitmap.getHeight();
        if(width > MAX_WIDTH)
        {
            height = MAX_WIDTH * height/width;
            width = MAX_WIDTH;
        }
        if(height > MAX_HEIGHT)
        {
            width= MAX_HEIGHT* width/height;
            height = MAX_HEIGHT;
        }
        return Bitmap.createScaledBitmap(bitmap, (int)width, (int)height, true);
    }

   public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            chooseImage(callback);
        }else
        {
            Toast.makeText(activity, "Camera Permission error", Toast.LENGTH_SHORT).show();
            callback.onError("camera permission denied");
        }
    }
}
