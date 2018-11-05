package framgia.com.ichat.screen.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import framgia.com.ichat.data.model.User;

public interface ProfileContract {
    interface View {
        boolean isCheckSelfPermission();

        boolean isShouldShowRequestPermission();

        void requestPermission();

        void onPermissionError();

        void showCoverImage();

        void showDialog();

        void showProgress();

        void setImageDialogFromGallery(Uri uri);

        void setImageDialogFromCamera(Bitmap bitmap);

        void chooseImageFromGallery();

        void chooseImageFromCamera();

        void onUserNameError();

        void showUserInfo(String userName, String email, String pathImage, String lastSignedIn);

        void updateProfile(Uri uri, String name);

        void updateProfile(String name);

        void signOut();
    }

    interface Presenter {
        void requestPermission();

        void onRequestPermissionsResult(int requestCode, int[] grantResults);

        void showUserInfo(User user);

        void setImage(int requestCode, int resultCode, Intent data);

        void update();

        void updateInfo(String userName);
    }
}
