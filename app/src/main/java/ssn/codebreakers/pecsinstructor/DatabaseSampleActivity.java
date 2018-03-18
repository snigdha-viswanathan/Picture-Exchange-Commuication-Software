package ssn.codebreakers.pecsinstructor;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.helpers.CommonUtils;

/**
 * Example Activity to show how to access the database using helper methods
 */
public class DatabaseSampleActivity extends AppCompatActivity {

    Button addCategoryButton;
    ListView categoriesListView;

    List<String> categoryNames;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_sample);

        categoriesListView = findViewById(R.id.categoriesListView);
        addCategoryButton = findViewById(R.id.addCategoryButton);

        List<Category> categories = CategoryHelper.getAllCategories(getApplicationContext());// get all the available categories from the database
        categoryNames = new ArrayList<>();
        for(Category category: categories)
            categoryNames.add(category.getName());
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, categoryNames);
        categoriesListView.setAdapter(adapter);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForNewCategory();
            }
        });
    }

    private void askForNewCategory()
    {
        final EditText editText = new EditText(this);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50 ,50, 50, 50);
        editText.setLayoutParams(params);
        editText.setHint("name");
        container.addView(editText);
        new AlertDialog.Builder(this)
                .setTitle("Add Category")
                .setView(container)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = editText.getText().toString();
                        addCategory(name);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addCategory(String categoryName)
    {
        Category category = new Category();
        category.setId(CommonUtils.getUniqueRandomID());
        category.setName(categoryName);
        CategoryHelper.addCategory(getApplicationContext(), category);// add new category to the database

        //update newly added category in the listview
        categoryNames.add(categoryName);
        adapter.notifyDataSetChanged();
    }
}
