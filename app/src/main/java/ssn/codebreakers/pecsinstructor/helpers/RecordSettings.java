package ssn.codebreakers.pecsinstructor.helpers;

import android.app.Activity;
import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.util.DisplayMetrics;

public class RecordSettings
{
    private int bitRate;
    private int frameRate;
    private boolean recordAudio;
    private int screenWidth;
    private int screenHeight;
    private int videoResolution;

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public boolean isRecordAudio() {
        return recordAudio;
    }

    public void setRecordAudio(boolean recordAudio) {
        this.recordAudio = recordAudio;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(int videoResolution) {
        this.videoResolution = videoResolution;
    }

    public static RecordSettings GetDefault(Activity activity)
    {
        RecordSettings recordSettings = new RecordSettings();
        recordSettings.setBitRate(8000000);
        recordSettings.setFrameRate(24);
        recordSettings.setRecordAudio(true);
        recordSettings.setVideoResolution(1080);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        recordSettings.setScreenWidth(displayMetrics.widthPixels);
        recordSettings.setScreenHeight(displayMetrics.heightPixels);
        return recordSettings;
    }
}
