package ssn.codebreakers.pecsinstructor.db.helpers;

import android.arch.persistence.room.Room;
import android.content.Context;

import ssn.codebreakers.pecsinstructor.db.LocalDatabase;
import ssn.codebreakers.pecsinstructor.db.models.SimpleMessage;

public class SimpleMessageHelper
{
    public static SimpleMessage getSimpleMessage(Context context, String simpleMessageId)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        SimpleMessage message = localDatabase.simpleMessageDao().get(simpleMessageId);
        localDatabase.close();
        return message;
    }

    public static void addSimpleMessage(Context context, SimpleMessage simpleMessage)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.simpleMessageDao().add(simpleMessage);
        localDatabase.close();
    }
}
