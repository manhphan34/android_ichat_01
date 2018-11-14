package framgia.com.ichat.screen.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import framgia.com.ichat.R;

import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.data.repository.ChatRepository;
import framgia.com.ichat.data.repository.RoomRepository;
import framgia.com.ichat.data.repository.UserRepository;
import framgia.com.ichat.data.source.remote.ChatRemoteDataSource;
import framgia.com.ichat.data.source.remote.RoomRemoteDataSource;
import framgia.com.ichat.data.source.remote.UserRemoteDataSource;
import framgia.com.ichat.screen.base.BaseActivity;
import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.User;
import framgia.com.ichat.screen.profile.ProfileActivity;

public class ChatActivity extends BaseActivity implements ChatContract.View,
        View.OnClickListener, ChatAdapter.OnMessageItemClickListener,
        EmojiAdapter.OnItemClickListener {
    public static final double DIALOG_EMOJI_WIDTH = 0.90;
    public static final double DIALOG_EMOJI_HEIGHT = 0.90;
    public static final double DIALOG_RENAME_ROOM_WIDTH = 0.90;
    public static final double DIALOG_RENAME_ROOM_HEIGHT = 0.20;
    public static final String EXTRA_ROOM = "EXTRA_ROOM";
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
    private EditText mEditRoomName;
    private TextView mTextRoomName;

    public static Intent getChatIntent(Context context, Room room, String roomType) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_ROOM, room);
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
        initActionBar();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Room room = getIntent().getParcelableExtra(EXTRA_ROOM);
        mPresenter = new ChatPresenter(ChatRepository.getInstance(
                ChatRemoteDataSource.getInstance(FirebaseDatabase.getInstance())),
                room.getId());
        mPresenter.setView(this);
        mPresenter.setRoomType(getRoomType());
        mPresenter.addOnChildChange(room.getId());
        mUserRepository = UserRepository.getInstance(
                UserRemoteDataSource.getInstance(
                        FirebaseDatabase.getInstance(),
                        FirebaseStorage.getInstance(),
                        FirebaseAuth.getInstance()
                )
        );
        updateActionBar(room.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getUser(mUserRepository);
    }

    @Override
    public void onGetDataSuccess(Message message) {
        mChatAdapter.addData(message);
        int last = getLastPosition();
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
                showDialogEmoji();
                break;
            case R.id.image_add_file:
                break;
            case R.id.button_rename_room:
                RoomRepository repository = RoomRepository.getInstance(
                        RoomRemoteDataSource.getInstance(
                                FirebaseDatabase.getInstance(),
                                FirebaseAuth.getInstance()
                        ));
                mPresenter.renameRoom(getText(mEditRoomName), repository, getRoomType());
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

    @Override
    public void dismissDialog() {
        mDialog.dismiss();
    }

    @Override
    public void updateActionBar(String name) {
        mTextRoomName.setText(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_friend:
                break;
            case R.id.rename_room:
                showDialogRenameRoom();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String path) {
        mDialog.dismiss();
        mPresenter.sendMessage(mEditTextMessage.getText().toString(), path);
    }

    private void sendMessage() {
        mPresenter.sendMessage(mEditTextMessage.getText().toString(), NULL);
        mEditTextMessage.setText(NULL);
    }

    private int getLastPosition() {
        return mChatAdapter.getItemCount() == 0 ? 0 : mChatAdapter.getItemCount() - 1;
    }

    private void initDialogEmoji() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_emoji_chat);
        resizeDialog(DIALOG_EMOJI_WIDTH, DIALOG_EMOJI_HEIGHT);
        RecyclerView recyclerEmoji = mDialog.findViewById(R.id.recycle_emoji_item);
        mEmojiAdapter = new EmojiAdapter(this, this);
        recyclerEmoji.setLayoutManager(new GridLayoutManager(this, SPAN_GRID));
        recyclerEmoji.setAdapter(mEmojiAdapter);
        mPresenter.getEmojis();
    }

    private void showDialogRenameRoom() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_rename_room);
        resizeDialog(DIALOG_RENAME_ROOM_WIDTH, DIALOG_RENAME_ROOM_HEIGHT);
        mEditRoomName = mDialog.findViewById(R.id.edit_dialog_room_name);
        mDialog.findViewById(R.id.button_rename_room).setOnClickListener(this);
        mDialog.show();
    }

    private void showDialogEmoji() {
        initDialogEmoji();
        mDialog.show();
    }

    private String getText(EditText editText) {
        return editText.getText().toString();
    }

    private void resizeDialog(double w, double h) {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * w);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * h);
        mDialog.getWindow().setLayout(width, height);
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_action_bar_title, null);
        ActionBar.LayoutParams p = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        p.gravity = Gravity.CENTER;
        mTextRoomName = v.findViewById(R.id.text_view_title_action_bar);
        getSupportActionBar().setCustomView(v, p);
    }
}
