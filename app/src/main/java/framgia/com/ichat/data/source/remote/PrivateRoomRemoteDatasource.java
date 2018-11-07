package framgia.com.ichat.data.source.remote;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.data.source.PrivateRoomDataSource;

public class PrivateRoomRemoteDatasource implements PrivateRoomDataSource.Remote {
    private FirebaseDatabase mDatabase;
    private static PrivateRoomRemoteDatasource sInstance;

    public static PrivateRoomRemoteDatasource getInstance(FirebaseDatabase database) {
        if (sInstance == null) {
            synchronized (PrivateRoomRemoteDatasource.class) {
                if (sInstance == null) {
                    sInstance = new PrivateRoomRemoteDatasource(database);
                }
            }
        }
        return sInstance;
    }
    private PrivateRoomRemoteDatasource(FirebaseDatabase database) {
        mDatabase = database;
    }

    @Override
    public void getPrivateRooms(ValueEventListener valueEventListener) {
        mDatabase.getReference(
                Room.PrivateRoomKey.PRIVATE_ROOM)
                .addValueEventListener(valueEventListener);
    }

    @Override
    public void createPrivateRoom(ValueEventListener valueEventListener) {

    }
}
