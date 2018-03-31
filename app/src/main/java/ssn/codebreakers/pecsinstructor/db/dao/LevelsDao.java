package ssn.codebreakers.pecsinstructor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;



import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.db.models.Levels;

/**
 * Created by NAFISA SAIDA on 30-03-2018.
 */
@Dao
public interface LevelsDao {
    @Update
    void updateLevel(Levels levels);

    @Query("SELECT * FROM levels")
    Levels getAllLevels();

    @Query("SELECT * FROM levels where id = :levelId")
    Levels get(int levelId);

}
