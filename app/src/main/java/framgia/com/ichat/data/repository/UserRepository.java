package framgia.com.ichat.data.repository;

import com.google.firebase.database.ValueEventListener;

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
}
