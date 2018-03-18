package ssn.codebreakers.pecsinstructor.db.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

import ssn.codebreakers.pecsinstructor.db.TimestampConverter;

@Entity
public class Message
{
    @PrimaryKey
    @NonNull
    private String id;
    private String fromUserId;
    @TypeConverters({TimestampConverter.class})
    private Date messageTime;
    private int messageType;//type of the message. Simple Message or Video Message
    private String messageObjectId;// id of the Simple Message or Video Message object
    private boolean seen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageObjectId() {
        return messageObjectId;
    }

    public void setMessageObjectId(String messageObjectId) {
        this.messageObjectId = messageObjectId;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
