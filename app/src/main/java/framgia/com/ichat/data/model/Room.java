package framgia.com.ichat.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Room implements Parcelable {
    public static final String NAME = "name";
    public static final String MEMBERS = "members";
    private String mId;
    private String mName;
    private HashMap<String, Message> mMessages;
    private String mImage;
    private HashMap<String, String> mMembers;

    public Room(String name, HashMap<String, Message> messages, String image, HashMap<String, String> members) {
        mName = name;
        mMessages = messages;
        mImage = image;
        mMembers = members;
    }

    public Room() {
    }

    protected Room(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mImage = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mImage);
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

    public HashMap<String, String> getMembers() {
        return mMembers;
    }

    public void setMembers(HashMap<String, String> members) {
        mMembers = members;
    }

    public class PrivateRoomKey {
        public static final String PRIVATE_ROOM = "PRIVATE_ROOM";
        public static final String NAME_DEFAULT = "New private room";
        public static final String IMAGE_DEFAULT =
                "https://cdn2-www.dogtime.com/assets/uploads/2017/09/pit-bull-puppies-3.jpg";
    }

    public class PublicRoomKey {
        public static final String PUBLIC_ROOM = "PUBLIC_ROOM";
        public static final String NAME_DEFAULT = "New public room";
        public static final String IMAGE_DEFAULT =
                "http://thepublicvoice.org/wordpress/wp-content/uploads/2016/01/TPV-people-hp.jpg";
    }
}
