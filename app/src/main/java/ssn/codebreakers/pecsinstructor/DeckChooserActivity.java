package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.SpeechHelper;

import static ssn.codebreakers.pecsinstructor.helpers.SpeechHelper.SPEECH_REQUEST_ID;

public class DeckChooserActivity extends AppCompatActivity {
    GridView grid1;
    GridView grid2;
    GridView grid3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_chooser);

        grid1=(GridView)findViewById(R.id.grid1);
        grid2=(GridView)findViewById(R.id.grid2);
        grid3=(GridView)findViewById(R.id.grid3);
        String userId=getIntent().getStringExtra("user_id");
        System.out.println("user id="+userId);
        SpeechHelper speechHelper = new SpeechHelper(getApplicationContext(), this);
        speechHelper.speechToText();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SPEECH_REQUEST_ID)
        {
            if (resultCode == RESULT_OK && data != null)
            {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String spokenText = result.get(0);//spoken text
                System.out.println("test output = "+ spokenText);
                final List<List<Card>> listOfCards = new ArrayList<>();
                final List<Category> categoryList = new ArrayList<>();
                APIHelper.getCardsForSentence(getApplicationContext(), listOfCards, categoryList, spokenText, new Callback() {
                    @Override
                    public void onSuccess(Object result) {
                        for (List<Card> cards : listOfCards ) {
                            for(Card card:cards) {
                                CardHelper.addCard(DeckChooserActivity.this,card);
                                System.out.println("cards for sentence "+card.getText());
                            }
                        }
                        for(Category category: categoryList)
                        {
                            CategoryHelper.addCategory(getApplicationContext(), category);
                            System.out.println("category for sentence "+category.getName());
                        }

                        if(listOfCards.size()>0)
                        {
                            CardGridAdapter cardGridAdapter1=new CardGridAdapter(getApplicationContext(),listOfCards.get(0));
                            grid1.setAdapter(cardGridAdapter1);
                        }
                        if(listOfCards.size()>1)
                        {
                            CardGridAdapter cardGridAdapter2=new CardGridAdapter(getApplicationContext(),listOfCards.get(1));
                            grid1.setAdapter(cardGridAdapter2);
                        }
                        if(listOfCards.size()>2)
                        {
                            CardGridAdapter cardGridAdapter3=new CardGridAdapter(getApplicationContext(),listOfCards.get(2));
                            grid1.setAdapter(cardGridAdapter3);
                        }

                    }

                    @Override
                    public void onError(Object error) {

                    }
                });
            }
        }
    }
}
