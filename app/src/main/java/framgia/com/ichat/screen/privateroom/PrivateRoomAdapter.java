package framgia.com.ichat.screen.privateroom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import framgia.com.ichat.GlideApp;
import framgia.com.ichat.R;
import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.Room;

public class PrivateRoomAdapter extends RecyclerView.Adapter<PrivateRoomAdapter.ViewHolder> {
    private Context mContext;
    private List<Room> mRooms;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public PrivateRoomAdapter(Context context, List<Room> rooms) {
        mContext = context;
        mRooms = rooms;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_private_room, viewGroup,
                false), mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindView(mContext, mRooms.get(i));
    }

    @Override
    public int getItemCount() {
        return mRooms != null ? mRooms.size() : 0;
    }

    public void addData(List<Room> rooms) {
        if (rooms == null) {
            return;
        }
        mRooms.clear();
        mRooms.addAll(rooms);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener {
        public ImageView mImageAvatar;
        public TextView mTextName;
        public TextView mTextLastMessageOfUser;
        public OnItemClickListener mOnItemClickListener;
        public Room mRoom;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            mImageAvatar = itemView.findViewById(R.id.image_avatar);
            mTextName = itemView.findViewById(R.id.text_name);
            mTextLastMessageOfUser = itemView.findViewById(R.id.text_last_message_of_user);
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindView(Context context, Room room) {
            mRoom = room;
            mTextName.setText(room.getName());
            GlideApp.with(context)
                    .load(room.getImage())
                    .circleCrop()
                    .into(mImageAvatar);
            List<Message> messages = new ArrayList<>(room.getMessages().values());
            mTextLastMessageOfUser.setText(messages.get(messages.size() - 1).getContent());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemLongClick(mRoom.getId());
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mRoom.getId());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id);

        void onItemLongClick(String id);
    }
}
