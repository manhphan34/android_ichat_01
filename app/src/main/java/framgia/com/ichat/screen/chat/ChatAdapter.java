package framgia.com.ichat.screen.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import framgia.com.ichat.ApplicationGlideModule;
import framgia.com.ichat.GlideApp;
import framgia.com.ichat.R;
import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.utils.DateTime;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SENT = 0;
    private static final int RECEIVE = 1;
    private Context mContext;
    private List<Message> mMessages;
    private LayoutInflater mInflater;
    private String mId;
    private OnMessageItemClickListener mOnClickListener;

    public ChatAdapter(Context context, String id,
                       OnMessageItemClickListener onClickListener) {
        mContext = context;
        mMessages = new ArrayList<>();
        mInflater = LayoutInflater.from(mContext);
        mId = id;
        mOnClickListener = onClickListener;
    }

    public void addData(Message message) {
        mMessages.add(message);
        notifyItemInserted(mMessages.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).getSenderId().equalsIgnoreCase(mId)) {
            return SENT;
        }
        return RECEIVE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case SENT:
                return new MessageSentViewHolder(mInflater.inflate(R.layout.item_message_sent,
                        viewGroup, false));
            default:
                return new MessageReceiveViewHolder(mInflater.inflate(R.layout.item_message_receive,
                        viewGroup, false), mOnClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {
            case SENT:
                MessageSentViewHolder sent = (MessageSentViewHolder) viewHolder;
                sent.bindView(mContext, mMessages.get(i));
                break;
            case RECEIVE:
                MessageReceiveViewHolder receive = (MessageReceiveViewHolder) viewHolder;
                receive.bindView(mContext, mMessages.get(i));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public static class MessageSentViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextMessageBody;
        private TextView mTextMessageTime;
        private ImageView mImageEmoji;

        public MessageSentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextMessageBody = itemView.findViewById(R.id.text_message_body);
            mTextMessageTime = itemView.findViewById(R.id.text_message_time);
            mImageEmoji = itemView.findViewById(R.id.image_view_emoji_send);
        }

        public void bindView(Context context, Message message) {
            showMessageContent(message);
            mTextMessageTime.setText(DateTime.getTime(message.getCreated()));
            showEmoji(context, message);
        }

        private void showMessageContent(Message message) {
            if (TextUtils.isEmpty(message.getContent())) {
                mTextMessageBody.setVisibility(View.INVISIBLE);
            } else {
                mTextMessageBody.setVisibility(View.VISIBLE);
                mTextMessageBody.setText(message.getContent());
            }
        }

        private void showEmoji(Context context, Message message) {
            if (message.getMessageEmoji() == null) {
                mImageEmoji.setVisibility(View.INVISIBLE);
            } else {
                mImageEmoji.setVisibility(View.VISIBLE);
                ApplicationGlideModule.loadImage(context, message.getMessageEmoji(), mImageEmoji);
            }
        }
    }

    public static class MessageReceiveViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView mImageMessageProfile;
        private TextView mTextMessageName;
        private TextView mTextMessageBody;
        private TextView mTextMessageTime;
        private ImageView mImageEmoji;
        private OnMessageItemClickListener mOnClickListener;
        private Message mMessage;

        public MessageReceiveViewHolder(@NonNull View itemView,
                                        OnMessageItemClickListener onClickListener) {
            super(itemView);
            mImageMessageProfile = itemView.findViewById(R.id.image_message_profile);
            mTextMessageName = itemView.findViewById(R.id.text_message_name);
            mTextMessageBody = itemView.findViewById(R.id.text_message_body);
            mTextMessageTime = itemView.findViewById(R.id.text_message_time);
            mImageEmoji = itemView.findViewById(R.id.image_view_emoji_receive);
            mOnClickListener = onClickListener;
            mImageMessageProfile.setOnClickListener(this);
        }

        public void bindView(Context context, Message message) {
            mMessage = message;
            showMessageContent(message);
            mTextMessageBody.setText(message.getContent());
            mTextMessageTime.setText(DateTime.getTime(message.getCreated()));
            ApplicationGlideModule.loadCircleImage(context, message.getSenderImage(),
                    mImageMessageProfile);
            showEmoji(context, message);
        }

        private void showMessageContent(Message message) {
            if (TextUtils.isEmpty(message.getContent())) {
                mTextMessageBody.setVisibility(View.INVISIBLE);
            } else {
                mTextMessageBody.setVisibility(View.VISIBLE);
                mTextMessageBody.setText(message.getContent());
            }
        }

        private void showEmoji(Context context, Message message) {
            if (message.getMessageEmoji() == null) {
                mImageEmoji.setVisibility(View.INVISIBLE);
            } else {
                mImageEmoji.setVisibility(View.VISIBLE);
                ApplicationGlideModule.loadImage(context, message.getMessageEmoji(), mImageEmoji);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onClickMessageItem(mMessage.getSenderId());
            }
        }
    }

    public interface OnMessageItemClickListener {
        void onClickMessageItem(String id);
    }
}
