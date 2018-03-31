package ssn.codebreakers.pecsinstructor.db.helpers;

import android.arch.persistence.room.Room;
import android.content.Context;

import ssn.codebreakers.pecsinstructor.db.LocalDatabase;
import ssn.codebreakers.pecsinstructor.db.models.Levels;

/**
 * Created by NAFISA SAIDA on 30-03-2018.
 */

public class LevelHelper {

    public static Levels get(Context context, int levelId)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        Levels level = localDatabase.levelsDao().get(levelId);
        localDatabase.close();
        return level;

    }


}
