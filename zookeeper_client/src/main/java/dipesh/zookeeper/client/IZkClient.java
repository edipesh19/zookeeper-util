package dipesh.zookeeper.client;

import dipesh.zookeeper.client.exception.ZkInterruptedException;
import dipesh.zookeeper.client.listner.IZkChildListener;
import dipesh.zookeeper.client.listner.IZkDataListener;
import dipesh.zookeeper.client.listner.IZkStateListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface IZkClient extends Closeable {

    int DEFAULT_CONNECTION_TIMEOUT = 10000;
    int DEFAULT_SESSION_TIMEOUT = 30000;

    void close() throws ZkInterruptedException;

    void connect(final long timeout, Watcher watcher);

    int countChildren(String path);

    String create(final String path, byte[] data, final CreateMode mode);

    void createEphemeral(final String path);

    void createEphemeral(final String path, final byte[] data);

    String createEphemeralSequential(final String path, final byte[] data);

    void createPersistent(String path);

    void createPersistent(String path, boolean createParents);

    void createPersistent(String path, byte[] data);

    String createPersistentSequential(String path, byte[] data);

    boolean delete(final String path);

    boolean deleteRecursive(String path);

    boolean exists(final String path);

    List<String> getChildren(String path);

    long getCreationTime(String path);

    int numberOfListeners();

    byte[] readData(String path);

    byte[] readData(String path, boolean returnNullIfPathNotExists);

    byte[] readData(String path, Stat stat);

    List<String> subscribeChildChanges(String path, IZkChildListener listener);

    void subscribeDataChanges(String path, IZkDataListener listener);

    void subscribeStateChanges(IZkStateListener listener);

    void unsubscribeAll();

    void unsubscribeChildChanges(String path, IZkChildListener childListener);

    void unsubscribeDataChanges(String path, IZkDataListener dataListener);

    void unsubscribeStateChanges(IZkStateListener stateListener);


    //void cas(String path, DataUpdater updater);

    boolean waitForKeeperState(Watcher.Event.KeeperState keeperState, long time, TimeUnit timeUnit);

    boolean waitUntilConnected() throws ZkInterruptedException;

    boolean waitUntilConnected(long time, TimeUnit timeUnit);

    boolean waitUntilExists(String path, TimeUnit timeUnit, long time);

    Stat writeData(String path, byte[] data);

    Stat writeData(String path, byte[] data, int expectedVersion);
    //List<?> multi(Iterable<?> ops);

    ZooKeeper getZooKeeper();

    boolean isConnected();

    /**
     * A CAS operation
     */
    interface DataUpdater {

        /**
         * Updates the current data of a znode.
         *
         * @param currentData The current contents.
         * @return the new data that should be written back to ZooKeeper.
         */
        public byte[] update(byte[] currentData);

    }

}
