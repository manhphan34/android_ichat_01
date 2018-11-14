package framgia.com.ichat.screen.publicroom;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import framgia.com.ichat.screen.base.BaseFragment;
import framgia.com.ichat.screen.chat.ChatActivity;

public class PublicRoomFragment extends BaseFragment implements PublicRoomContract.View,
        View.OnClickListener, PublicRoomAdapter.OnItemClickListener {
    private static final int SPAN = 2;
    private PublicRoomContract.Presenter mPresenter;
    private PublicRoomAdapter mAdapter;

    public static PublicRoomFragment newInstance() {
        return new PublicRoomFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_public_room;
    }

    @Override
    protected void initComponents() {
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_public_room);
        getView().findViewById(R.id.fab_public_room).setOnClickListener(this);
        List<Room> rooms = new ArrayList<>();
        mAdapter = new PublicRoomAdapter(getActivity(), rooms);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter = new PublicRoomPresenter(
                RoomRepository.getInstance(RoomRemoteDataSource.getInstance(
                        FirebaseDatabase.getInstance(), FirebaseAuth.getInstance())));
        mPresenter.setView(this);
        mPresenter.getPublicRooms(FirebaseAuth.getInstance().getUid());
    }

    @Override
    public void onClick(View view) {
        mPresenter.createPublicRoom();
    }

    @Override
    public void onGetRoomsSuccess(List<Room> rooms) {
        mAdapter.addData(rooms);
    }

    @Override
    public void onGetRoomsFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreatePublicRoomSuccess() {
        Toast.makeText(getActivity(), R.string.msg_create_private_room_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreatePublicRoomFailed() {
        Toast.makeText(getActivity(),
                R.string.msg_create_public_room_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(String id) {
        startActivity(ChatActivity.getChatIntent(getActivity(), id));
    }
}
