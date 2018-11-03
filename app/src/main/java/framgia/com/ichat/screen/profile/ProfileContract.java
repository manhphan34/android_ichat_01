package framgia.com.ichat.screen.profile;

import framgia.com.ichat.data.model.User;

public interface ProfileContract {
    interface View{
        void showCoverImage();
        void showUserInfo(String userName, String email, String pathImage, String lastSignedIn);
    }
    interface Presenter{
        void showUserInfo(User user);
    }
}
