package framgia.com.ichat.data.source.remote;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.source.AuthenticationDataSource;
import framgia.com.ichat.utils.Constant;
import framgia.com.ichat.utils.DateTime;

public class AuthenticationRemoteDataSource implements AuthenticationDataSource.Remote {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String mName;

    private static AuthenticationRemoteDataSource sInstance;

    public static AuthenticationRemoteDataSource getInstance(FirebaseAuth auth, FirebaseDatabase database) {
        if (sInstance == null) {
            synchronized (AuthenticationRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new AuthenticationRemoteDataSource(auth, database);
                }
            }
        }
        return sInstance;
    }

    public AuthenticationRemoteDataSource(FirebaseAuth auth, FirebaseDatabase firebaseDatabase) {
        mAuth = auth;
        mDatabase = firebaseDatabase;
    }

    public void createAccount(String name, String email, String password,
                              OnCompleteListener onCompleteListener,
                              OnFailureListener onFailureListener) {
        mName = name;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    @Override
    public void getLoginAccountStatus(String email, String password,
                                      OnCompleteListener onCompleteListener,
                                      OnFailureListener onFailureListener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account,
                                 OnCompleteListener onCompleteListener,
                                 OnFailureListener onFailureListener) {
        if (account != null) {
            authenticateGoogleWithFireBase(account, onCompleteListener, onFailureListener);
        }
    }

    @Override
    public void saveUserToDatabase(FirebaseUser firebaseUser) {
        mDatabase.getReference(User.UserKey.USER_REFERENCE)
                .child(firebaseUser.getUid())
                .setValue(getInformationOfUser(firebaseUser));
    }

    @Override
    public void saveUserToDatabase(FirebaseUser firebaseUser,
                                   OnCompleteListener onCompleteListener,
                                   OnFailureListener onFailureListener) {
        mDatabase.getReference(User.UserKey.USER_REFERENCE)
                .child(firebaseUser.getUid())
                .setValue(initUser(firebaseUser))
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public User getInformationOfUser(FirebaseUser firebaseUser) {
        return new User(firebaseUser, true);
    }

    @Override
    public void setInformationOfUser(FirebaseUser user,
                                     OnCompleteListener onCompleteListener,
                                     OnFailureListener onFailureListener) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .setPhotoUri(Uri.parse(Constant.UserProfile.DEFAULT_PROFILE_URL))
                .build();
        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void updateUser(FirebaseUser user) {
        DatabaseReference reference = mDatabase.getReference(User.UserKey.USER_REFERENCE);
        reference.child(user.getUid()).child(User.UserKey.ONLINE).setValue(true);
        reference.child(user.getUid()).child(User.UserKey.LAST_SING_IN).setValue(Calendar.getInstance().getTimeInMillis());
    }

    private void authenticateGoogleWithFireBase(GoogleSignInAccount account,
                                                OnCompleteListener onCompleteListener,
                                                OnFailureListener onFailureListener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    private User initUser(FirebaseUser user) {
        return new User(
                user.getUid(),
                user.getEmail(),
                user.getDisplayName(),
                user.getPhotoUrl().toString(),
                DateTime.getCurrentTime(),
                true
        );
    }
}
