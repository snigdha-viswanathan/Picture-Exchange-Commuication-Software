package ssn.codebreakers.pecsinstructor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.models.User;

@Dao
public interface UserDao
{
    @Insert
    long addUser(User user);

    @Query("SELECT * FROM User WHERE instructor = 1")
    List<User> getInstructors();

    @Query("SELECT * FROM User WHERE instructor = 0")
    List<User> getAutisticUsers();

    @Query("SELECT * FROM User WHERE id = :id")
    User getUser(String id);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
