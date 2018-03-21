package ssn.codebreakers.pecsinstructor.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ssn.codebreakers.pecsinstructor.helpers.APIHelper;
import ssn.codebreakers.pecsinstructor.helpers.Callback;

public class PECSFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "PECSInstructorFirebase";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    //TODO : sendRegistrationToServer each time app starts
    private void sendRegistrationToServer(String token) {
        APIHelper.updateFCMToken(getApplicationContext(), new Callback() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}