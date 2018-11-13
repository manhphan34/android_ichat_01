package framgia.com.ichat.screen.publicroom;

import java.util.List;

import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.screen.base.BasePresenter;

public class PublicRoomContract {
    interface View {
        void onGetRoomsSuccess(List<Room> rooms);

        void onGetRoomsFailed(String message);

        void onCreatePublicRoomSuccess();

        void onCreatePublicRoomFailed();
    }

    interface Presenter extends BasePresenter<View> {
        void getPublicRooms(String id);

        void createPublicRoom();
    }
}
