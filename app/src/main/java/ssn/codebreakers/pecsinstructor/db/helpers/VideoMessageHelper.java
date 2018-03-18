package ssn.codebreakers.pecsinstructor.db.helpers;

import android.arch.persistence.room.Room;
import android.content.Context;

import ssn.codebreakers.pecsinstructor.db.LocalDatabase;
import ssn.codebreakers.pecsinstructor.db.models.VideoMessage;

public class VideoMessageHelper
{
    public static VideoMessage getVideoMessage(Context context, String videoMessageId)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        VideoMessage message = localDatabase.videoMessageDao().get(videoMessageId);
        localDatabase.close();
        return message;
    }

    public static void addVideoMessage(Context context, VideoMessage videoMessage)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.videoMessageDao().addVideoMessage(videoMessage);
        localDatabase.close();
    }
}
