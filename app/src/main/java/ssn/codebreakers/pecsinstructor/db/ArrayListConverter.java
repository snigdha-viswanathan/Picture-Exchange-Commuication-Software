package ssn.codebreakers.pecsinstructor.db;

import android.arch.persistence.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ArrayListConverter
{
    @TypeConverter
    public static List<String> fromJSONArray(String jsonText)
    {
        if(jsonText == null)
            return null;
        List<String> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonText);
            for(int i=0; i<jsonArray.length(); i++)
                arrayList.add(jsonArray.getString(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @TypeConverter
    public static String fromArrayList(List<String> list)
    {
        if(list == null)
            return null;
        JSONArray jsonArray = new JSONArray();
        for(String val: list)
            jsonArray.put(val);
        return jsonArray.toString();
    }
}
