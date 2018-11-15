package framgia.com.ichat.screen.onlineuser;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.repository.UserRepository;

public class OnlineUserPresenter implements OnlineUserContract.Presenter, ValueEventListener {
    private OnlineUserContract.View mView;
    private UserRepository mRepository;

    public OnlineUserPresenter(UserRepository repository) {
        mRepository = repository;
    }

    @Override
    public void setView(OnlineUserContract.View view) {
        this.mView = view;
    }

    @Override
    public void getUsers() {
        mRepository.getUsers(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List users = new ArrayList<User>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User user = snapshot.getValue(User.class);
            if (user.isOnline()) {
                users.add(user);
            }
        }
        mView.onGetUsersSuccess(users);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mView.onGetUsersFailed(databaseError.getMessage());
    }
}
