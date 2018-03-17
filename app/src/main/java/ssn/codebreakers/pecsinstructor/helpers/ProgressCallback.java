package ssn.codebreakers.pecsinstructor.helpers;


public interface ProgressCallback
{
    void onSuccess(Object result);
    void onProgress(int progress);
    void onError(Object error);
}
