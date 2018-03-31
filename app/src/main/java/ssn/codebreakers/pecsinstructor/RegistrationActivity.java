package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;

public class RegistrationActivity extends AppCompatActivity {
    Button studentButton;
    Button parentButton;
    LinearLayout registerAs;
    LinearLayout details;
    EditText name_text;
    EditText phone_text;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        studentButton=(Button)findViewById(R.id.studentBtn);
        parentButton=(Button)findViewById(R.id.parentBtn);
        registerAs=(LinearLayout)findViewById(R.id.register_as);
        details=(LinearLayout)findViewById(R.id.details);
        name_text=(EditText)findViewById(R.id.name_id);
        phone_text=(EditText)findViewById(R.id.phone_id);
        registerBtn=(Button)findViewById(R.id.register_id);
        parentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAs.setVisibility(View.GONE);
                details.setVisibility(View.VISIBLE);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=name_text.getText().toString();
                String phone=phone_text.getText().toString();
                APIHelper.registerInstructor(getApplicationContext(), name, phone, new Callback() {
                    @Override
                    public void onSuccess(Object result) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        sharedPreferences.edit().putBoolean("registered_already", true).apply();
                        startActivity(new Intent(RegistrationActivity.this,NewHomeScreen.class));
                        finish();
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });
            }
        });
    }
}
