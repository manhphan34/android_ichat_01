package framgia.com.ichat.screen.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import framgia.com.ichat.ApplicationGlideModule;
import framgia.com.ichat.GlideApp;
import framgia.com.ichat.R;
import framgia.com.ichat.data.repository.UserRepository;
import framgia.com.ichat.data.source.remote.UserRemoteDataSource;
import framgia.com.ichat.screen.base.BaseActivity;
import framgia.com.ichat.screen.profile.ProfileActivity;

public class HomeActivity extends BaseActivity implements
        SearchView.OnQueryTextListener, HomeContract.View, View.OnClickListener {
    private HomeContract.Presenter mPresenter;
    private ImageView mImageViewUser;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void initComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TabLayout tabLayout = findViewById(R.id.tab_home);
        ViewPager viewPager = findViewById(R.id.viewpager_home);
        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.title_tab_home)));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mImageViewUser = findViewById(R.id.image_toolbar_home);
        mPresenter = new HomePresenter(this,
                UserRepository.getInstance(UserRemoteDataSource.getInstance(
                        FirebaseDatabase.getInstance(),
                        FirebaseStorage.getInstance(),
                        FirebaseAuth.getInstance()
                )));
        mImageViewUser.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getUser(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        MenuItem searchItem = menu.findItem(R.id.search_home);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override

    public void showImage(String uri) {
        GlideApp.with(getApplicationContext())
                .load(uri)
                .apply(new RequestOptions().override(ApplicationGlideModule.WIDTH,
                        ApplicationGlideModule.HEIGHT))
                .circleCrop()
                .into(mImageViewUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_toolbar_home:
                navigateProfile();
                break;
        }
    }

    private void navigateProfile() {
        startActivity(ProfileActivity.getIntent(this, mPresenter.getUSer()));
    }
}

