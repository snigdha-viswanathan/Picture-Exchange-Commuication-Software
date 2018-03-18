package ssn.codebreakers.pecsinstructor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import ssn.codebreakers.pecsinstructor.db.models.SimpleMessage;

@Dao
public interface SimpleMessageDao
{
    @Insert
    long add(SimpleMessage simpleMessage);

    @Query("SELECT * FROM SimpleMessage where id = :id")
    SimpleMessage get(String id);

    @Update
    void updateSimpleMessage(SimpleMessage simpleMessage);

    @Delete
    void deleteSimpleMessage(SimpleMessage simpleMessage);
}
