package dipesh.zookeeper.client.listner;

import org.apache.zookeeper.Watcher;

public interface IZkStateListener {
    public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception;
    public void handleNewSession() throws Exception;
}
