package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class PreviewVideo3 extends AppCompatActivity {

    Button retryButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video3);
        VideoView preview = (VideoView) findViewById(R.id.Preview);
        String videoUrl = getIntent().getStringExtra("video_url");
        preview.setVideoPath(videoUrl);
        preview.start();


        retryButton = findViewById(R.id.Retry);
        nextButton = findViewById(R.id.Next);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecordVideo3.class));
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OptionsActivity.class));
            }
        });
    }
}

