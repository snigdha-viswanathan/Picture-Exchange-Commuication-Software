package ssn.codebreakers.pecsinstructor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;


public class TitleActivity extends AppCompatActivity {
    Button okButton;
    EditText inputTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Perspective");
        actionBar.setDisplayShowHomeEnabled(true);

        inputTitle = findViewById(R.id.input_title);
        okButton=findViewById(R.id.oks);
        inputTitle.getText().toString();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<List<Card>> listOfCards = new ArrayList<>();

                final List<Category> categories = new ArrayList<>();

                final ProgressDialog progressDialog = new ProgressDialog(TitleActivity.this);
                progressDialog.setProgress(0);
                progressDialog.show();
                APIHelper.getCardsForWord(TitleActivity.this,listOfCards,categories,inputTitle.getText().toString(),new Callback(){
                    @Override
                    public void onSuccess(Object result) {
                        for (List<Card> cards : listOfCards ) {
                            for(Card card:cards) {
                                CardHelper.addCard(TitleActivity.this,card);
                            }
                        }
                        for(Category category: categories)
                        {
                            CategoryHelper.addCategory(getApplicationContext(), category);
                        }
                        Intent intent = new Intent(TitleActivity.this, RecordVideoActivity.class);
                        intent.putExtra("listOfCards",new Gson().toJson(listOfCards));
                        intent.putExtra("categories",new Gson().toJson(categories));
                        intent.putExtra("word",inputTitle.getText().toString());
                        intent.putExtra("user_id",getIntent().getStringExtra("user_id"));
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public  void  onError(Object result) {

                    }
                } );


            }
        });

    }
}
