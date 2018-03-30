package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class TitleActivity extends AppCompatActivity {
    Button okButton;
    EditText inputTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        inputTitle = findViewById(R.id.input_title);
        okButton=findViewById(R.id.oks);
        inputTitle.getText().toString();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecordVideoActivity.class));
            }
        });

    }
}
