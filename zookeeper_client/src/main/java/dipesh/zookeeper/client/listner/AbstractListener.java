package dipesh.zookeeper.client.listner;

import org.apache.zookeeper.Watcher;

import java.util.List;

public abstract class AbstractListener implements IZkChildListener, IZkDataListener, IZkStateListener {
    @Override
    public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
    }

    @Override
    public void handleDataChange(String dataPath, byte[] data) throws Exception {
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
    }

    @Override
    public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
    }

    @Override
    public void handleNewSession() throws Exception {
    }
}
