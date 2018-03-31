package ssn.codebreakers.pecsinstructor;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.Toast;

import ssn.codebreakers.pecsinstructor.db.helpers.UserHelper;
import ssn.codebreakers.pecsinstructor.db.models.User;
import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;

public class NewHomeScreen extends AppCompatActivity {
    ListView listView;
    Button galleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home_screen);
        galleryButton = findViewById(R.id.gallery_button);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean registeredAlready = sharedPreferences.getBoolean("registered_already", false);
        System.out.println("reg"+registeredAlready);

        if(!registeredAlready)
        {
            startActivity(new Intent(NewHomeScreen.this,RegistrationActivity.class));
            finish();
        }

        listView = (ListView) findViewById(R.id.list_item);
        final UserAdapter userAdapter=new UserAdapter(getApplicationContext(), UserHelper.getAllAutisticUsers(getApplicationContext()));
        listView.setAdapter(userAdapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ThreadView.class);
                Bundle bundle = new Bundle();
                //intent.putExtras(bundle);
                intent.putExtra("user_id", ((User)userAdapter.getItem(position)).getId());
                startActivity(intent);
            }
        });
       galleryButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), GalleryActivity.class));
           }
       });
        APIHelper.updateFCMToken(getApplicationContext(), new Callback() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(Object error) {

            }
        });



    }


    //final Context context = this;
    //Intent intent = new Intent(context, Screen2Activity.class);
    //startActivity(intent);
    //HomeScreenActivity.this.startActivity(intent);
    //Intent intent = new Intent(getApplicationContext(),Screen2Activity.class);
    //startActivity(intent);
}
