package ssn.codebreakers.pecsinstructor.db.helpers;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.LocalDatabase;
import ssn.codebreakers.pecsinstructor.db.models.Message;

public class MessageHelper
{
    public static void addMessage(Context context, Message message)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.messageDao().addMessage(message);
        localDatabase.close();
    }

    public static void getMessages(Context context, String fromUserId)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        List<Message> messages = localDatabase.messageDao().getMessages(fromUserId);
        localDatabase.close();
    }
}
