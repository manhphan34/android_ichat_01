package framgia.com.ichat.screen.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import framgia.com.ichat.GlideApp;
import framgia.com.ichat.R;
import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.repository.UserRepository;
import framgia.com.ichat.data.source.remote.UserRemoteDataSource;
import framgia.com.ichat.screen.base.BaseActivity;
import framgia.com.ichat.screen.login.LoginActivity;

public class ProfileActivity extends BaseActivity implements ProfileContract.View, View.OnClickListener {
    private static final String EXTRA_USER = "EXTRA_USER";
    public static final int PICK_IMAGE_FROM_GALLERY = 0x248;
    public static final int PICK_IMAGE_FROM_CAMERA = 0x249;
    public static final int PERMISSIONS_REQUEST = 0x141;
    private ImageView mImageViewAvatar;
    private TextView mTextViewUserName, mTextViewEmail, mTextViewLastSignedIn;
    private ProfileContract.Presenter mPresenter;
    private Dialog mDialog;
    private EditText mEditDialogUserName;
    private ImageView mDialogImage;

    public static Intent getDataIntent(Context context, User user) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initComponents() {
        mImageViewAvatar = findViewById(R.id.image_view_user);
        mTextViewUserName = findViewById(R.id.text_view_user_name);
        mTextViewEmail = findViewById(R.id.text_view_user_email);
        mTextViewLastSignedIn = findViewById(R.id.text_view_user_last_sign_in);
        mPresenter = new ProfilePresenter(this, new UserRepository(new UserRemoteDataSource(
                FirebaseDatabase.getInstance(),
                FirebaseStorage.getInstance(),
                FirebaseAuth.getInstance())));

        findViewById(R.id.button_sign_out).setOnClickListener(this);
        findViewById(R.id.button_edit_profile).setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter.showUserInfo(getUser());
    }

    @Override
    public boolean isCheckSelfPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean isShouldShowRequestPermission() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                PERMISSIONS_REQUEST);
    }

    @Override
    public void onPermissionError() {
        this.showToastShort(getString(R.string.profile_permission_error));
    }

    @Override
    public void showCoverImage() {
        setImage();
    }

    @Override
    public void showUserInfo(String userName, String email, String pathImage, String lastSignedIn) {
        setImage(pathImage, mImageViewAvatar);
        mTextViewUserName.setText(userName);
        mTextViewEmail.setText(email);
        mTextViewLastSignedIn.setText(getString(R.string.profile_last_signed_in).concat(lastSignedIn));
    }

    @Override
    public void updateProfile(Uri uri, String name) {
        this.hideProgressDialog();
        mDialog.dismiss();
        setImage(uri.toString(), mImageViewAvatar);
        mTextViewUserName.setText(name);
    }

    @Override
    public void updateProfile(String name) {
        this.hideProgressDialog();
        mDialog.dismiss();
        mTextViewUserName.setText(name);
    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        navigate();
    }

    private void navigate() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showDialog() {
        initDialog();
        mDialog.show();
    }

    @Override
    public void setImageDialogFromGallery(Uri uri) {
        setImage(uri.toString(), mDialogImage);
    }

    @Override
    public void setImageDialogFromCamera(Bitmap bitmap) {
        setImage(bitmap, mDialogImage);
    }

    @Override
    public void chooseImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE_FROM_GALLERY);
    }

    @Override
    public void chooseImageFromCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), PICK_IMAGE_FROM_CAMERA);
    }

    @Override
    public void onUserNameError() {
        mEditDialogUserName.setError(getString(R.string.error_not_empty));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_edit_profile:
                mPresenter.requestPermission();
                break;
            case R.id.button_choose_camera:
                chooseImageFromCamera();
                break;
            case R.id.button_choose_gallery:
                chooseImageFromGallery();
                break;
            case R.id.button_update:
                mPresenter.updateInfo(getText(mEditDialogUserName));
                break;
            case R.id.button_sign_out:
                signOut();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.setImage(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, grantResults);
    }

    private User getUser() {
        return getIntent().getParcelableExtra(EXTRA_USER);
    }

    private void initDialog() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.70);
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_edit_profile);
        mEditDialogUserName = mDialog.findViewById(R.id.edit_pro_user_name);
        mDialog.getWindow().setLayout(width, height);
        mDialogImage = mDialog.findViewById(R.id.image_dialog_avatar);

        mDialog.findViewById(R.id.button_choose_camera).setOnClickListener(this);
        mDialog.findViewById(R.id.button_choose_gallery).setOnClickListener(this);
        mDialog.findViewById(R.id.button_update).setOnClickListener(this);
    }

    private String getText(EditText text) {
        return text.getText().toString();
    }

    private void setImage() {
        GlideApp.with(this)
                .load(R.drawable.ic_image_cover)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into((ImageView) findViewById(R.id.image_cover_image));
    }

    private void setImage(String uri, ImageView imageView) {
        GlideApp.with(this)
                .load(uri)
                .circleCrop()
                .into(imageView);

    }

    private void setImage(Bitmap bitmap, ImageView imageView) {
        GlideApp.with(this)
                .load(bitmap)
                .circleCrop()
                .into(imageView);

    }

    public void showProgress() {
        this.showProgressDialog();
    }
}
