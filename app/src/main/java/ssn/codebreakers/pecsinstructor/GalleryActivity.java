package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Category;

public class GalleryActivity extends AppCompatActivity {
    GridView gridView ;
    Button addCategory;
    ArrayAdapter<String> adapter;
    List<Category> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        categories = CategoryHelper.getAllCategories(getApplicationContext());

        addCategory = findViewById(R.id.addc);
        addCategory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(GalleryActivity.this, AddCategoryActivity.class));
        }
    }
        );

    // Get gridview object from xml file

    gridView = (GridView) findViewById(R.id.gridView1);


    // Set custom adapter (GridAdapter) to gridview

        gridView.setAdapter(  new CategoryGridAdapter( this, categories ) );
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
           Intent intent =  new Intent(GalleryActivity.this, CategoryCardsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("category", categories.get(position).getId());
            System.out.println("CatId"+categories.get(position).getId());
            intent.putExtras(bundle) ;
            //Toast.makeText(getApplicationContext(), position   +""+categories.get(position).getId(), Toast.LENGTH_SHORT ).show();
            startActivity(intent);

        }
    });

}
}
