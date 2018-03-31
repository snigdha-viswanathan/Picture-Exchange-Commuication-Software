package ssn.codebreakers.pecsinstructor;

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

import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.CommonUtils;
import ssn.codebreakers.pecsinstructor.helpers.FileUploader;
import ssn.codebreakers.pecsinstructor.helpers.ImagePicker;
import ssn.codebreakers.pecsinstructor.helpers.ProgressCallback;

public class AddCategoryActivity extends AppCompatActivity {
    ImagePicker imagePicker;
    Button uploadButton;
    EditText CategoryName;
    ImageView AddCategory;
    FileUploader fileUploader;
    TextView acknowledge ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        AddCategory = findViewById(R.id.AddCategory);
        AddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // creates new category as specified by the user
                Category categoryobj = new Category();
                CategoryName = findViewById(R.id.CategoryId);
                System.out.println("CateName"+CategoryName.getText().toString());
                categoryobj.setId(CommonUtils.getUniqueRandomID());
                categoryobj.setName(CategoryName.getText().toString());
                acknowledge = findViewById(R.id.acknowledge);
                try {
                    CategoryHelper.addCategory(AddCategoryActivity.this, categoryobj);

                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddCategoryActivity.this, GalleryActivity.class));
                }
                catch (Exception e){
                    Toast.makeText(AddCategoryActivity.this, "Category Already Exists !!" , Toast.LENGTH_SHORT).show();
                                    }
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
