package framgia.com.ichat.screen.onlineuser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import framgia.com.ichat.GlideApp;
import framgia.com.ichat.R;
import framgia.com.ichat.data.model.User;

public class OnlineUserAdapter extends RecyclerView.Adapter<OnlineUserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private OnUserItemClickListener mClickListener;

    public OnlineUserAdapter(Context context, List<User> users, OnUserItemClickListener onUserItemClickListener) {
        mContext = context;
        mUsers = users;
        mClickListener = onUserItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_online_room, viewGroup, false);
        return new ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindView(mContext, mUsers.get(i));
    }

    @Override
    public int getItemCount() {
        return mUsers.isEmpty() ? 0 : mUsers.size();
    }

    public void addData(List<User> users) {
        if (users == null) {
            return;
        }
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView mTextName;
        public OnUserItemClickListener mClickListener;
        public User mUser;

        public ViewHolder(@NonNull View itemView, OnUserItemClickListener onUserItemClickListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_avatar);
            mTextName = itemView.findViewById(R.id.text_name);
            itemView.setOnClickListener(this);
            mClickListener = onUserItemClickListener;
        }

        public void bindView(Context context, User user) {
            mUser = user;
            mTextName.setText(user.getDisplayName());
            GlideApp.with(context)
                    .load(user.getPhotoUrl())
                    .circleCrop()
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onUserItemClick(mUser);
            }
        }
    }

    public interface OnUserItemClickListener {
        void onUserItemClick(User user);
    }
}
