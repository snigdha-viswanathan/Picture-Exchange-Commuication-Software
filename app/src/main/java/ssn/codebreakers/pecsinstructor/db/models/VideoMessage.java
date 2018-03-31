package ssn.codebreakers.pecsinstructor.db.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.List;

import ssn.codebreakers.pecsinstructor.db.ArrayListConverter;

@Entity
public class VideoMessage
{
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String videoId;
    private String localVideoPath;
    private String successVideoId;
    private String localSuccessVideoPath;
    private String errorVideoId;
    private String localErrorVideoPath;
    @TypeConverters({ArrayListConverter.class})
    private List<String> cardIds;
    private String correctCardId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getLocalVideoPath() {
        return localVideoPath;
    }

    public void setLocalVideoPath(String localVideoPath) {
        this.localVideoPath = localVideoPath;
    }

    public String getSuccessVideoId() {
        return successVideoId;
    }

    public void setSuccessVideoId(String successVideoId) {
        this.successVideoId = successVideoId;
    }

    public String getLocalSuccessVideoPath() {
        return localSuccessVideoPath;
    }

    public void setLocalSuccessVideoPath(String localSuccessVideoPath) {
        this.localSuccessVideoPath = localSuccessVideoPath;
    }

    public String getErrorVideoId() {
        return errorVideoId;
    }

    public void setErrorVideoId(String errorVideoId) {
        this.errorVideoId = errorVideoId;
    }

    public String getLocalErrorVideoPath() {
        return localErrorVideoPath;
    }

    public void setLocalErrorVideoPath(String localErrorVideoPath) {
        this.localErrorVideoPath = localErrorVideoPath;
    }

    public List<String> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<String> cardIds) {
        this.cardIds = cardIds;
    }

    public String getCorrectCardId() {
        return correctCardId;
    }

    public void setCorrectCardId(String correctCardId) {
        this.correctCardId = correctCardId;
    }
}
