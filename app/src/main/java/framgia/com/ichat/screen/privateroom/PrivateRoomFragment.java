package framgia.com.ichat.screen.privateroom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import framgia.com.ichat.R;
import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.data.repository.RoomRepository;
import framgia.com.ichat.data.source.remote.RoomRemoteDataSource;
import framgia.com.ichat.screen.chat.ChatActivity;
import framgia.com.ichat.screen.base.BaseFragment;

public class PrivateRoomFragment extends BaseFragment implements View.OnClickListener,
        PrivateRoomContract.View, PrivateRoomAdapter.OnItemClickListener {
    private PrivateRoomPresenter mPresenter;
    private List<Room> mRooms;
    private PrivateRoomAdapter mAdapter;

    public static PrivateRoomFragment newInstance() {
        return new PrivateRoomFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_private_room;
    }

    @Override
    protected void initComponents() {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_private_room);
        mRooms = new ArrayList<>();
        getView().findViewById(R.id.fab_private_room).setOnClickListener(this);
        mAdapter = new PrivateRoomAdapter(getActivity(), mRooms);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter = new PrivateRoomPresenter(RoomRepository.getInstance
                (RoomRemoteDataSource.getInstance(FirebaseDatabase.getInstance(),
                        FirebaseAuth.getInstance())));
        mPresenter.setView(this);
        mPresenter.getPrivateRooms(FirebaseAuth.getInstance().getUid());
    }

    @Override
    public void onClick(View view) {
        mPresenter.createPrivateRoom();
    }

    @Override
    public void onGetDataFailed() {
        Toast.makeText(getActivity(),
                getResources().getString(R.string.msg_get_data_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetPrivateRoomsSuccess(List<Room> rooms) {
        mAdapter.addData(rooms);
    }

    @Override
    public void onDeletePrivateRoomSuccess() {
        Toast.makeText(getActivity(), getString(R.string.msg_delete_conversation_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeletePrivateRoomFailed() {
        Toast.makeText(getActivity(), getString(R.string.msg_delete_conversation_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreatePrivateRoomSuccess() {
        Toast.makeText(getActivity(), getString(R.string.msg_create_private_room_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreatePrivateRoomFailed() {
        Toast.makeText(getActivity(), getString(R.string.msg_create_private_room_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(String id) {
        startActivity(ChatActivity.getChatIntent(getActivity(), id));
    }

    @Override
    public void onItemLongClick(final String id) {
        showAlertDialog(getString(R.string.title_confirm_delete), getString(R.string.msg_delete_conversation),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.deletePrivateRoom(id);
                    }
                }
        );
    }
}
