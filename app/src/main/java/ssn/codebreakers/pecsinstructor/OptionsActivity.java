package ssn.codebreakers.pecsinstructor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.db.models.VideoMessage;
import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Gson gson = new Gson();
        final List<List<Card>> tmpCards = gson.fromJson(getIntent().getStringExtra("listOfCards"), new TypeToken<List<List<Card>>>() {
        }.getType());
        List<Category> tmpCategories = gson.fromJson(getIntent().getStringExtra("categories"), new TypeToken<List<Category>>() {
        }.getType());
        final String word = getIntent().getStringExtra("word");

        System.out.print("CardText" + tmpCards.get(0).get(0).getText());


        GridView gridView1 = findViewById(R.id.gridView1);
        GridView gridView2 = findViewById(R.id.gridView2);
        GridView gridView3 = findViewById(R.id.gridView3);



        if (tmpCards.size() >= 0 && tmpCards.get(0) != null) {
            gridView1.setAdapter(new CardGridAdapter(this, tmpCards.get(0)));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    VideoMessage videoMessage = new VideoMessage();
                    videoMessage.setSuccessVideoId(UUID.randomUUID().toString());
                    videoMessage.setErrorVideoId(UUID.randomUUID().toString());
                    videoMessage.setVideoId(UUID.randomUUID().toString());
                    videoMessage.setLocalErrorVideoPath(getIntent().getStringExtra("video_url3"));
                    videoMessage.setLocalSuccessVideoPath(getIntent().getStringExtra("video_url2"));
                    videoMessage.setLocalVideoPath(getIntent().getStringExtra("video_url1"));
                    videoMessage.setId(UUID.randomUUID().toString());
                    List<String> cardIds = new ArrayList<>();
                    String successCardId = null;
                    for (Card card : tmpCards.get(0)) {
                        if (card.getText().trim().equals(word)) {
                            successCardId = card.getId();
                            cardIds.add(card.getId());
                        }
                    }
                    videoMessage.setCorrectCardId(successCardId);
                    videoMessage.setCardIds(cardIds);
                    APIHelper.saveAndSendMessage(OptionsActivity.this, videoMessage, "984fefc9-5290-48d6-b576-305ab9e9ebf2", new Callback() {
                        @Override
                        public void onSuccess(Object result) {

                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });

                }
            });
        }

        if (tmpCards.size() >= 1 && tmpCards.get(1) != null) {
            gridView2.setAdapter(new CardGridAdapter(this, tmpCards.get(1)));
            gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    VideoMessage videoMessage = new VideoMessage();
                    videoMessage.setSuccessVideoId(UUID.randomUUID().toString());
                    videoMessage.setErrorVideoId(UUID.randomUUID().toString());
                    videoMessage.setVideoId(UUID.randomUUID().toString());
                    videoMessage.setLocalErrorVideoPath(getIntent().getStringExtra("video_url3"));
                    videoMessage.setLocalSuccessVideoPath(getIntent().getStringExtra("video_url2"));
                    videoMessage.setLocalVideoPath(getIntent().getStringExtra("video_url1"));
                    videoMessage.setId(UUID.randomUUID().toString());
                    List<String> cardIds = new ArrayList<>();
                    String successCardId = null;
                    for (Card card : tmpCards.get(1)) {
                        if (card.getText().trim().equals(word)) {
                            successCardId = card.getId();
                            cardIds.add(card.getId());
                        }
                    }
                    videoMessage.setCorrectCardId(successCardId);
                    videoMessage.setCardIds(cardIds);
                    APIHelper.saveAndSendMessage(OptionsActivity.this, videoMessage, "984fefc9-5290-48d6-b576-305ab9e9ebf2", new Callback() {
                        @Override
                        public void onSuccess(Object result) {

                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });

                }
            });
        }
        if (tmpCards.size() >= 2 && tmpCards.get(2) != null) {
            gridView3.setAdapter(new CardGridAdapter(this, tmpCards.get(2)));
            gridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    VideoMessage videoMessage = new VideoMessage();
                    videoMessage.setSuccessVideoId(UUID.randomUUID().toString());
                    videoMessage.setErrorVideoId(UUID.randomUUID().toString());
                    videoMessage.setVideoId(UUID.randomUUID().toString());
                    videoMessage.setLocalErrorVideoPath(getIntent().getStringExtra("video_url3"));
                    videoMessage.setLocalSuccessVideoPath(getIntent().getStringExtra("video_url2"));
                    videoMessage.setLocalVideoPath(getIntent().getStringExtra("video_url1"));
                    videoMessage.setId(UUID.randomUUID().toString());
                    List<String> cardIds = new ArrayList<>();
                    String successCardId = null;
                    for (Card card : tmpCards.get(2)) {
                        if (card.getText().trim().equals(word)) {
                            successCardId = card.getId();
                            cardIds.add(card.getId());
                        }
                    }
                    videoMessage.setCorrectCardId(successCardId);
                    videoMessage.setCardIds(cardIds);
                    APIHelper.saveAndSendMessage(OptionsActivity.this, videoMessage, "984fefc9-5290-48d6-b576-305ab9e9ebf2", new Callback() {
                        @Override
                        public void onSuccess(Object result) {

                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });

                }
            });
        }
    }
}
