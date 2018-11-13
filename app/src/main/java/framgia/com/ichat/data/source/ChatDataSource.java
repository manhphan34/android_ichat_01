package framgia.com.ichat.data.source;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import framgia.com.ichat.data.model.Message;

public interface ChatDataSource {
    interface Remote {
        void getMessages(String id, String roomType,
                         ValueEventListener valueEventListener);

        void sendMessage(String roomId, String roomType, Message message);

        void addOnChildChange(String roomId, String roomType,
                              ChildEventListener childEventListener);

        void getEmojis(ValueEventListener valueEventListener);
    }
}
