package ssn.codebreakers.pecsinstructor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class GameScreenActivity extends AppCompatActivity{


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
       listView = findViewById(R.id.play);
       /* level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),GameScreenAct2.class);
                startActivity(i);
                setContentView(R.layout.game_levels);
            }
        });
        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),GameScreenAct2.class);
                startActivity(i);
                setContentView(R.layout.game_levels);
            }
        });
        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),GameScreenAct2.class);
                startActivity(i);
                setContentView(R.layout.game_levels);
            }
        });*/
       String[] levels = new String[] {"level1","level2","level3"};
        listView.setAdapter(new ArrayAdapter(GameScreenActivity.this,R.layout.list_item,R.id.list_item,levels));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),GameLevelsActivity.class);
                intent.putExtra("level",i);
                startActivity(intent);




            }
        });

    }
}
