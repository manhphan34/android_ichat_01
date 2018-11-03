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
    private OnUserItemClickListener mListener;

    public OnlineUserAdapter(Context context, List<User> users, OnUserItemClickListener listener) {
        mContext = context;
        mUsers = users;
        mListener =listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_online_room, viewGroup, false);
        return new ViewHolder(view, mListener);
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
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView mTextName;
        private OnUserItemClickListener mListener;
        private User mUser;
        private View mView;

        public ViewHolder(@NonNull View itemView, OnUserItemClickListener listener) {
            super(itemView);
            mView = itemView;
            mListener = listener;
            mImageView = itemView.findViewById(R.id.image_avatar);
            mTextName = itemView.findViewById(R.id.text_name);
            mView.setOnClickListener(this);
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
            mListener.onClickItem(mUser);
        }
    }

    public interface OnUserItemClickListener {
        void onClickItem(User user);
    }
}
