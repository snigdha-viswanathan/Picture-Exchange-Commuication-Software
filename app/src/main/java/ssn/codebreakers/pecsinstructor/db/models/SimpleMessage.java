package ssn.codebreakers.pecsinstructor.db.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.ArrayListConverter;

@Entity
public class SimpleMessage
{
    @PrimaryKey
    @NonNull
    private String id;
    private String text;
    @TypeConverters({ArrayListConverter.class})
    private List<String> cardIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<String> cardIds) {
        this.cardIds = cardIds;
    }
}
