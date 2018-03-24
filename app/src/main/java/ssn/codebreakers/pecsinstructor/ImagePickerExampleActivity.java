package ssn.codebreakers.pecsinstructor;
        import android.content.DialogInterface;
        import android.support.v7.app.AlertDialog;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.GridView;
        import java.util.ArrayList;
        import java.util.List;

        import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
        import ssn.codebreakers.pecsinstructor.db.models.Category;
        import ssn.codebreakers.pecsinstructor.helpers.Callback;
        import ssn.codebreakers.pecsinstructor.helpers.CommonUtils;
        import ssn.codebreakers.pecsinstructor.helpers.ImagePicker;

/*
    Example Activity to show the use of ImagePicker API
 */
public class ImagePickerExampleActivity extends AppCompatActivity {

    GridView gridView ;
    ArrayAdapter<String> adapter;
    static final String[ ] GRID_DATA = new String[] {
           "fruits" ,
            "veges",
            "animals" ,
            "daily routines",
            "food" ,
            "aches & pains",
            "relatives",
            "things in pairs",
            "smell" ,
            "simple words"
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.gridviewfile );

        final Button addcategory = findViewById(R.id.addc);
        addcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), category_pic.class));
                }


        }
        );

        // Get gridview object from xml file

        gridView = (GridView) findViewById(R.id.gridView1);


        // Set custom adapter (GridAdapter) to gridview

        gridView.setAdapter(  new CustomGridAdapter( this, GRID_DATA ) );
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        ((TextView) v.findViewById( R.id.textView ))
                                .getText(), Toast.LENGTH_SHORT).show();

            }
        });

    }


}
