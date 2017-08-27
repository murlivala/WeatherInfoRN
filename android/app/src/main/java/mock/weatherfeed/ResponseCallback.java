package mock.weatherfeed;

public interface ResponseCallback {
    void onSuccess(String response,int what);
    //void onUpdate(int state, int index);
    void onFailure(String errorMessage);
}
