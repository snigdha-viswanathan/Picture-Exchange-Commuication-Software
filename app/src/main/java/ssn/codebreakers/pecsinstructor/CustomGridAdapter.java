package ssn.codebreakers.pecsinstructor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kandhavel on 20-Mar-18.
 */

public class CustomGridAdapter extends BaseAdapter {
    private Context context;
    private final String[] gridValues;

    //Constructor to initialize values
    public CustomGridAdapter(Context context, String[ ] gridValues) {

        this.context        = context;
        this.gridValues     = gridValues;
    }

    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return gridValues.length;
    }

    @Override
    public Object getItem(int position) {

        return null;
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

            // get layout from grid_item.xml ( Defined Below )

            gridView = inflater.inflate( R.layout.layout , null);

            // set value into textview

            TextView textView = (TextView) gridView
                    .findViewById(R.id.textView);

            textView.setText(gridValues[position]);

            // set image based on selected text

            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.imageView);

            String arrLabel = gridValues[ position ];

            if (arrLabel.equals("GRID_DATA[0]")) {

                imageView.setImageResource(R.drawable.fruits);

            } else if (arrLabel.equals("veges")) {

                imageView.setImageResource(R.drawable.vege);

            } else if (arrLabel.equals("animals")) {

                imageView.setImageResource(R.drawable.animals);

            } else {

                imageView.setImageResource(R.drawable.none);
            }

        } else {

            gridView = (View) convertView;
        }

        return gridView;
    }
}

