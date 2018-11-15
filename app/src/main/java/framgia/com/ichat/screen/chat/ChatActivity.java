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

import java.util.ArrayList;
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
import framgia.com.ichat.screen.home.HomeActivity;
import framgia.com.ichat.screen.onlineuser.OnlineUserAdapter;
import framgia.com.ichat.screen.profile.ProfileActivity;

public class ChatActivity extends BaseActivity implements ChatContract.View,
        View.OnClickListener, ChatAdapter.OnMessageItemClickListener,
        EmojiAdapter.OnItemClickListener, OnlineUserAdapter.OnUserItemClickListener {
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
    private OnlineUserAdapter mUserAdapter;

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
        initActionBar();
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
        Room room = getIntent().getParcelableExtra(EXTRA_ROOM);
        mPresenter = new ChatPresenter(getChatRepo(), getRoomRepo(),
                getUserRepo(), room.getId());
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
        mTextRoomName.setText(room.getName());
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
    public void onGetUserSuccess(List<User> users) {
        mUserAdapter.addData(users);
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
                mPresenter.renameRoom(getText(mEditRoomName), getRoomType());
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
    public void onItemClick(String path) {
        mDialog.dismiss();
        mPresenter.sendMessage(mEditTextMessage.getText().toString(), path);
    }

    @Override
    public void onSystemError() {
        showToastShort(getString(R.string.error_system_busy));
    }

    @Override
    public void onRoomNameNull() {
        showToastShort(getString(R.string.room_name_error));
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
    public void onUserItemClick(User user) {
        mPresenter.addMember(getRoomType(), user);
    }

    @Override
    public void onAddMemberSuccess() {
        showToastShort(getString(R.string.chat_add_member_success));
    }

    @Override
    public void onAddMemberFail() {
        showToastShort(getString(R.string.chat_add_new_member_fail));
    }

    @Override
    public void navigateHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
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
                showDialogAddMember();
                break;
            case R.id.rename_room:
                showDialogRenameRoom();
                break;
            case R.id.quit_room:
                mPresenter.exitRoom(getRoomType());
                break;
        }
        return super.onOptionsItemSelected(item);
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

    public void showDialogAddMember() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_add_member);
        RecyclerView recyclerView = mDialog.findViewById(R.id.recycle_member);
        List<User> users = new ArrayList<>();
        mUserAdapter = new OnlineUserAdapter(this, users, this);
        resizeDialog(DIALOG_EMOJI_WIDTH, DIALOG_EMOJI_HEIGHT);
        mPresenter.getUsers();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mUserAdapter);
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
        View view = inflater.inflate(R.layout.item_action_bar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mTextRoomName = view.findViewById(R.id.text_view_title_action_bar);
        getSupportActionBar().setCustomView(view, params);
    }

    private RoomRepository getRoomRepo() {
        return RoomRepository.getInstance(
                RoomRemoteDataSource.getInstance(
                        FirebaseDatabase.getInstance(),
                        FirebaseAuth.getInstance()
                ));
    }

    private ChatRepository getChatRepo() {
        return ChatRepository.getInstance(
                ChatRemoteDataSource.getInstance(FirebaseDatabase.getInstance()));
    }

    private UserRepository getUserRepo() {
        return UserRepository.getInstance(UserRemoteDataSource.getInstance(
                FirebaseDatabase.getInstance(),
                FirebaseStorage.getInstance(),
                FirebaseAuth.getInstance()
        ));
    }
}
