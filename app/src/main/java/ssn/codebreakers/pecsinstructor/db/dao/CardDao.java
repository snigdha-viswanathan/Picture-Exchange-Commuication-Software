package ssn.codebreakers.pecsinstructor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.models.Card;

@Dao
public interface CardDao
{
    @Insert
    long addCard(Card card);

    @Query("SELECT * FROM Card where id = :id")
    Card get(String id);

    @Query("SELECT * FROM Card where categoryId = :categoryId")
    List<Card> getCardsOfCategory(String categoryId);

    @Update
    void updateCard(Card card);

    @Delete
    void deleteCard(Card card);
}
