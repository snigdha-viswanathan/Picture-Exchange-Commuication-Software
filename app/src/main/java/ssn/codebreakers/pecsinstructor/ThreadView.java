package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.MessageHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.db.models.Message;
import ssn.codebreakers.pecsinstructor.db.models.User;
import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.SpeechHelper;

import static ssn.codebreakers.pecsinstructor.helpers.SpeechHelper.SPEECH_REQUEST_ID;

public class ThreadView extends AppCompatActivity {
    Button speak_btn;
    Button rec_video_btn;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_view);
        speak_btn=(Button)findViewById(R.id.speak_btn);
        rec_video_btn=(Button)findViewById(R.id.rec_video_btn);
        listView = findViewById(R.id.listview);

        rec_video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
                Bundle bundle = new Bundle();
                //intent.putExtras(bundle);
                final String userId = getIntent().getStringExtra("user_id");
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        List<Message> messages = MessageHelper.getAllMessages(getApplicationContext());
        System.out.println("message count = "+messages.size());
        MessageAdapter messageAdapter = new MessageAdapter(getApplicationContext(), messages);
        listView.setAdapter(messageAdapter);
    }
    public void speech(View view) {
        SpeechHelper speechHelper = new SpeechHelper(getApplicationContext(), this);
        speechHelper.speechToText();
    }
    //result of the speechtotext
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
                List<Category> categoryList = new ArrayList<>();
                APIHelper.getCardsForSentence(getApplicationContext(), listOfCards, categoryList, spokenText, new Callback() {
                    @Override
                    public void onSuccess(Object result) {
                        for (List<Card> cards : listOfCards ) {
                            for(Card card:cards) {
                                CardHelper.addCard(ThreadView.this,card);
                            }
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
