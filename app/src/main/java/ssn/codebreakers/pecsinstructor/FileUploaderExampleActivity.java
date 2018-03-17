package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.UUID;

import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.FileUploader;
import ssn.codebreakers.pecsinstructor.helpers.ImagePicker;
import ssn.codebreakers.pecsinstructor.helpers.ProgressCallback;

/**
 * Example Activity to show the use of FileUploader API
 */
public class FileUploaderExampleActivity extends AppCompatActivity {

    Button uploadImageButton;
    TextView statusView;
    TextView linkView;

    ImagePicker imagePicker;
    FileUploader fileUploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_uploader_example);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        statusView = findViewById(R.id.statusView);
        linkView = findViewById(R.id.link);

        imagePicker = new ImagePicker(this);
        fileUploader = new FileUploader(getApplicationContext());

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.chooseImage(new Callback() {
                    @Override
                    public void onSuccess(Object result) {//on successfully choosing image, try to upload the selected image
                        String filePath = (String)result;
                        File file = new File(filePath);
                        String uploadedFileID = UUID.randomUUID().toString();//random unique file name
                        fileUploader.uploadFile(file, uploadedFileID, new ProgressCallback() {
                            @Override
                            public void onSuccess(Object result) {//on succesfully uploading, show the uploaded file url
                                linkView.setText((String)result);
                            }

                            @Override
                            public void onProgress(int progress) {//for large files, or slow network, use onprogress to show progress
                                statusView.setText("uploading in progress. "+ progress+"% complete.");
                            }

                            @Override
                            public void onError(Object error) {
                                statusView.setText("uploading error"+error);
                            }
                        });
                    }

                    @Override
                    public void onError(Object error) {
                    }
                });
            }
        });
    }

    //must be added in the activity in which imagepicker is used
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        imagePicker.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    //must be added in the activity in which imagepicker is used
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
