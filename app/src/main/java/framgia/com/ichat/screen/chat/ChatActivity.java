package framgia.com.ichat.screen.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import framgia.com.ichat.R;

import framgia.com.ichat.data.repository.ChatRepository;
import framgia.com.ichat.data.repository.UserRepository;
import framgia.com.ichat.data.source.remote.ChatRemoteDataSource;
import framgia.com.ichat.data.source.remote.UserRemoteDataSource;
import framgia.com.ichat.screen.base.BaseActivity;
import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.User;
import framgia.com.ichat.screen.profile.ProfileActivity;

public class ChatActivity extends BaseActivity implements ChatContract.View,
        View.OnClickListener, ChatAdapter.OnMessageItemClickListener,
        EmojiAdapter.OnItemClickListener {
    public static final double WIDTH = 0.90;
    public static final double HEIGHT = 0.90;
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_ROOM_TYPE = "EXTRA_ROOM_TYPE";
    public static final String NULL = null;
    public static final int SPAN_GRID = 3;
    private ChatPresenter mPresenter;
    private ChatAdapter mChatAdapter;
    private EmojiAdapter mEmojiAdapter;
    private EditText mEditTextMessage;
    private RecyclerView mRecyclerView;
    private UserRepository mUserRepository;
    private Dialog mDialog;

    public static Intent getChatIntent(Context context, String id, String roomType) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_ROOM_TYPE, roomType);
        return intent;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initComponents() {
        mRecyclerView = findViewById(R.id.recycle_chat_message);
        mEditTextMessage = findViewById(R.id.edit_chat_message);
        mChatAdapter = new ChatAdapter(this,
                FirebaseAuth.getInstance().getCurrentUser().getUid(), this);
        findViewById(R.id.image_send).setOnClickListener(this);
        findViewById(R.id.image_emoji).setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mChatAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String id = getIntent().getExtras().getString(EXTRA_ID);
        mPresenter = new ChatPresenter(ChatRepository.getInstance(
                ChatRemoteDataSource.getInstance(FirebaseDatabase.getInstance())),
                id);
        mPresenter.setView(this);
        mPresenter.setRoomType(getRoomType());
        mPresenter.addOnChildChange(id);
        mUserRepository = UserRepository.getInstance(
                UserRemoteDataSource.getInstance(
                        FirebaseDatabase.getInstance(),
                        FirebaseStorage.getInstance(),
                        FirebaseAuth.getInstance()
                )
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getUser(mUserRepository);
    }

    @Override
    public void onGetDataSuccess(Message message) {
        mChatAdapter.addData(message);
        mRecyclerView.scrollToPosition(getLastPosition());
    }

    @Override
    public void onGetDataSuccess(List<String> emojis) {
        mEmojiAdapter.addData(emojis);
    }

    @Override
    public void onMessageNull() {
        showToastShort(getString(R.string.chat_error_message_empty));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_send:
                sendMessage();
                break;
            case R.id.image_emoji:
                showDialog();
                break;
            case R.id.image_add_file:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClickMessageItem(String id) {
        mPresenter.getUser(mUserRepository, id);
    }

    @Override
    public void navigateProfile(User user) {
        startActivity(ProfileActivity.getIntent(this, user));
    }

    @Override
    public String getRoomType() {
        return getIntent().getStringExtra(EXTRA_ROOM_TYPE);
    }

    private void sendMessage() {
        mPresenter.sendMessage(mEditTextMessage.getText().toString(), NULL);
        mEditTextMessage.setText(NULL);
    }

    private int getLastPosition() {
        return mChatAdapter.getItemCount() == 0 ? 0 : mChatAdapter.getItemCount() - 1;
    }

    private void initDialog() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * WIDTH);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * HEIGHT);
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_emoji_chat);
        mDialog.getWindow().setLayout(width, height);
        RecyclerView recyclerEmoji = mDialog.findViewById(R.id.recycle_emoji_item);
        mEmojiAdapter = new EmojiAdapter(this, this);
        recyclerEmoji.setLayoutManager(new GridLayoutManager(this, SPAN_GRID));
        recyclerEmoji.setAdapter(mEmojiAdapter);
        mPresenter.getEmojis();
    }

    private void showDialog() {
        initDialog();
        mDialog.show();
    }

    @Override
    public void onItemClick(String path) {
        mDialog.dismiss();
        mPresenter.sendMessage(mEditTextMessage.getText().toString(), path);
    }
}
