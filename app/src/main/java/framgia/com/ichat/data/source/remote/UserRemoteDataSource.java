package framgia.com.ichat.data.source.remote;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.source.UserDataSource;

public class UserRemoteDataSource implements UserDataSource.Remote {

    private FirebaseDatabase mDatabase;

    public UserRemoteDataSource(FirebaseDatabase database) {
        mDatabase = database;
    }

    @Override
    public void getUsers(ValueEventListener valueEventListener) {
        mDatabase.getReference(User.UserKey.USER_REFERENCE).addValueEventListener(valueEventListener);
    }

}
