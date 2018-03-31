package ssn.codebreakers.pecsinstructor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.SimpleMessageHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.UserHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.VideoMessageHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Message;
import ssn.codebreakers.pecsinstructor.db.models.SimpleMessage;
import ssn.codebreakers.pecsinstructor.db.models.User;
import ssn.codebreakers.pecsinstructor.db.models.VideoMessage;
import ssn.codebreakers.pecsinstructor.helpers.CommonUtils;

/**
 * Created by thiag on 3/31/2018.
 */

public class MessageAdapter extends BaseAdapter {
    Context context;
    List<Message> messages;
    public MessageAdapter(Context context, List messages)
    {
        this.context=context;
        this.messages=messages;
    }
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        // LayoutInflator to call external grid_item.xml file

        System.out.println("message detail = "+messages.get(i).getFromUserId()+" : "+messages.get(i).getToUserId());
        if(messages.get(i).getFromUserId().equals(CommonUtils.getSelfUserID(context)))
            System.out.println("from me");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = inflater.inflate(R.layout.simple_message, null);


            // set value into textview



        } else {

            gridView = (View) convertView;
        }

        // get card_item from grid_item.xml ( Defined Below )
        if(messages.get(i).getMessageType() == Message.SIMPLE_MESSAGE) {

            SimpleMessage simpleMessage = SimpleMessageHelper.getSimpleMessage(context,messages.get(i).getMessageObjectId());
            List<Card> cards = new ArrayList<>();
            for(String id: simpleMessage.getCardIds())
            {
                Card card = CardHelper.getCard(context, id);
                cards.add(card);
                System.out.println("card name"+card.getText());
            }
            TextView simpleTextGridView = gridView.findViewById(R.id.message);
            simpleTextGridView.setText(simpleMessage.getText());
            TextView fromtext=gridView.findViewById(R.id.from);
            if(messages.get(i).getFromUserId().equals(CommonUtils.getSelfUserID(context)))
                fromtext.setText("you");
            else {
                User user = UserHelper.getUser(context, messages.get(i).getFromUserId());
                fromtext.setText(user.getName());
            }

        }
        else
        {
            VideoMessage videoMessage = VideoMessageHelper.getVideoMessage(context, messages.get(i).getMessageObjectId());
            gridView = inflater.inflate(R.layout.video_message, null);
            TextView textView = gridView.findViewById(R.id.message);
            if(videoMessage != null)
                textView.setText(videoMessage.getTitle());
        }





        return gridView;
    }
}
