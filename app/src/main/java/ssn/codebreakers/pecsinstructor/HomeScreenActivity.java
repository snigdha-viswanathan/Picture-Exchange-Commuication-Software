package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        final Button imagePickerButton = findViewById(R.id.imagePickerButton);
        final Button fileUploaderButton = findViewById(R.id.fileUploaderButton);
        final Button databaseButton = findViewById(R.id.databaseButton);
        final Button recordVideoButton = findViewById(R.id.recordVideo);

        //open imagepicker sample activity on button click
        imagePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ImagePickerExampleActivity.class));
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
                startActivity(new Intent(getApplicationContext(), RecordVideoSampleActivity.class));
            }
        });
    }


}
