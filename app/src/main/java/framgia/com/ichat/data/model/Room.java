package framgia.com.ichat.data.model;

import java.util.HashMap;

public class Room {
    private String mId;
    private String mName;
    private HashMap<String, Message> mMessages;
    private String mImage;

    public Room(String id, String name, HashMap<String, Message> messages) {
        mId = id;
        mName = name;
        mMessages = messages;
    }

    public Room() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public HashMap<String, Message> getMessages() {
        return mMessages;
    }

    public void setMessages(HashMap<String, Message> messages) {
        mMessages = messages;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public class PrivateRoomKey {
        public static final String PRIVATE_ROOM = "private_room";
    }
}
