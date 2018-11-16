package framgia.com.ichat.data.source;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;

import framgia.com.ichat.data.model.User;

public interface AuthenticationDataSource {
    interface Remote {
        void createAccount(String name, String email, String password,
                           OnCompleteListener onCompleteListener,
                           OnFailureListener onFailureListener);

        FirebaseUser getCurrentUser();

        void getLoginAccountStatus(String email, String password,
                                   OnCompleteListener onCompleteListener,
                                   OnFailureListener onFailureListener);

        void signInWithGoogle(GoogleSignInAccount account,
                              OnCompleteListener onCompleteListener,
                              OnFailureListener onFailureListener) throws ApiException;

        void saveUserToDatabase(FirebaseUser firebaseUser);

        void saveUserToDatabase(FirebaseUser firebaseUser,
                                OnCompleteListener onCompleteListener,
                                OnFailureListener onFailureListener);

        User getInformationOfUser(FirebaseUser user);

        void setInformationOfUser(FirebaseUser user,
                                  OnCompleteListener onCompleteListener,
                                  OnFailureListener onFailureListener);

        void updateUser(FirebaseUser user);
    }

    interface Local {
        void saveUserInformation(String email, String password);

        void changeRememberStatus(boolean isRemember);

        boolean getRememberStatus();

        void getUserInformation(CallBack<User> callBack);
    }
}
