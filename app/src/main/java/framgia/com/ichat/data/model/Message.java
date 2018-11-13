package framgia.com.ichat.data.model;

public class Message {
    private String mId;
    private String mContent;
    private String mCreated;
    private String mSenderId;
    private String mSenderName;
    private String mSenderImage;

    public Message(String content, String created, String senderId, String senderName, String senderImage) {
        mContent = content;
        mCreated = created;
        mSenderId = senderId;
        mSenderName = senderName;
        mSenderImage = senderImage;
    }

    public Message() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getCreated() {
        return mCreated;
    }

    public void setCreated(String created) {
        mCreated = created;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public void setSenderName(String senderName) {
        mSenderName = senderName;
    }

    public String getSenderImage() {
        return mSenderImage;
    }

    public void setSenderImage(String senderImage) {
        mSenderImage = senderImage;
    }
    public class MessageKey{
        public static final String MESSAGES = "messages";
        public static final String CONTENT_DEFAULT = "Invite people join chat with you!";
        public static final String SENDER_IMAGE_DEFAULT = "https://www.mobafire.com/images/avatars/ashe-championship.png";
        public static final String SENDER_NAME_DEFAULT = "IChat";
        public static final String SENDER_ID_DEFAULT = "-LR5D9qxCJIo_kB13TNS";
        public static final String ID_DEFAULT = "-LR4XSCvfyWEKYA8JiqF";
    }
}
