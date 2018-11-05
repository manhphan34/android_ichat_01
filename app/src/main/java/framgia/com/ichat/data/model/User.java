package framgia.com.ichat.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class User implements Parcelable {
    private String mUid;
    private String mEmail;
    private String mPassword;
    private String mDisplayName;
    private String mPhotoUrl;
    private long mLastSignIn;
    private boolean mIsOnline;

    public User() {
    }

    public User(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public User(String uid, String email, String displayName, String photoUrl, long lastSignIn, boolean isOnline) {
        mUid = uid;
        mEmail = email;
        mDisplayName = displayName;
        mPhotoUrl = photoUrl;
        mLastSignIn = lastSignIn;
        mIsOnline = isOnline;
    }

    public User(FirebaseUser firebaseUser) {
        mUid = firebaseUser.getUid();
        mEmail = firebaseUser.getEmail();
        mDisplayName = firebaseUser.getDisplayName();
        mPhotoUrl = String.valueOf(firebaseUser.getPhotoUrl());
        mLastSignIn = Calendar.getInstance().getTimeInMillis();
        mIsOnline = true;
    }

    public User(FirebaseUser user, boolean isOnline) {
        mUid = user.getUid();
        mEmail = user.getEmail();
        mDisplayName = user.getDisplayName();
        mPhotoUrl = String.valueOf(user.getPhotoUrl());
        mLastSignIn = user.getMetadata().getLastSignInTimestamp();
        mIsOnline = isOnline;
    }

    public User(Parcel in) {
        mUid = in.readString();
        mEmail = in.readString();
        mDisplayName = in.readString();
        mPhotoUrl = in.readString();
        mLastSignIn = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUid);
        dest.writeString(mEmail);
        dest.writeString(mDisplayName);
        dest.writeString(mPhotoUrl);
        dest.writeLong(mLastSignIn);
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public long getLastSignIn() {
        return mLastSignIn;
    }

    public void setLastSignIn(long lastSignIn) {
        mLastSignIn = lastSignIn;
    }

    public boolean isOnline() {
        return mIsOnline;
    }

    public void setOnline(boolean online) {
        mIsOnline = online;
    }

    public class UserKey {
        public static final String USER_REFERENCE = "user";
        public static final String DISPLAY_NAME = "displayName";
    }
}
