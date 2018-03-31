package ssn.codebreakers.pecsinstructor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;

/**
 * Created by kandhavel on 20-Mar-18.
 */

public class CategoryGridAdapter extends BaseAdapter {
    private Context context;
    private List categories;

    //Constructor to initialize values
    public CategoryGridAdapter(Context context, List categories) {
        this.context        = context;
        this.categories     = categories;
    }

    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return categories.size();
    }

    @Override
    public Object getItem(int position) {

        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(int position, View convertView, ViewGroup parent) {

        // LayoutInflator to call external grid_item.xml file

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get card_item from grid_item.xml ( Defined Below )

            gridView = inflater.inflate( R.layout.card_item , null);

            // set value into textview



        } else {

            gridView = (View) convertView;
        }

        TextView textView = (TextView) gridView
                .findViewById(R.id.textView);

        textView.setText(((Category)categories.get(position)).getName());

        // set image based on selected text

        ImageView imageView = (ImageView) gridView
                .findViewById(R.id.imageView);

        Category category = (Category) categories.get(position);
        List<Card> cards = CardHelper.getCardsOfCategory(this.context,category.getId());
        if(cards.size()!=0 && cards.get(0).getLocalImagePath()!=null) {
            String imagePath = cards.get(0).getLocalImagePath();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
            if(bitmap!= null)
            {
                bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                imageView.setImageBitmap(bitmap);
            }else
            {
                imageView.setImageResource(R.drawable.none);
            }
        }
        else {
            imageView.setImageResource(R.drawable.none);
        }

        //String arrLabel = ((Category)categories.get(position)).get

        return gridView;
    }
}

