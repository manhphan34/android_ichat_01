package framgia.com.ichat.data.repository;

import framgia.com.ichat.data.source.PublicRoomDataSource;

public class PublicRoomRepository implements PublicRoomDataSource.Remote {

    private PublicRoomDataSource.Remote mRemote;
    private static PublicRoomRepository sInstance;

    public static PublicRoomRepository getInstance(PublicRoomDataSource.Remote remote) {
        if (sInstance == null) {
            synchronized (PublicRoomRepository.class) {
                if (sInstance == null) {
                    sInstance = new PublicRoomRepository(remote);
                }
            }
        }
        return sInstance;
    }

    private PublicRoomRepository(PublicRoomDataSource.Remote remote) {
        mRemote = remote;
    }

    @Override
    public void getPublicRoom() {

    }
}
