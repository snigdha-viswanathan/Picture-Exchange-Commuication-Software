package ssn.codebreakers.pecsinstructor;

/**
 * Created by kandhavel on 20-Mar-18.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.CommonUtils;
import ssn.codebreakers.pecsinstructor.helpers.ImagePicker;


public class category_pic extends AppCompatActivity {
    ImagePicker imagePicker;

    Button addcategory;
    ImageView selectedImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker_example);

        addcategory = findViewById(R.id.selectImageButton);
        selectedImageView = findViewById(R.id.selectedImage);

        imagePicker = new ImagePicker(this);

        addcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.chooseImage(new Callback() {
                    @Override
                    public void onSuccess(Object result) {//returns the file path of the selected image
                        String filePath = (String)result;
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        selectedImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Object object) {
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
