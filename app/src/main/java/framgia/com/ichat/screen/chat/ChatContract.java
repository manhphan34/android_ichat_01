package framgia.com.ichat.screen.chat;

import java.util.List;

import framgia.com.ichat.data.model.Message;
import framgia.com.ichat.screen.base.BasePresenter;

public class ChatContract {
    interface View {
        void onGetDataSuccess(List<Message> listMessage);
    }

    interface Presenter extends BasePresenter<View> {
        void getListMessage(String id);
    }
}
