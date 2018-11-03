package framgia.com.ichat.screen.profile;

import java.text.SimpleDateFormat;
import java.util.Date;

import framgia.com.ichat.data.model.User;

public class ProfilePresenter implements ProfileContract.Presenter {
    private static final String PATTERN = "dd/MM/yyyy";
    private ProfileContract.View mView;


    ProfilePresenter(ProfileContract.View view){
        mView = view;
    }

    @Override
    public void showUserInfo(User user) {
        mView.showCoverImage();
        mView.showUserInfo(user.getDisplayName(),
                user.getEmail(),
                user.getPhotoUrl(),
                formatDate(user.getLastSignIn()));
    }
    private String formatDate(long time){
        String dateString = new SimpleDateFormat(PATTERN).format(new Date(time));
        return dateString;
    }
}
