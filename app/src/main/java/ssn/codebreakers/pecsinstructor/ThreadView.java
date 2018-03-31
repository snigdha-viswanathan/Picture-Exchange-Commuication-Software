package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.db.models.User;
import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;
import ssn.codebreakers.pecsinstructor.helpers.SpeechHelper;

import static ssn.codebreakers.pecsinstructor.helpers.SpeechHelper.SPEECH_REQUEST_ID;

public class ThreadView extends AppCompatActivity {
    Button speak_btn;
    Button rec_video_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_view);
        speak_btn=(Button)findViewById(R.id.speak_btn);
        rec_video_btn=(Button)findViewById(R.id.rec_video_btn);
        speak_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userId = getIntent().getStringExtra("user_id");
                Intent intent=new Intent(ThreadView.this,DeckChooserActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });
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
    }

    //result of the speechtotext

}
