package framgia.com.ichat.data.repository;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;

import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.source.AuthenticationDataSource;
import framgia.com.ichat.data.source.CallBack;

public class AuthenticationRepository implements AuthenticationDataSource.Remote, AuthenticationDataSource.Local {
    private AuthenticationDataSource.Remote mRemote;
    private AuthenticationDataSource.Local mLocal;

    private static AuthenticationRepository sInstance;

    public static AuthenticationRepository getInstance(AuthenticationDataSource.Remote remote) {
        if (sInstance == null) {
            synchronized (AuthenticationRepository.class) {
                if (sInstance == null) {
                    sInstance = new AuthenticationRepository(remote);
                }
            }
        }
        return sInstance;
    }

    public AuthenticationRepository(AuthenticationDataSource.Remote remote) {
        mRemote = remote;
    }

    public AuthenticationRepository(AuthenticationDataSource.Remote remote, AuthenticationDataSource.Local local) {
        mRemote = remote;
        mLocal = local;
    }

    @Override
    public void createAccount(String name, String email, String password,
                              OnCompleteListener onCompleteListener,
                              OnFailureListener onFailureListener) {
        mRemote.createAccount(name, email, password, onCompleteListener, onFailureListener);
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mRemote.getCurrentUser();
    }

    @Override
    public void getLoginAccountStatus(String email, String password,
                                      OnCompleteListener onCompleteListener,
                                      OnFailureListener onFailureListener) {
        mRemote.getLoginAccountStatus(email, password, onCompleteListener, onFailureListener);
    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account,
                                 OnCompleteListener onCompleteListener,
                                 OnFailureListener onFailureListener) throws ApiException {
        mRemote.signInWithGoogle(account, onCompleteListener, onFailureListener);
    }

    @Override
    public void saveUserToDatabase(FirebaseUser firebaseUser) {
        mRemote.saveUserToDatabase(firebaseUser);
    }

    @Override
    public void saveUserToDatabase(FirebaseUser firebaseUser,
                                   OnCompleteListener onCompleteListener,
                                   OnFailureListener onFailureListener) {
        mRemote.saveUserToDatabase(firebaseUser, onCompleteListener, onFailureListener);
    }

    @Override
    public User getInformationOfUser(FirebaseUser user) {
        return mRemote.getInformationOfUser(user);
    }

    @Override
    public void setInformationOfUser(FirebaseUser user,
                                     OnCompleteListener onCompleteListener,
                                     OnFailureListener onFailureListener) {
        mRemote.setInformationOfUser(user, onCompleteListener, onFailureListener);
    }

    public void updateUser(FirebaseUser user) {
        mRemote.updateUser(user);
    }

    @Override
    public void saveUserInformation(String email, String password) {
        mLocal.saveUserInformation(email, password);
    }

    @Override
    public void changeRememberStatus(boolean isRemember) {
        mLocal.changeRememberStatus(isRemember);
    }

    @Override
    public boolean getRememberStatus() {
        return mLocal.getRememberStatus();
    }

    @Override
    public void getUserInformation(CallBack<User> callBack) {
        mLocal.getUserInformation(callBack);
    }
}
