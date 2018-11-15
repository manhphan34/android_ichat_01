package framgia.com.ichat.screen.chat;

import android.icu.lang.UScript;

import java.util.List;

import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.User;

import framgia.com.ichat.data.repository.RoomRepository;
import framgia.com.ichat.data.repository.UserRepository;
import framgia.com.ichat.screen.base.BasePresenter;

public interface ChatContract {
    interface View {
        void onGetDataSuccess(Message message);

        void onGetDataSuccess(List<String> emojis);

        void onGetUserSuccess(List<User> users);

        void onMessageNull();

        void navigateProfile(User user);

        String getRoomType();

        void onSystemError();

        void onRoomNameNull();

        void dismissDialog();

        void updateActionBar(String name);

        void onAddMemberSuccess();

        void onAddMemberFail();

        void navigateHome();
    }

    interface Presenter extends BasePresenter<View> {
        void getUser(UserRepository userRepository);

        void getUser(UserRepository userRepository, String id);

        User getUserValue();

        void setRoomType(String roomType);

        void sendMessage(String message, String emoji);

        void addOnChildChange(String id);

        void getEmojis();

        void renameRoom(String name, String roomType);

        void getUsers();

        void addMember(String roomType, User user);

        void exitRoom(String roomType);
    }
}
