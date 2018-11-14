package framgia.com.ichat.data.source.remote;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.source.ChatDataSource;

public class ChatRemoteDataSource implements ChatDataSource.Remote {
    private FirebaseDatabase mDatabase;
    private static ChatRemoteDataSource sInstance;

    public static ChatRemoteDataSource getInstance(FirebaseDatabase database) {
        if (sInstance == null) {
            synchronized (ChatRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new ChatRemoteDataSource(database);
                }
            }
        }
        return sInstance;
    }

    public ChatRemoteDataSource(FirebaseDatabase database) {
        mDatabase = database;
    }

    @Override
    public void getMessages(String id, String roomType, ValueEventListener valueEventListener) {
        mDatabase.getReference(roomType)
                .child(id).child(Message.MessageKey.MESSAGES)
                .addValueEventListener(valueEventListener);
    }

    @Override
    public void sendMessage(String roomId, String roomType, Message message) {
        String id = mDatabase.getReference(roomType).push().getKey();
        DatabaseReference reference = mDatabase.getReference(roomType);
        reference.child(roomId)
                .child(Message.MessageKey.MESSAGES)
                .child(id)
                .setValue(message);
    }

    @Override
    public void addOnChildChange(String roomId, String roomType,
                                 ChildEventListener childEventListener) {
        DatabaseReference reference = mDatabase.getReference(roomType);
        reference.child(roomId)
                .child(Message.MessageKey.MESSAGES)
                .addChildEventListener(childEventListener);
    }

    @Override
    public void getEmojis(ValueEventListener valueEventListener) {
        DatabaseReference reference = mDatabase.getReference(Message.MessageKey.EMOJI);
        reference.child(Message.MessageKey.PIKATRUMP)
                .addValueEventListener(valueEventListener);
    }
}
