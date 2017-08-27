package mock.weatherfeed;

public interface ResponseCallback {
    void onSuccess(String response,int what);
    void onFailure(String errorMessage);
}
