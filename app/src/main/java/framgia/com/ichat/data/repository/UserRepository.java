package framgia.com.ichat.data.repository;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.source.UserDataSource;

public class UserRepository implements UserDataSource.Remote {
    private UserDataSource.Remote mRemote;

    public UserRepository(UserDataSource.Remote remote) {
        mRemote = remote;
    }

    @Override
    public void getUsers(ValueEventListener valueEventListener) {
        mRemote.getUsers(valueEventListener);
    }

    @Override
    public void uploadImage(FirebaseUser user, Uri file,
                            OnCompleteListener onCompleteListener,
                            OnFailureListener onFailureListener) {
        mRemote.uploadImage(user, file, onCompleteListener, onFailureListener);
    }

    @Override
    public void uploadImage(FirebaseUser user, ByteArrayOutputStream bytes, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener) {
        mRemote.uploadImage(user, bytes, onCompleteListener, onFailureListener);
    }

    @Override
    public void updateUser(User user) {
        mRemote.updateUser(user);
    }

    @Override
    public void getLinkImage(OnCompleteListener onCompleteListener) {
        mRemote.getLinkImage(onCompleteListener);
    }

    @Override
    public void getUser(FirebaseUser user, ValueEventListener eventListener) {
        mRemote.getUser(user, eventListener);
    }

    @Override
    public void updateUserName(String uid, String userName) {
        mRemote.updateUserName(uid, userName);
    }
}
