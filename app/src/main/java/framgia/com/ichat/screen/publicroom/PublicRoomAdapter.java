package framgia.com.ichat.screen.publicroom;

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

public class PublicRoomAdapter extends RecyclerView.Adapter<PublicRoomAdapter.ViewHolder> {
    private Context mContext;
    private List<Room> mRooms;
    private LayoutInflater mLayoutInflater;

    public PublicRoomAdapter(Context context, List<Room> rooms) {
        mContext = context;
        mRooms = rooms;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public PublicRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PublicRoomAdapter.ViewHolder(mLayoutInflater.inflate(R.layout.item_public_room, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PublicRoomAdapter.ViewHolder viewHolder, int i) {
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
        public TextView mTextNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageAvatar = itemView.findViewById(R.id.image_view_public_room);
            mTextName = itemView.findViewById(R.id.text_view_public_room_name);
            mTextNumber = itemView.findViewById(R.id.text_view_public_room_member);
        }

        public void bindView(Context context, Room room) {
            mTextName.setText(room.getName());
            GlideApp.with(context)
                    .load(room.getImage())
                    .circleCrop()
                    .into(mImageAvatar);
            List<Message> messages = new ArrayList<>(room.getMessages().values());
        }
    }
}
