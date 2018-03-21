package ssn.codebreakers.pecsinstructor.helpers;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ssn.codebreakers.pecsinstructor.db.helpers.CategoryHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.MessageHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.SimpleMessageHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.UserHelper;
import ssn.codebreakers.pecsinstructor.db.helpers.VideoMessageHelper;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Category;
import ssn.codebreakers.pecsinstructor.db.models.Message;
import ssn.codebreakers.pecsinstructor.db.models.SimpleMessage;
import ssn.codebreakers.pecsinstructor.db.models.User;
import ssn.codebreakers.pecsinstructor.db.models.VideoMessage;

public class APIHelper
{
    //private static final String hostName = "http://192.168.1.3:8080/pecs/";
    private static final String hostName = "http://pecs.us-east-1.elasticbeanstalk.com/";

    /**
     * Save the message locally in db and send it
     * @param messageObject VideoMessage object or SimpleMessage object
     * @param toUserId user id to send message
     */
    public static void saveAndSendMessage(Context context, Object messageObject, String toUserId, final Callback callback)
    {
        try{
            Message message = new Message();
            message.setId(CommonUtils.getUniqueRandomID());
            message.setToUserId(toUserId);
            message.setFromUserId(CommonUtils.getSelfUserID(context));
            message.setSeen(false);
            JSONObject parameters = new JSONObject();
            if(messageObject instanceof VideoMessage)
            {
                message.setMessageType(Message.VIDEO_MESSAGE);
                message.setMessageObjectId(((VideoMessage) messageObject).getId());
                VideoMessageHelper.addVideoMessage(context, (VideoMessage)messageObject);
                parameters.put("video_message", new Gson().toJson(messageObject));
            }else if(messageObject instanceof SimpleMessage)
            {
                message.setMessageType(Message.SIMPLE_MESSAGE);
                message.setMessageObjectId(((SimpleMessage) messageObject).getId());
                SimpleMessageHelper.addSimpleMessage(context, (SimpleMessage) messageObject);
                parameters.put("simple_message", new Gson().toJson(messageObject));
            }else
            {
                throw new RuntimeException("messageObject must be of type SimpleMessage or VideoMessage");
            }

            MessageHelper.addMessage(context, message);
            parameters.put("message", new Gson().toJson(message));
            sendJsonRequest(context, parameters, getAPIURL("SendMessage"), callback);
        }catch (RuntimeException e)
        {
            throw e;
        }catch (Exception e)
        {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    /**
     * Add card in remote db for the user, and notify related people to update the new card
     */
    public static void updateNewCard(Context context, Card card, Callback callback)
    {
        try {
            JSONObject parameters = new JSONObject();
            Category category = CategoryHelper.getCategory(context, card.getCategoryId());
            parameters.put("by", new Gson().toJson(CommonUtils.getSelfUser(context)));
            parameters.put("card", new Gson().toJson(card));
            parameters.put("category", new Gson().toJson(category));
            sendJsonRequest(context, parameters, getAPIURL("AddCard"), callback);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    /**
     * Add user details to remote db(instructor)
     */
    public static void registerInstructor(Context context, String name, String phoneNumber, Callback callback)
    {
        User user = new User();
        user.setId(CommonUtils.getSelfUserID(context));
        user.setInstructor(true);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        try {
            UserHelper.addUser(context, user);
        }catch (Exception e)
        {
            System.out.println("user already exists");
        }
        try {
            JSONObject parameters = new JSONObject(new Gson().toJson(user));
            sendJsonRequest(context, parameters, getAPIURL("RegisterUser"), callback);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    /**
     * Add user details to remote db(autistic person)
     */
    public static void registerAutisticPerson(Context context, String name, String phoneNumber, Callback callback)
    {
        User user = new User();
        user.setId(CommonUtils.getSelfUserID(context));
        user.setInstructor(false);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        try{
            UserHelper.addUser(context, user);
        }catch (Exception e){System.out.println("user already exists");}
        try {
            JSONObject parameters = new JSONObject(new Gson().toJson(user));
            sendJsonRequest(context, parameters, getAPIURL("RegisterUser"), callback);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    /**
     * Adding an instructor for an autistic person in remote db
     * @param phoneNumber phone number of the instructor to identity him
     */
    public static void addInstructor(Context context, String phoneNumber, Callback callback)
    {
        try {
            JSONObject parameters = new JSONObject();
            parameters.put("phone_number", phoneNumber);
            parameters.put("user_id", CommonUtils.getSelfUserID(context));
            sendJsonRequest(context, parameters, getAPIURL("AddInstructor"), callback);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    public static void updateFCMToken(Context context, Callback callback)
    {
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            JSONObject parameters = new JSONObject();
            parameters.put("user_id", CommonUtils.getSelfUserID(context));
            parameters.put("token", token);
            sendJsonRequest(context, parameters, getAPIURL("UpdateFCMToken"), callback);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    private static void sendJsonRequest(Context context, JSONObject parameters, String url, final Callback callback)
    {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onError(error.getMessage());
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                //DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                10000,//10 second timeout
                0, // No retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(jsonRequest);
    }

    private static String getAPIURL(String api)
    {
        return hostName + api;
    }

    /*
        //Example usage

        SimpleMessage simpleMessage = new SimpleMessage();
        simpleMessage.setId(CommonUtils.getUniqueRandomID());
        simpleMessage.setText("Hi!! test message..!!");

        APIHelper.saveAndSendMessage(getApplicationContext(), simpleMessage, [touserid], new Callback() {
            @Override
            public void onSuccess(Object result) {
                JSONObject jsonObject = (JSONObject)result;
                System.out.println("message sent : "+jsonObject.toString());
            }

            @Override
            public void onError(Object error) {
                System.out.println("error sending message "+error);
            }
        });



        APIHelper.registerInstructor(getApplicationContext(), "thg", "9840124959", new Callback() {
            @Override
            public void onSuccess(Object result) {
                System.out.println("registered successfully");
            }

            @Override
            public void onError(Object error) {
                System.out.println("error");
            }
        });



        APIHelper.addInstructor(getApplicationContext(), "9840124959", new Callback() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(Object error) {

            }
        });


        Category category = new Category();
        category.setId(CommonUtils.getUniqueRandomID());
        category.setName("fruits");
        CategoryHelper.addCategory(getApplicationContext(), category);

        Card card = new Card();
        card.setId(CommonUtils.getUniqueRandomID());
        card.setText("orange");
        card.setCategoryId(category.getId());
        CardHelper.addCard(getApplicationContext(), card);

        APIHelper.updateNewCard(getApplicationContext(), card, new Callback() {
            @Override
            public void onSuccess(Object result) {
                System.out.println("card updated");
            }

            @Override
            public void onError(Object error) {
                System.out.println("card update failed");
            }
        });
     */
}
