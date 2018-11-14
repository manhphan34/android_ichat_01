package framgia.com.ichat.screen.chat;

import java.util.List;

import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.User;

import framgia.com.ichat.data.repository.UserRepository;
import framgia.com.ichat.screen.base.BasePresenter;

public interface ChatContract {
    interface View {
        void onGetDataSuccess(Message message);

        void onGetDataSuccess(List<String> emojis);

        void onMessageNull();

        void navigateProfile(User user);

        String getRoomType();

        void onSystemError();
    }

    interface Presenter extends BasePresenter<View> {
        void getUser(UserRepository userRepository);

        void getUser(UserRepository userRepository, String id);

        User getUserValue();

        void setRoomType(String roomType);

        void sendMessage(String message, String emoji);

        void addOnChildChange(String id);

        void getEmojis();
    }
}
