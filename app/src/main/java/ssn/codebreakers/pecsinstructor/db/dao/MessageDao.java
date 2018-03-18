package ssn.codebreakers.pecsinstructor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.models.Message;

@Dao
public interface MessageDao
{
    @Insert
    long addMessage(Message message);

    @Query("SELECT * FROM Message")
    List<Message> getAllMessages();

    @Query("SELECT * FROM Message where fromUserId = :fromUserId")
    List<Message> getMessages(String fromUserId);

    @Update
    void updateMessage(Message message);

    @Delete
    void deleteMessage(Message message);
}
