package ssn.codebreakers.pecsinstructor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.FileUploader;
import ssn.codebreakers.pecsinstructor.helpers.ImagePicker;
import ssn.codebreakers.pecsinstructor.helpers.ProgressCallback;

public class AddCardsActivity extends AppCompatActivity {
    ImagePicker imagePicker;
    Button uploadButton;
    EditText CardName;
    ImageView AddCard;
    FileUploader fileUploader;
    TextView acknowledge ;
    TextView linkView;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cards);
        linkView = findViewById(R.id.link);
        AddCard = findViewById(R.id.AddCard);
        final String categoryid =  getIntent().getExtras().getString("category");
        AddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // creates new category as specified by the user
                try {
                    final Card cardobj = new Card();
                    CardName = findViewById(R.id.CardId);
                    cardobj.setId(CardName.getText().toString());
                    cardobj.setCategoryId(categoryid);
                    cardobj.setText(CardName.getText().toString());
                    acknowledge = findViewById(R.id.acknowledge);
                    if (filePath != null) {
                        File file = new File(filePath);
                        final String uploadedFileID = UUID.randomUUID().toString();//random unique file name
                        final ProgressDialog progressDialog = new ProgressDialog(AddCardsActivity.this);
                        progressDialog.setMessage("Uploading Image");
                        progressDialog.setProgress(0);
                        progressDialog.show();
                        fileUploader.uploadFile(file, uploadedFileID, new ProgressCallback() {
                            @Override
                            public void onSuccess(Object result) {//on succesfully uploading, show the uploaded file url
                                cardobj.setImageId(uploadedFileID);
                                cardobj.setLocalImagePath(filePath);
                                CardHelper.addCard(AddCardsActivity.this, cardobj);
                                finish();

                            }

                            @Override
                            public void onProgress(int progress) {//for large files, or slow network, use onprogress to show progress
                                progressDialog.setProgress(progress);
                            }

                            @Override
                            public void onError(Object error) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Card Already Exists !!" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        fileUploader = new FileUploader(getApplicationContext());
        uploadButton = findViewById(R.id.uploadBtn);
        imagePicker = new ImagePicker(this);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.chooseImage(new Callback() {
                    @Override
                    public void onSuccess(Object result) {//on successfully choosing image, try to upload the selected image
                        filePath = (String)result;
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
