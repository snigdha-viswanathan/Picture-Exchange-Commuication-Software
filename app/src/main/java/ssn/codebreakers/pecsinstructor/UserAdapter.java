package ssn.codebreakers.pecsinstructor;

/**
 * Created by Snigdha on 31-03-2018.
 */

import android.content.Context;
import android.widget.BaseAdapter;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.List;

        import ssn.codebreakers.pecsinstructor.db.models.Card;
        import ssn.codebreakers.pecsinstructor.db.models.User;

/**
 * Created by Snigdha on 30-03-2018.
 */

public class UserAdapter extends BaseAdapter {
    Context context;
    List<User> users;
    public UserAdapter(Context context, List<User> users)
    {
        this.context=context;
        this.users=users;
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // LayoutInflator to call external grid_item.xml file

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get card_item from grid_item.xml ( Defined Below )

            gridView = inflater.inflate( R.layout.user_item_view , null);

            // set value into textview



        } else {

            gridView = (View) convertView;
        }

        TextView textView = (TextView) gridView.findViewById(R.id.textView);

        textView.setText(((User)users.get(position)).getName());




        return gridView;
    }
}

