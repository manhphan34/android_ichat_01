package framgia.com.ichat.data.source.remote;

import com.google.firebase.database.FirebaseDatabase;

import framgia.com.ichat.data.source.PublicRoomDataSource;

public class PublicRoomRemoteDataSource implements PublicRoomDataSource.Remote {
    private FirebaseDatabase mDatabase;
    private static PublicRoomRemoteDataSource sInstance;

    public static PublicRoomRemoteDataSource getInstance(FirebaseDatabase database) {
        if (sInstance == null) {
            synchronized (PublicRoomRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new PublicRoomRemoteDataSource(database);
                }
            }
        }
        return sInstance;
    }

    private PublicRoomRemoteDataSource(FirebaseDatabase database) {
        mDatabase = database;
    }

    @Override
    public void getPublicRoom() {

    }
}
