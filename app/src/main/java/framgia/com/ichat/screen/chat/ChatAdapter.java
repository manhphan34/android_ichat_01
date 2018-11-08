package framgia.com.ichat.screen.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import framgia.com.ichat.ApplicationGlideModule;
import framgia.com.ichat.GlideApp;
import framgia.com.ichat.R;
import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.Room;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int LOCATION = 4;
    public static final String SPACE = " ";
    private static final String PATTERN = "HH:mm:ss";
    private static final int SENT = 0;
    private static final int RECEIVE = 1;
    private Context mContext;
    private List<Message> mListMessage;
    private LayoutInflater mInflater;
    private String mId;

    public ChatAdapter(Context context, List<Message> messages, String id) {
        mContext = context;
        mListMessage = messages;
        mInflater = LayoutInflater.from(mContext);
        mId = id;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListMessage.get(position).getSenderId().equalsIgnoreCase(mId)) {
            return SENT;
        }
        return RECEIVE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case SENT:
                return new ViewHolderMessageSent(mInflater.inflate(R.layout.item_message_sent, viewGroup, false));
            case RECEIVE:
                return new ViewHolderMessageReceive(mInflater.inflate(R.layout.item_message_receive, viewGroup, false));
            default:
                return new ViewHolderMessageReceive(mInflater.inflate(R.layout.item_message_receive, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {
            case SENT:
                ViewHolderMessageSent sent = (ViewHolderMessageSent) viewHolder;
                sent.bindView(mContext, mListMessage.get(i));
                break;
            case RECEIVE:
                ViewHolderMessageReceive receive = (ViewHolderMessageReceive) viewHolder;
                receive.bindView(mContext, mListMessage.get(i));
                break;
            default:
//                ViewHolderMessageSent sent = (ViewHolderMessageSent) viewHolder;
//                sent.bindView(mContext,mListMessage.get(i));
//                break;
        }
    }

    @Override
    public int getItemCount() {
        return mListMessage != null ? mListMessage.size() : 0;
    }

    public void addData(List<Message> listMessage) {
        mListMessage.clear();
        mListMessage.addAll(listMessage);
        notifyDataSetChanged();
    }

    public static String getTime(String time) {
        String[] times = time.split(SPACE);
        return times[LOCATION];
    }

    public static class ViewHolderMessageSent extends RecyclerView.ViewHolder {
        private TextView mTextMessageBody;
        private TextView mTextMessageTime;

        public ViewHolderMessageSent(@NonNull View itemView) {
            super(itemView);
            mTextMessageBody = itemView.findViewById(R.id.text_message_body);
            mTextMessageTime = itemView.findViewById(R.id.text_message_time);
        }

        public void bindView(Context context, Message message) {
            mTextMessageBody.setText(message.getContent());
            mTextMessageTime.setText(ChatAdapter.getTime(message.getCreated()));
        }
    }

    public static class ViewHolderMessageReceive extends RecyclerView.ViewHolder {
        private ImageView mImageMessageProfile;
        private TextView mTextMessageName;
        private TextView mTextMessageBody;
        private TextView mTextMessageTime;

        public ViewHolderMessageReceive(@NonNull View itemView) {
            super(itemView);
            mImageMessageProfile = itemView.findViewById(R.id.image_message_profile);
            mTextMessageName = itemView.findViewById(R.id.text_message_name);
            mTextMessageBody = itemView.findViewById(R.id.text_message_body);
            mTextMessageTime = itemView.findViewById(R.id.text_message_time);
        }

        public void bindView(Context context, Message message) {
            GlideApp.with(context)
                    .load(message.getSenderImage())
                    .override(ApplicationGlideModule.WIDTH,
                            ApplicationGlideModule.HEIGHT)
                    .circleCrop()
                    .into(mImageMessageProfile);
            mTextMessageName.setText(message.getSenderName());
            mTextMessageBody.setText(message.getContent());
            mTextMessageTime.setText(ChatAdapter.getTime(message.getCreated()));
        }
    }
}
