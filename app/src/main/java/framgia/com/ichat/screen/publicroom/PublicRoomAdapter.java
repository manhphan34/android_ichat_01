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
import framgia.com.ichat.data.model.Room;

public class PublicRoomAdapter extends RecyclerView.Adapter<PublicRoomAdapter.ViewHolder> {
    private Context mContext;
    private List<Room> mRooms;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public PublicRoomAdapter(Context context, List<Room> rooms) {
        mContext = context;
        mRooms = rooms;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.item_public_room, viewGroup,
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImagePublicRoom;
        private TextView mTextName;
        private TextView mTextMember;
        private OnItemClickListener mOnItemClickListener;

        private Room mRoom;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            mImagePublicRoom = itemView.findViewById(R.id.image_public_room);
            mTextName = itemView.findViewById(R.id.text_name);
            mTextMember = itemView.findViewById(R.id.text_member);
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        public void bindView(Context context, Room room) {
            mRoom = room;
            mTextName.setText(room.getName());
            mTextMember.setText(getNameMembers(context, new ArrayList<>(room.getMembers().values())));
            GlideApp.with(context)
                    .load(room.getImage())
                    .circleCrop()
                    .into(mImagePublicRoom);
        }

        public String getNameMembers(Context context, List<String> members) {
            StringBuilder stringBuilder = new StringBuilder(members.get(0));
            for (int i = 1; i < members.size(); i++) {
                stringBuilder.append(context.getResources().getString(R.string.char_comma));
                stringBuilder.append(members.get(i));
            }
            return stringBuilder.toString();
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
    }
}
