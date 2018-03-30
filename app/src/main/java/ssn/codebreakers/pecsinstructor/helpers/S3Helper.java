package ssn.codebreakers.pecsinstructor.helpers;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.util.UUID;

import ssn.codebreakers.pecsinstructor.db.models.Card;


public class S3Helper
{
    private static final String IDENTITY_POOL_ID = "us-east-1:f372529b-fbe4-424f-88b9-fca3f11bd0c3";
    private static final Regions REGION = Regions.US_EAST_1;
    private static final String S3_BUCKET = "pecs-user-uploads";

    public static void downloadCardImage(Context context, final Card card)
    {
        System.out.println("downloading file");
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID, REGION);
        final AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(REGION));

        File folder = CommonUtils.getAppFolder();
        File downloadFile = new File(folder, "image"+CommonUtils.getUniqueRandomID()+".jpg");
        card.setLocalImagePath(downloadFile.getAbsolutePath());
        card.setId(UUID.randomUUID().toString());
        TransferUtility transferUtility = TransferUtility.builder()
                                            .context(context)
                                            .s3Client(s3)
                                            .defaultBucket(S3_BUCKET)
                                            .build();

        TransferObserver downloadObserver = transferUtility.download(card.getImageId(), downloadFile);

        downloadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    System.out.println("download "+card.getImageId()+" complete to "+card.getLocalImagePath());
                }
            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;
                System.out.println("   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }
            @Override
            public void onError(int id, Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
