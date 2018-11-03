package framgia.com.ichat.data.source;

import com.google.firebase.database.ValueEventListener;

public interface UserDataSource {
    interface Remote {

        void getUsers(ValueEventListener valueEventListener);
    }
}
