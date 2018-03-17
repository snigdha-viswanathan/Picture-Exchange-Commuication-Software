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
import java.util.Date;

public class FileUploader
{
    Context context;
    private static final String IDENTITY_POOL_ID = "us-east-1:f372529b-fbe4-424f-88b9-fca3f11bd0c3";
    private static final Regions REGION = Regions.US_EAST_1;
    private static final String S3_BUCKET = "pecs-user-uploads";

    public FileUploader(Context context)
    {
        this.context = context;
    }

    /**
     *
     * @param file File that needs to be uploaded
     * @param uploadedFileName Name of the uploaded file
     * @param progressCallback Returns temporary URL on succes, progress in percent while uploading, and error reason when failed
     */
    public void uploadFile(File file, final String uploadedFileName, final ProgressCallback progressCallback)
    {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID, REGION);
        final AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(REGION));
        TransferUtility transferUtility = TransferUtility.builder()
                                            .context(context)
                                            .s3Client(s3)
                                            .defaultBucket(S3_BUCKET)
                                            .build();
        TransferObserver uploadObserver = transferUtility.upload(uploadedFileName, file);
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                System.out.println("s3 status changed "+state);
                if (TransferState.COMPLETED == state) {
                    progressCallback.onSuccess(s3.generatePresignedUrl(S3_BUCKET, uploadedFileName, new Date(new Date().getTime() + 1000 * 60 * 60)).toString());
                }
            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentDone = (int)((float) bytesCurrent / (float) bytesTotal) * 100;
                progressCallback.onProgress(percentDone);
            }
            @Override
            public void onError(int id, Exception ex) {
                progressCallback.onError(ex.getMessage());
            }
        });

    }
}
