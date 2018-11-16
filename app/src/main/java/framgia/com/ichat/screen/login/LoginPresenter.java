package framgia.com.ichat.screen.login;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.repository.AuthenticationRepository;
import framgia.com.ichat.data.source.CallBack;

public class LoginPresenter implements LoginContract.Presenter, OnCompleteListener, OnFailureListener {
    private boolean mIsRemember;
    private LoginContract.View mLoginView;
    private AuthenticationRepository mRepository;

    public LoginPresenter(LoginContract.View view, AuthenticationRepository repository) {
        this.mLoginView = view;
        this.mRepository = repository;
        mIsRemember = mRepository.getRememberStatus();
    }

    @Override
    public void showUserInformation() {
        if (getRememberStatus()) setUserInformation();
    }

    @Override
    public boolean validateEmailPassword(String email, String password) {
        boolean result = false;
        if (email.isEmpty()) {
            mLoginView.onErrorEmail();
            result = true;
        }
        if (password.isEmpty()) {
            mLoginView.onErrorPassword();
            result = true;
        }
        return result;
    }

    @Override
    public void saveUserInformation(String email, String password) {
        mRepository.saveUserInformation(email, password);
    }

    @Override
    public void setUserInformation() {
        mRepository.getUserInformation(new CallBack<User>() {
            @Override
            public void onLoginSuccess(User data) {
                getInformationRemember(data);
            }

            @Override
            public void onLoginFailed(Exception e) {

            }
        });
    }

    @Override
    public void login(String email, String password) {
        if (!validateEmailPassword(email, password)) {
            mLoginView.showProgress();
            loginWithAccount(email, password);
        }
    }

    @Override
    public void loginWithAccount(String email, String password) {
        mRepository.getLoginAccountStatus(email, password, this, this);
    }

    @Override
    public void getInformationRemember(User user) {
        mLoginView.updateUi(user.getEmail(), user.getPassword());
    }

    @Override
    public boolean getRememberStatus() {
        return mRepository.getRememberStatus();
    }

    @Override
    public void setIsRemember(boolean isRemember) {
        mIsRemember = isRemember;
    }

    @Override
    public void getSignedInAccount(int requestCode, Intent data) {
        mLoginView.showProgress();
        if (requestCode == LoginKey.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInWithGoogle(task);
        }
    }

    @Override
    public void handleSignInWithGoogle(Task<GoogleSignInAccount> task) {
        try {
            mRepository.signInWithGoogle(task.getResult(ApiException.class),
                    new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            loginGoogleSuccess();
                        }
                    }, this);
        } catch (ApiException e) {
            mLoginView.onSystemError();
        }
    }

    @Override
    public void onComplete(@NonNull Task task) {
        mLoginView.hideProgress();
        if (!task.isSuccessful()) {
            return;
        }
        loginSuccess();
        mLoginView.navigateHome();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        mLoginView.onLoginError();
    }

    private void loginSuccess() {
        if (mIsRemember) {
            mLoginView.saveInformation();
        }
        mRepository.changeRememberStatus(mIsRemember);
        mRepository.updateUser(mRepository.getCurrentUser());
    }

    private void loginGoogleSuccess() {
        mRepository.saveUserToDatabase(FirebaseAuth.getInstance().getCurrentUser(),
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        mLoginView.navigateHome();
                    }
                }, LoginPresenter.this);
    }
}
