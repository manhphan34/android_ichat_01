package framgia.com.ichat.data.source.remote;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.source.RoomDataSource;

public class RoomRemoteDataSource implements RoomDataSource.Remote {
    private static final String PATTERN = "EEE, d MMM yyyy, HH:mm";
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private static RoomRemoteDataSource sInstance;

    public static RoomRemoteDataSource getInstance(FirebaseDatabase database, FirebaseAuth auth) {
        if (sInstance == null) {
            synchronized (RoomRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new RoomRemoteDataSource(database, auth);
                }
            }
        }
        return sInstance;
    }

    private RoomRemoteDataSource(FirebaseDatabase database, FirebaseAuth auth) {
        mDatabase = database;
        mAuth = auth;
    }

    @Override
    public void getRooms(String reference, ValueEventListener valueEventListener) {
        mDatabase.getReference(reference)
                .addValueEventListener(valueEventListener);
    }

    @Override
    public void createRoom(Room room,
                           String reference,
                           OnCompleteListener onCompleteListener,
                           OnFailureListener onFailureListener) {
        mDatabase.getReference(reference).push().setValue(room);
    }

    @Override
    public void deletePrivateRoom(String id,
                                  OnCompleteListener onCompleteListener,
                                  OnFailureListener onFailureListener) {
        mDatabase.getReference(Room.PrivateRoomKey.PRIVATE_ROOM)
                .child(id)
                .removeValue()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void renameRoom(String roomType, String id, String name,
                           OnCompleteListener onCompleteListener,
                           OnFailureListener onFailureListener) {
        mDatabase.getReference(roomType)
                .child(id)
                .child(Room.NAME)
                .setValue(name)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void addMember(String roomType, String roomId, User user,
                          OnCompleteListener onCompleteListener,
                          OnFailureListener onFailureListener) {
        mDatabase.getReference(roomType)
                .child(roomId)
                .child(Room.MEMBERS)
                .child(user.getUid())
                .setValue(user.getDisplayName())
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public void exitRoom(String roomType, String roomId, String userId,
                         OnCompleteListener onCompleteListener,
                         OnFailureListener onFailureListener) {
        mDatabase.getReference(roomType)
                .child(roomId)
                .child(Room.MEMBERS)
                .child(userId)
                .removeValue()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public String getCurrentTime() {
        return new SimpleDateFormat(PATTERN).format(Calendar.getInstance().getTime());
    }

    @Override
    public Room initDefaultRoom(String roomName, String image) {
        Room room = new Room();
        HashMap<String, Message> messages = new HashMap<>();
        HashMap<String, String> members = new HashMap<>();
        Message message = new Message(
                Message.MessageKey.CONTENT_DEFAULT,
                getCurrentTime(),
                Message.MessageKey.SENDER_ID_DEFAULT,
                Message.MessageKey.SENDER_NAME_DEFAULT,
                Message.MessageKey.SENDER_IMAGE_DEFAULT);
        messages.put(Message.MessageKey.ID_DEFAULT, message);
        members.put(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getDisplayName());
        room.setName(roomName);
        room.setImage(image);
        room.setMessages(messages);
        room.setMembers(members);
        return room;
    }
}
