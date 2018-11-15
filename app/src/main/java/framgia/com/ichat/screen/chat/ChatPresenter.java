package framgia.com.ichat.screen.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.model.User;
import framgia.com.ichat.data.repository.ChatRepository;
import framgia.com.ichat.data.repository.RoomRepository;
import framgia.com.ichat.data.repository.UserRepository;

public class ChatPresenter implements ChatContract.Presenter, ValueEventListener {
    private static final String PATTERN = "EEE, d MMM yyyy, HH:mm";
    private ChatContract.View mView;
    private ChatRepository mChatRepository;
    private RoomRepository mRoomRepository;
    private UserRepository mUserRepository;
    private User mUser;
    private String mRoomId;
    private String mRoomType;

    public ChatPresenter(ChatRepository chatRepository, RoomRepository roomRepository,
                         UserRepository userRepository, String roomId) {
        mChatRepository = chatRepository;
        mRoomRepository = roomRepository;
        mUserRepository = userRepository;
        mRoomId = roomId;
    }

    @Override
    public void setView(ChatContract.View view) {
        mView = view;
    }

    @Override
    public void getUser(UserRepository userRepository) {
        userRepository.getUser(FirebaseAuth.getInstance().getCurrentUser(), this);
    }

    @Override
    public void getUser(UserRepository userRepository, String id) {
        userRepository.getUser(id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                mView.navigateProfile(mUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onSystemError();
            }
        });
    }

    @Override
    public User getUserValue() {
        return mUser;
    }

    @Override
    public void setRoomType(String roomType) {
        mRoomType = roomType;
    }

    @Override
    public void sendMessage(String message, String emoji) {
        if (isEmpty(message) && isEmpty(emoji)) {
            mView.onMessageNull();
            return;
        }
        send(message, emoji);
    }

    @Override
    public void addOnChildChange(String id) {
        mChatRepository.addOnChildChange(id, mRoomType, new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mView.onGetDataSuccess(dataSnapshot.getValue(Message.class));
            }

            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onSystemError();
            }
        });
    }

    @Override
    public void getEmojis() {
        mChatRepository.getEmojis(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mView.onGetDataSuccess(getEmojis(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onSystemError();
            }
        });
    }

    @Override
    public void renameRoom(final String name, String roomType) {
        if (name.isEmpty()) {
            mView.onRoomNameNull();
            return;
        }
        mRoomRepository.renameRoom(roomType, mRoomId, name,
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        mView.updateActionBar(name);
                        mView.dismissDialog();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.onSystemError();
                    }
                });
    }

    @Override
    public void getUsers() {
        mUserRepository.getUsers(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mView.onGetUserSuccess(getUsers(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onSystemError();
            }
        });
    }

    @Override
    public void addMember(String roomType, User user) {
        mRoomRepository.addMember(roomType, mRoomId, user,
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        mView.dismissDialog();
                        if (!task.isSuccessful()) {
                            return;
                        }
                        mView.onAddMemberSuccess();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.dismissDialog();
                        mView.onAddMemberFail();
                    }
                });
    }

    @Override
    public void exitRoom(String roomType) {
        mRoomRepository.exitRoom(roomType, mRoomId, mUser.getUid(),
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        mView.navigateHome();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.onSystemError();
                    }
                });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mUser = dataSnapshot.getValue(User.class);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mView.onSystemError();
    }

    private boolean isEmpty(String s) {
        return s.isEmpty();
    }

    private void send(String message, String path) {
        mChatRepository.sendMessage(mRoomId, mRoomType, getMessage(message, path));
    }

    private Message getMessage(String message, String emoji) {
        return initMessage(message, emoji);
    }

    private Message initMessage(String message, String emoji) {
        return new Message(
                message,
                getCurrentTime(),
                mUser.getUid(),
                mUser.getDisplayName(),
                mUser.getPhotoUrl(),
                emoji
        );
    }

    public String getCurrentTime() {
        return new SimpleDateFormat(PATTERN).format(Calendar.getInstance().getTime());
    }

    private List<String> getEmojis(DataSnapshot dataSnapshot) {
        List<String> emojis = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            emojis.add(snapshot.getValue().toString());
        }
        return emojis;
    }

    private List<User> getUsers(DataSnapshot dataSnapshot) {
        List<User> users = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            users.add(snapshot.getValue(User.class));
        }
        return users;
    }
}
