package ssn.codebreakers.pecsinstructor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ssn.codebreakers.pecsinstructor.db.dao.LevelsDao;
import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.helpers.SpeechHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;


public class GameLevelsActivity extends AppCompatActivity {
    SpeechHelper speechHelper;

    ImageView searchImageButton;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_levels);
        final int level = getIntent().getIntExtra("level",-1);
        searchImageButton = findViewById(R.id.searchImageButton);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        image = findViewById(R.id.image);
        speechHelper = new SpeechHelper(getApplicationContext(), this);//helper object for speech related APIs
        /*searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edittext.getText().toString();
                speechHelper.textToSpeech(text);
            }
        });*/

            List<Category> categories= CategoryHelper.getAllCategories(this);
           int categoryLen= categories.size();
           Random random = new Random();
           int random1 = random.nextInt(categoryLen);
           int random2 = random.nextInt(categoryLen);
           int random3 = random.nextInt(categoryLen);
           final Card cards1=CardHelper.getRandomCardOfCategory(this,categories.get(random1).getId());
           final Card cards2=CardHelper.getRandomCardOfCategory(this,categories.get(random2).getId());
           final Card cards3=CardHelper.getRandomCardOfCategory(this,categories.get(random3).getId());

           Map<Integer,Card> mapOfCards = new HashMap<>();
           mapOfCards.put(0,cards1);
           mapOfCards.put(1,cards2);
           mapOfCards.put(2,cards3);
           final int randomCardNo = random.nextInt(3);
          final Card randomSuccessCard = mapOfCards.get(randomCardNo);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(cards1.getLocalImagePath(), bmOptions);
            //bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            imageView1.setImageBitmap(bitmap);
            Bitmap bitmap2 = BitmapFactory.decodeFile(cards2.getLocalImagePath(), bmOptions);
            //bitmap2 = Bitmap.createScaledBitmap(bitmap2, 100, 100, true);
            imageView2.setImageBitmap(bitmap2);
            Bitmap bitmap3 = BitmapFactory.decodeFile(cards3.getLocalImagePath(), bmOptions);
            //bitmap3 = Bitmap.createScaledBitmap(bitmap3, 100, 100, true);
            imageView3.setImageBitmap(bitmap3);

              searchImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    speechHelper.textToSpeech(randomSuccessCard.getText());
                }
            });
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(randomCardNo==0){
                    image.setImageResource(R.drawable.success);
                    }
                    else{
                        image.setImageResource(R.drawable.wrong);
                    }




                }
            }
            );
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (randomCardNo == 1) {
                        image.setImageResource(R.drawable.success);

                    } else {
                        image.setImageResource(R.drawable.wrong);
                    }

                }
            }
            );
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(randomCardNo==2){
                        image.setImageResource(R.drawable.success);
                }
                else{
                        image.setImageResource(R.drawable.wrong);
                    }
            }
            }
            );




}
}

