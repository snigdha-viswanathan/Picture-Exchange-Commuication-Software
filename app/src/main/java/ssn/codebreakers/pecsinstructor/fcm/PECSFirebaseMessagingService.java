package ssn.codebreakers.pecsinstructor.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import ssn.codebreakers.pecsinstructor.db.helpers.MessageHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.SimpleMessageHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.UserHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.VideoMessageHelper;
import ssn.codebreakers.pecsinstructor.db.models.Message;
import ssn.codebreakers.pecsinstructor.db.models.SimpleMessage;
import ssn.codebreakers.pecsinstructor.db.models.User;
import ssn.codebreakers.pecsinstructor.db.models.VideoMessage;
import ssn.codebreakers.pecsinstructor.helpers.NotificationUtils;

public class PECSFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "PECSInstructorFirebase";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                JSONObject dataJson = new JSONObject(remoteMessage.getData());
                String type = dataJson.getString("type");
                if(type.equals("add_instructor"))
                {
                    JSONObject data = new JSONObject(dataJson.getString("data"));
                    User user = new Gson().fromJson(data.getString("user"), User.class);
                    UserHelper.addUser(getApplicationContext(), user);
                    NotificationUtils.showNotification(getApplicationContext(), "New student "+ user.getName()+ " added.");
                }else if(type.equals("message"))
                {
                    JSONObject data = new JSONObject(dataJson.getString("data"));
                    Message message = new Gson().fromJson(data.getString("message"), Message.class);
                    MessageHelper.addMessage(getApplicationContext(), message);
                    if(message.getMessageType() == Message.SIMPLE_MESSAGE)
                    {
                        SimpleMessage simpleMessage = new Gson().fromJson(data.getString("simple_message"), SimpleMessage.class);
                        SimpleMessageHelper.addSimpleMessage(getApplicationContext(), simpleMessage);
                    }else if(message.getMessageType() == Message.VIDEO_MESSAGE)
                    {
                        VideoMessage videoMessage = new Gson().fromJson(data.getString("video_message"), VideoMessage.class);
                        VideoMessageHelper.addVideoMessage(getApplicationContext(), videoMessage);
                    }
                    User fromUser = UserHelper.getUser(getApplicationContext(), message.getFromUserId());
                    NotificationUtils.showNotification(getApplicationContext(), "New message from "+ fromUser.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
