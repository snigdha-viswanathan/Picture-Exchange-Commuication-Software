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

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;

public class CategoryCardsActivity extends AppCompatActivity {
    GridView gridView ;
    Button addCard;
    ArrayAdapter<String> adapter;
    List<Card> cards;
    String categoryid;

    @Override
    protected void onResume() {
        super.onResume();
        cards = CardHelper.getCardsOfCategory(CategoryCardsActivity.this,categoryid);
        gridView.setAdapter(  new CardGridAdapter( this, cards ) );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_cards);

        categoryid =  getIntent().getExtras().getString("category");
       System.out.println("cardid="+categoryid);
        cards = CardHelper.getCardsOfCategory(CategoryCardsActivity.this,categoryid);
        //toast for printing no of cards added so far
        System.out.println("cards"+cards.size());
        addCard = findViewById(R.id.addc);
        addCard.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(CategoryCardsActivity.this, AddCardsActivity.class);
                                               Bundle bundle = new Bundle();
                                               bundle.putString("category", categoryid);
                                               intent.putExtras(bundle) ;
                                               //
                                               startActivity(intent);
                                           }
                                       }
        );

        // Get gridview object from xml file

        gridView = (GridView) findViewById(R.id.gridView1);


        // Set custom adapter (GridAdapter) to gridview

        gridView.setAdapter(  new CardGridAdapter( this, cards ) );
      /***  gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                Intent intent =  new Intent(getApplicationContext(), CategoryCardsActivity.class);
                intent.putExtra("category ",cards.get(position).getText()) ;
                startActivity(intent);

            }
        });*/

    }
}
