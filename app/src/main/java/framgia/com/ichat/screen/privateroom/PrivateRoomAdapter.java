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

    public PrivateRoomAdapter(Context context, List<Room> rooms) {
        mContext = context;
        mRooms = rooms;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_private_room, viewGroup, false));
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
        mRooms.clear();
        mRooms.addAll(rooms);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageAvatar;
        public TextView mTextName;
        public TextView mTextLastMessageOfUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageAvatar = itemView.findViewById(R.id.image_avatar);
            mTextName = itemView.findViewById(R.id.text_name);
            mTextLastMessageOfUser = itemView.findViewById(R.id.text_last_message_of_user);
        }

        public void bindView(Context context, Room room) {
            mTextName.setText(room.getName());
            GlideApp.with(context)
                    .load(room.getImage())
                    .circleCrop()
                    .into(mImageAvatar);
            List<Message> messages = new ArrayList<>(room.getMessages().values());
            mTextLastMessageOfUser.setText(messages.get(messages.size() - 1).getContent());
        }
    }
}
