package framgia.com.ichat.screen.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import framgia.com.ichat.GlideApp;
import framgia.com.ichat.R;
import framgia.com.ichat.data.model.User;
import framgia.com.ichat.screen.base.BaseActivity;

public class ProfileActivity extends BaseActivity implements ProfileContract.View, View.OnClickListener {
    private static final String EXTRA_USER = "EXTRA_USER";
    private ImageView mImageViewAvatar;
    private TextView mTextViewUserName, mTextViewEmail, mTextViewLastSignedIn;
    private ProfileContract.Presenter mPresenter;

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
        mPresenter = new ProfilePresenter(this);

        findViewById(R.id.button_sign_out).setOnClickListener(this);
        findViewById(R.id.button_edit_profile).setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter.showUserInfo(getUser());

    }

    @Override
    public void showCoverImage() {
        GlideApp.with(this)
                .load(R.drawable.ic_image_cover)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into((ImageView) findViewById(R.id.image_cover_image));
    }

    @Override
    public void showUserInfo(String userName, String email, String pathImage, String lastSignedIn) {
        GlideApp.with(this)
                .load(pathImage)
                .circleCrop()
                .into(mImageViewAvatar);
        mTextViewUserName.setText(userName);
        mTextViewEmail.setText(email);
        mTextViewLastSignedIn.setText(getString(R.string.profile_last_signed_in).concat(lastSignedIn));
    }

    @Override
    public void onClick(View v) {

    }
    private User getUser(){
        return getIntent().getParcelableExtra(EXTRA_USER);
    }
}
