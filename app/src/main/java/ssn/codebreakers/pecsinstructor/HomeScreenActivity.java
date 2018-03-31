package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.CommonUtils;

public class HomeScreenActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean registeredAlready = sharedPreferences.getBoolean("registered_already", false);
        if(!registeredAlready)
        {
            startActivity(new Intent(HomeScreenActivity.this,RegistrationActivity.class));
            finish();
        }

        final Button imagePickerButton = findViewById(R.id.imagePickerButton);
        final Button fileUploaderButton = findViewById(R.id.fileUploaderButton);
        final Button databaseButton = findViewById(R.id.databaseButton);
        final Button recordVideoButton = findViewById(R.id.recordVideo);

        //open imagepicker sample activity on button click
        imagePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GalleryActivity.class));
            }
        });

        fileUploaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FileUploaderExampleActivity.class));
            }
        });

        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DatabaseSampleActivity.class));
            }
        });

        recordVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TitleActivity.class));
            }
        });

        //add these two lines in the starting activity
        CommonUtils.checkAndAskFilePermission(this);
        APIHelper.updateFCMToken(getApplicationContext(), new Callback() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(Object error) {

            }
        });

        /*Category category=new Category();
        category.setId(CommonUtils.getUniqueRandomID());
        category.setName("animals");
        CategoryHelper.addCategory(getApplicationContext(),category);

        Card card=new Card();
        card.setId(CommonUtils.getUniqueRandomID());
        card.setCategoryId(category.getId());
        card.setText("dog");
        CardHelper.addCard(getApplicationContext(),card);

        Card card1=new Card();
        card1.setId(CommonUtils.getUniqueRandomID());
        card1.setCategoryId(category.getId());
        card1.setText("cat");
        CardHelper.addCard(getApplicationContext(),card1);

        Card card2=new Card();
        card2.setId(CommonUtils.getUniqueRandomID());
        card2.setCategoryId(category.getId());
        card2.setText("elephant");
        CardHelper.addCard(getApplicationContext(),card2);


        Category category1=new Category();
        category1.setId(CommonUtils.getUniqueRandomID());
        category1.setName("colors");
        CategoryHelper.addCategory(getApplicationContext(),category1);

        Card card3=new Card();
        card3.setId(CommonUtils.getUniqueRandomID());
        card3.setCategoryId(category1.getId());
        card3.setText("red");
        CardHelper.addCard(getApplicationContext(),card3);

        Card card4=new Card();
        card4.setId(CommonUtils.getUniqueRandomID());
        card4.setCategoryId(category1.getId());
        card4.setText("blue");
        CardHelper.addCard(getApplicationContext(),card4);

        Card card5=new Card();
        card5.setId(CommonUtils.getUniqueRandomID());
        card5.setCategoryId(category1.getId());
        card5.setText("green");
        CardHelper.addCard(getApplicationContext(),card5);


        Category category2=new Category();
        category2.setId(CommonUtils.getUniqueRandomID());
        category2.setName("family");
        CategoryHelper.addCategory(getApplicationContext(),category2);

        Card card6=new Card();
        card6.setId(CommonUtils.getUniqueRandomID());
        card6.setCategoryId(category2.getId());
        card6.setText("mother");
        CardHelper.addCard(getApplicationContext(),card6);

        Card card7=new Card();
        card7.setId(CommonUtils.getUniqueRandomID());
        card7.setCategoryId(category2.getId());
        card7.setText("father");
        CardHelper.addCard(getApplicationContext(),card7);

        Card card8=new Card();
        card8.setId(CommonUtils.getUniqueRandomID());
        card8.setCategoryId(category2.getId());
        card8.setText("sister");
        CardHelper.addCard(getApplicationContext(),card8);*/
        List<Category> categories =CategoryHelper.getAllCategories(getApplicationContext());
        for(int i=0;i<categories.size();i++)
        {
            System.out.println("category="+categories.get(i).getName());
            List<Card> cards = CardHelper.getCardsOfCategory(getApplicationContext(),categories.get(i).getId());
            for(Card card: cards)
            {
                System.out.println("card = "+card);
            }
        }

    }
    public void goToAnActivity(View view) {
        Intent Intent = new Intent(this,NewHomeScreen.class);
        startActivity(Intent);
    }

}
