package framgia.com.ichat.screen.chat;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.data.repository.ChatRepository;

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private ChatRepository mRepository;

    public ChatPresenter(ChatRepository repository) {
        mRepository = repository;
    }

    @Override
    public void setView(ChatContract.View view) {
        mView = view;
    }

    @Override
    public void getListMessage(String id) {
        mRepository.getListMessage(id, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Message> listMessage = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listMessage.add(snapshot.getValue(Message.class));
                }
                mView.onGetDataSuccess(listMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
