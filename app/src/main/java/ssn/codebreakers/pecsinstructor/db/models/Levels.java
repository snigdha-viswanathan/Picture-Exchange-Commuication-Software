package ssn.codebreakers.pecsinstructor.db.models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Levels {
    @PrimaryKey
    @NonNull
    private int id;
    private boolean hasCompleted;
    private int noOfAttempts;

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }
    public boolean isHasCompleted() {
        return hasCompleted;
    }


   public boolean getHasCompleted(boolean hasCompleted) {
        return hasCompleted;
    }

    public void setHasCompleted(boolean hasCompleted) {
        this.hasCompleted = hasCompleted;
    }

    public int getNoOfAttempts() {
        return noOfAttempts;
    }
    public void setNoOfAttempts(int noOfAttempts){
        this.noOfAttempts = noOfAttempts;
    }
}

