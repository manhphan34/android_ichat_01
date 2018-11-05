package framgia.com.ichat.screen.profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.repository.UserRepository;

public class ProfilePresenter implements ProfileContract.Presenter,
        OnFailureListener, OnCompleteListener, ValueEventListener {
    private static final String PATTERN = "dd/MM/yyyy";
    private ProfileContract.View mView;
    private Uri mFilepath;
    private String mName;
    private UserRepository mRepository;
    private ByteArrayOutputStream mBytes;


    ProfilePresenter(ProfileContract.View view, UserRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void requestPermission() {
        if (!mView.isCheckSelfPermission()) {
            mView.showDialog();
            return;
        }
        if (!mView.isShouldShowRequestPermission()) {
            mView.requestPermission();
            return;
        }
        mView.requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == ProfileActivity.PERMISSIONS_REQUEST
                && grantResults.length > 0 && !isGranted(grantResults)) {
            mView.onPermissionError();
            return;
        }
        mView.showDialog();
    }

    @Override
    public void showUserInfo(User user) {
        mView.showCoverImage();
//        mView.showUserInfo(user.getDisplayName(),
//                user.getEmail(),
//                user.getPhotoUrl(),
//                formatDate(user.getLastSignIn()));
    }

    @Override
    public void setImage(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == ProfileActivity.PICK_IMAGE_FROM_GALLERY) {
                pickImageFromGallery(data);
            } else if (requestCode == ProfileActivity.PICK_IMAGE_FROM_CAMERA) {
                pickImageFromCamera((Bitmap) data.getExtras().get("data"));
            }
        }
    }

    @Override
    public void updateInfo(String userName) {
        if (isUserNameEmpty(userName)) {
            mView.onUserNameError();
            return;
        }
        mName = userName;
        mView.showProgress();
        update();
    }

    @Override
    public void update() {
        if (mFilepath != null) {
            mRepository.uploadImage(FirebaseAuth.getInstance().getCurrentUser()
                    , mFilepath, this, this);
        } else if (mBytes != null) {
            mRepository.uploadImage(FirebaseAuth.getInstance().getCurrentUser()
                    , mBytes, this, this);
        } else {
            mRepository.updateUserName(FirebaseAuth.getInstance().getCurrentUser().getUid(), mName);
            mView.updateProfile(mName);
        }
    }

    @Override
    public void onComplete(@NonNull Task task) {
        checkTask(task);
        mRepository.getLinkImage(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                checkTask(task);
                mRepository.updateUser(initUser(task.getResult().toString()));
                mView.updateProfile(Uri.parse(task.getResult().toString()), mName);
            }
        });
    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }

    private User initUser(String path) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return new User(user.getUid(),
                user.getEmail(),
                mName,
                path,
                user.getMetadata().getLastSignInTimestamp(),
                true);
    }

    private boolean isUserNameEmpty(String userName) {
        return userName.isEmpty();
    }

    private String formatDate(long time) {
        String dateString = new SimpleDateFormat(PATTERN).format(new Date(time));
        return dateString;
    }

    private boolean isGranted(int[] grantResults) {
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void checkTask(Task task) {
        if (!task.isSuccessful()) {
            return;
        }
    }

    private void pickImageFromGallery(Intent data) {
        mFilepath = data.getData();
        mView.setImageDialogFromGallery(data.getData());
    }

    private void pickImageFromCamera(Bitmap bitmap) {
        mView.setImageDialogFromCamera(bitmap);
        mBytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, mBytes);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        user.setDisplayName(mName);
        mRepository.updateUser(user);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
