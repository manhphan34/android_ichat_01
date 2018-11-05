package framgia.com.ichat.data.source.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;

import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.source.UserDataSource;

public class UserRemoteDataSource implements UserDataSource.Remote {
    private static final String PNG = ".png";
    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;

    public UserRemoteDataSource(FirebaseDatabase database) {
        mDatabase = database;
    }

    public UserRemoteDataSource(FirebaseDatabase database, FirebaseStorage storage, FirebaseAuth auth) {
        mDatabase = database;
        mStorage = storage;
        mAuth = auth;
    }

    @Override
    public void getUsers(ValueEventListener valueEventListener) {
        mDatabase.getReference(User.UserKey.USER_REFERENCE).addValueEventListener(valueEventListener);
    }

    @Override
    public void uploadImage(FirebaseUser user, Uri file,
                            OnCompleteListener onCompleteListener,
                            OnFailureListener onFailureListener) {
        StorageReference storageReference = mStorage.getReference();
        storageReference.child(user.getUid().concat(PNG))
                .putFile(file)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void uploadImage(FirebaseUser user, ByteArrayOutputStream bytes,
                            OnCompleteListener onCompleteListener,
                            OnFailureListener onFailureListener) {
        StorageReference storageReference = mStorage.getReference();
        storageReference.child(user.getUid().concat(PNG))
                .putBytes(bytes.toByteArray())
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void updateUser(User user) {
        setUserInfo(user);
    }

    @Override
    public void getLinkImage(OnCompleteListener onCompleteListener) {
        FirebaseUser user = mAuth.getCurrentUser();
        StorageReference storageReference = mStorage.getReference(user.getUid().concat(PNG));
        storageReference.getDownloadUrl().addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void getUser(FirebaseUser user, ValueEventListener eventListener) {
        DatabaseReference reference = mDatabase.getReference(User.UserKey.USER_REFERENCE).child(user.getUid());
        reference.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void updateUserName(String uid, String userName) {
        DatabaseReference reference = mDatabase.getReference(User.UserKey.USER_REFERENCE).child(uid);
        reference.child(User.UserKey.DISPLAY_NAME).setValue(userName);
    }

    private void setUserInfo(User user) {
        mDatabase.getReference(User.UserKey.USER_REFERENCE).child(user.getUid()).setValue(user);
    }
}
