package framgia.com.ichat.screen.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import framgia.com.ichat.ApplicationGlideModule;
import framgia.com.ichat.GlideApp;
import framgia.com.ichat.R;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mEmojis;
    private OnItemClickListener mClickListener;

    public EmojiAdapter(Context context,
                        OnItemClickListener onItemClickListener) {
        mContext = context;
        mEmojis = new ArrayList<>();
        mClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EmojiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_emoji, viewGroup, false);
        return new EmojiAdapter.ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindView(mContext, mEmojis.get(i));
    }

    @Override
    public int getItemCount() {
        return mEmojis.isEmpty() ? 0 : mEmojis.size();
    }

    public void addData(List<String> emojis) {
        if (emojis != null && getItemCount() == 0) {
            mEmojis.addAll(emojis);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public EmojiAdapter.OnItemClickListener mClickListener;
        public String mPath;

        public ViewHolder(@NonNull View itemView, EmojiAdapter.OnItemClickListener onUserItemClickListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view_emoji);
            itemView.setOnClickListener(this);
            mClickListener = onUserItemClickListener;
        }

        public void bindView(Context context, String path) {
            mPath = path;
            GlideApp.with(context)
                    .load(path)
                    .placeholder(R.drawable.ic_loading)
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(mPath);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String path);
    }
}
