package framgia.com.ichat.screen.publicroom;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.data.repository.RoomRepository;

public class PublicRoomPresenter implements PublicRoomContract.Presenter {
    private PublicRoomContract.View mView;
    private RoomRepository mRepository;

    public PublicRoomPresenter(RoomRepository repository) {
        mRepository = repository;
    }

    @Override
    public void getPublicRooms(final String id) {
        mRepository.getRooms(Room.PublicRoomKey.PUBLIC_ROOM, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Room> rooms = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    rooms.add(snapshot.getValue(Room.class));
                }
                mView.onGetRoomsSuccess(rooms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onGetRoomsFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void createPublicRoom() {
        Room room = mRepository.initDefaultRoom(Room.PublicRoomKey.NAME_DEFAULT,
                Room.PublicRoomKey.IMAGE_DEFAULT);
        mRepository.createRoom(room, Room.PublicRoomKey.PUBLIC_ROOM, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    mView.onCreatePublicRoomSuccess();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mView.onCreatePublicRoomFailed();
            }
        });
    }

    @Override
    public void setView(PublicRoomContract.View view) {
        mView = view;
    }
}
