package framgia.com.ichat.screen.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import framgia.com.ichat.R;
import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.repository.ChatRepository;
import framgia.com.ichat.data.source.remote.ChatRemoteDataSource;
import framgia.com.ichat.screen.base.BaseActivity;
import framgia.com.ichat.screen.privateroom.PrivateRoomAdapter;

public class ChatActivity extends BaseActivity implements ChatContract.View {
    public static final String EXTRA_ID = "EXTRA_ID";
    private ChatPresenter mPresenter;
    private String mId;
    private List<Message> mListMessage;
    private ChatAdapter mChatAdapter;

    public static Intent getChatIntent(Context context, String id) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initComponents() {
        RecyclerView recyclerView = findViewById(R.id.recycle_chat_message);
        mListMessage = new ArrayList<>();
        mChatAdapter = new ChatAdapter(this, mListMessage,
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView.setAdapter(mChatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mId = getIntent().getExtras().getString(EXTRA_ID);
        mPresenter = new ChatPresenter(ChatRepository.getInstance(
                ChatRemoteDataSource.getInstance(FirebaseDatabase.getInstance())));
        mPresenter.setView(this);
        mPresenter.getListMessage(mId);
    }

    @Override
    public void onGetDataSuccess(List<Message> listMessage) {
        if (listMessage != null) mChatAdapter.addData(listMessage);
    }
}
