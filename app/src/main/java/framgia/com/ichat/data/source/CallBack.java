package framgia.com.ichat.data.source;

public interface CallBack<T> {
    void onLoginSuccess(T data);

    void onLoginFailed(Exception e);
}
