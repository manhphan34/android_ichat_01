package framgia.com.ichat.data.source;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ValueEventListener;

import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.data.model.User;

public interface RoomDataSource {
    interface Remote {
        void getRooms(String reference, ValueEventListener valueEventListener);

        Room initDefaultRoom(String roomName, String image);

        void createRoom(Room room, String reference,
                        OnCompleteListener onCompleteListener,
                        OnFailureListener onFailureListener);

        void deletePrivateRoom(String id,
                               OnCompleteListener onCompleteListener,
                               OnFailureListener onFailureListener);

        void renameRoom(String roomType, String id, String name,
                        OnCompleteListener onCompleteListener,
                        OnFailureListener onFailureListener);

        void addMember(String roomType, String roomId, User user,
                       OnCompleteListener onCompleteListener,
                       OnFailureListener onFailureListener);

        void exitRoom(String roomType, String userId, String name,
                      OnCompleteListener onCompleteListener,
                      OnFailureListener onFailureListener);
    }
}
