package ssn.codebreakers.pecsinstructor.db.helpers;


import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.LocalDatabase;
import ssn.codebreakers.pecsinstructor.db.models.Card;
import ssn.codebreakers.pecsinstructor.helpers.S3Helper;

public class CardHelper
{
    public static void addCard(Context context, Card card)
    {
        if(card.getImageId() != null && card.getLocalImagePath() == null)//download image from s3
            S3Helper.downloadCardImage(context, card);

        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.cardDao().addCard(card);
        localDatabase.close();
    }

    public static void getCard(Context context, String id)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.cardDao().get(id);
        localDatabase.close();
    }

    public static List<Card> getCardsOfCategory(Context context, String categoryId)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        List<Card> cards = localDatabase.cardDao().getCardsOfCategory(categoryId);
        localDatabase.close();
        return cards;
    }

    public static void updateCard(Context context, Card card)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.cardDao().updateCard(card);
        localDatabase.close();
    }

    public static void deleteCard(Context context, Card card)
    {
        LocalDatabase localDatabase = Room.databaseBuilder(context, LocalDatabase.class, "pecsi-local").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        localDatabase.cardDao().deleteCard(card);
        localDatabase.close();
    }
}
