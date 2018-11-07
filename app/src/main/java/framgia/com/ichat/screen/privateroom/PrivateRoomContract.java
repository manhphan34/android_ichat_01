package framgia.com.ichat.screen.privateroom;

import java.util.List;

import framgia.com.ichat.data.model.Room;
import framgia.com.ichat.screen.base.BasePresenter;

public class PrivateRoomContract {
    public interface View {
        void onGetDataFailed();

        void onGetListPrivateRoomSuccess(List<Room> rooms);
    }

    public interface Presenter extends BasePresenter<View> {
        void createPrivateRoom();

        void getPrivateRooms();
    }
}
