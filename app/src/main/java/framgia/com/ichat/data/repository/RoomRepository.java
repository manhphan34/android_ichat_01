package framgia.com.ichat.data.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ValueEventListener;

import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.source.RoomDataSource;

public class RoomRepository implements RoomDataSource.Remote {
    private RoomDataSource.Remote mRemote;
    private static RoomRepository sInstance;

    public static RoomRepository getInstance(RoomDataSource.Remote remote) {
        if (sInstance == null) {
            synchronized (RoomRepository.class) {
                if (sInstance == null) {
                    sInstance = new RoomRepository(remote);
                }
            }
        }
        return sInstance;
    }

    private RoomRepository(RoomDataSource.Remote remote) {
        mRemote = remote;
    }

    @Override
    public void getRooms(String reference, ValueEventListener valueEventListener) {
        mRemote.getRooms(reference, valueEventListener);
    }

    @Override
    public Room initDefaultRoom(String roomName, String image) {
        return mRemote.initDefaultRoom(roomName, image);
    }

    @Override
    public void createRoom(Room room,
                           String reference,
                           OnCompleteListener onCompleteListener,
                           OnFailureListener onFailureListener) {
        mRemote.createRoom(room, reference, onCompleteListener, onFailureListener);
    }

    @Override
    public void deletePrivateRoom(String id,
                                  OnCompleteListener onCompleteListener,
                                  OnFailureListener onFailureListener) {
        mRemote.deletePrivateRoom(id, onCompleteListener, onFailureListener);
    }

    @Override
    public void renameRoom(String roomType, String id, String name,
                           OnCompleteListener onCompleteListener,
                           OnFailureListener onFailureListener) {
        mRemote.renameRoom(roomType, id, name, onCompleteListener, onFailureListener);
    }

    @Override
    public void addMember(String roomType, String roomId, User user,
                          OnCompleteListener onCompleteListener,
                          OnFailureListener onFailureListener) {
        mRemote.addMember(roomType, roomId, user, onCompleteListener, onFailureListener);
    }

    @Override
    public void exitRoom(String roomType, String roomId, String userId,
                         OnCompleteListener onCompleteListener,
                         OnFailureListener onFailureListener) {
        mRemote.exitRoom(roomType, roomId, userId, onCompleteListener, onFailureListener);
    }
}
