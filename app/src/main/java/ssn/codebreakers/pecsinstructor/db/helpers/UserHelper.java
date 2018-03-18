package ssn.codebreakers.pecsinstructor.db.helpers;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.LocalDatabase;
import ssn.codebreakers.pecsinstructor.db.models.User;

public class UserHelper
{
    public static void addUser(Context context, User user)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.userDao().addUser(user);
        localDatabase.close();
    }

    public static List<User> getAllInstructors(Context context)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        List<User> users = localDatabase.userDao().getInstructors();
        localDatabase.close();
        return users;
    }

    public static List<User> getAllAutisticUsers(Context context)
    {
        List<User> users;
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        users = localDatabase.userDao().getAutisticUsers();
        localDatabase.close();
        return users;
    }
}
