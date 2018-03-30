package ssn.codebreakers.pecsinstructor;

import android.support.v7.app.AppCompatActivity;
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

public class NewHomeScreen extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home_screen);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_item);
        String[] values = new String[]{"Sarah", "Snigdha","Priya","Samantika","Nafisa","Yeshwanthraa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(NewHomeScreen.this, ThreadView.class);
                intent.putExtra("user_id", position);
                startActivity(intent);
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
