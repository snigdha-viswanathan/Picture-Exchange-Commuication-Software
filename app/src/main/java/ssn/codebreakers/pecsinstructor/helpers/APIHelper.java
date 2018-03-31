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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.GenericSignatureFormatError;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ssn.codebreakers.pecsinstructor.db.helpers.CardHelper;
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
    public static void saveAndSendMessage(final Context context, Object messageObject, String toUserId, final Callback callback, final ProgressCallback uploadCallback)
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
                final VideoMessage videoMessage = ((VideoMessage) messageObject);
                final FileUploader fileUploader = new FileUploader(context);
                System.out.println("local video path "+videoMessage.getVideoId()+" : "+videoMessage.getLocalVideoPath());
                fileUploader.uploadFile(new File(videoMessage.getLocalVideoPath()), videoMessage.getVideoId(), new ProgressCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        fileUploader.uploadFile(new File(videoMessage.getLocalSuccessVideoPath()), videoMessage.getSuccessVideoId(), new ProgressCallback() {
                            @Override
                            public void onSuccess(Object result) {
                                fileUploader.uploadFile(new File(videoMessage.getLocalErrorVideoPath()), videoMessage.getErrorVideoId(), uploadCallback);
                            }

                            @Override
                            public void onProgress(int progress) {

                            }

                            @Override
                            public void onError(Object error) {

                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onError(Object error) {

                    }
                });
                message.setMessageType(Message.VIDEO_MESSAGE);
                message.setMessageObjectId(videoMessage.getId());
                VideoMessage videoMsg = (VideoMessage) messageObject;
                List<String> cardIds = videoMsg.getCardIds();
                List<Card> cards = new ArrayList<>();
                List<Category> categories= new ArrayList<>();
                for ( String id: cardIds) {
                    Card card = CardHelper.getCard(context,id);
                    cards.add(card);
                    categories.add(CategoryHelper.getCategory(context,card.getCategoryId()));
                }
                VideoMessageHelper.addVideoMessage(context, videoMsg);

                parameters.put("video_message", new Gson().toJson(messageObject));
                parameters.put("cards",new Gson().toJson(cards));
                parameters.put("categories",new Gson().toJson(categories));
            }else if(messageObject instanceof SimpleMessage)
            {
                message.setMessageType(Message.SIMPLE_MESSAGE);
                message.setMessageObjectId(((SimpleMessage) messageObject).getId());
                SimpleMessageHelper.addSimpleMessage(context, (SimpleMessage) messageObject);
                SimpleMessage simpleMessage = (SimpleMessage) messageObject;
                List<String> cardIds = simpleMessage.getCardIds();
                List<Card> cards = new ArrayList<>();
                List<Category> categories= new ArrayList<>();
                for ( String id: cardIds) {
                    Card card = CardHelper.getCard(context,id);
                    cards.add(card);
                    categories.add(CategoryHelper.getCategory(context,card.getCategoryId()));
                }
                parameters.put("simple_message", new Gson().toJson(messageObject));
                parameters.put("cards",new Gson().toJson(cards));
                parameters.put("categories",new Gson().toJson(categories));

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
    public static void addInstructor(final Context context, String phoneNumber, final Callback callback)
    {
        try {
            JSONObject parameters = new JSONObject();
            parameters.put("phone_number", phoneNumber);
            parameters.put("user", new Gson().toJson(CommonUtils.getSelfUser(context)));
            sendJsonRequest(context, parameters, getAPIURL("AddInstructor"), new Callback() {
                @Override
                public void onSuccess(Object result) {
                    JSONObject jsonResponse = (JSONObject) result;
                    try {
                        User instructor = new Gson().fromJson(jsonResponse.getString("instructor"), User.class);
                        UserHelper.addUser(context, instructor);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.onSuccess(result);
                }

                @Override
                public void onError(Object error) {
                    callback.onError(error);
                }
            });
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

    public static void getCardsForWord(Context context, final List<List<Card>> cards, final List<Category> categoryList, String word, final Callback callback)
    {
        try {
            JSONObject parameters = new JSONObject();
            parameters.put("word", word);
            sendJsonRequest(context, parameters, getAPIURL("GetCardsForWord"), new Callback() {
                @Override
                public void onSuccess(Object result) {
                    JSONObject jsonResponse = (JSONObject) result;
                    Gson gson = new Gson();
                    try {
                        List<List<Card>> tmpCards = gson.fromJson(jsonResponse.getString("cards"), new TypeToken<List<List<Card>>>(){}.getType());
                        List<Category> tmpCategories = gson.fromJson(jsonResponse.getString("categories"), new TypeToken<List<Category>>(){}.getType());
                        cards.addAll(tmpCards);
                        categoryList.addAll(tmpCategories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.onSuccess(result);
                }

                @Override
                public void onError(Object error) {
                    callback.onError(error);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    public static void getCardsForSentence(Context context, final List<List<Card>> cards,final List<Category> categoryList, String sentence, final Callback callback)
    {
        try {
            JSONObject parameters = new JSONObject();
            parameters.put("sentence", sentence);
            sendJsonRequest(context, parameters, getAPIURL("GetCardsForSentence"), new Callback() {
                @Override
                public void onSuccess(Object result) {
                    JSONObject jsonResponse = (JSONObject) result;
                    Gson gson = new Gson();
                    try {
                        List<List<Card>> tmpCards = gson.fromJson(jsonResponse.getString("cards"), new TypeToken<List<List<Card>>>(){}.getType());
                        List<Category> tmpCategories = gson.fromJson(jsonResponse.getString("categories"), new TypeToken<List<Category>>(){}.getType());
                        cards.addAll(tmpCards);
                        categoryList.addAll(tmpCategories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.onSuccess(result);
                }

                @Override
                public void onError(Object error) {
                    callback.onError(error);
                }
            });
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


        final List<List<Card>> listOfCards = new ArrayList<>();
        final List<Category> categories = new ArrayList<>();
        APIHelper.getCardsForWord(getApplicationContext(), listOfCards, categories, "eat", new Callback() {
            @Override
            public void onSuccess(Object result) {
                for(List<Card> cards: listOfCards)
                {
                    System.out.println("option 1");
                    for(Card card: cards)
                        System.out.println(card.getText()+":"+card.getImageId());
                }
                for(Category category: categories)
                    System.out.println("category : "+category.getName());

            }

            @Override
            public void onError(Object error) {

            }
        });
     */
}
