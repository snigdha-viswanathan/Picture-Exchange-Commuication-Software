package ssn.codebreakers.pecsinstructor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import ssn.codebreakers.pecsinstructor.db.models.VideoMessage;

@Dao
public interface VideoMessageDao
{
    @Insert
    long addVideoMessage(VideoMessage videoMessage);

    @Query("SELECT * FROM VideoMessage where id = :id")
    VideoMessage get(String id);

    @Update
    void updateVideoMessage(VideoMessage videoMessage);

    @Delete
    void deleteVideoMessage(VideoMessage videoMessage);
}
