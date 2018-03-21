package ssn.codebreakers.pecsinstructor.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

import ssn.codebreakers.pecsinstructor.db.helpers.UserHelper;
import ssn.codebreakers.pecsinstructor.db.models.User;

import static android.content.Context.MODE_PRIVATE;

public class CommonUtils
{
    private static final String USER_PREF = "user_preference";

    public static String getUniqueRandomID()
    {
        return UUID.randomUUID().toString();
    }

    public static String getSelfUserID(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
        String userId = preferences.getString("user_id", null);
        if(userId == null)
        {
            userId = getUniqueRandomID();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_id", userId);
            editor.apply();
        }
        return userId;
    }

    public static User getSelfUser(Context context)
    {
        return UserHelper.getUser(context, getSelfUserID(context));
    }
}
