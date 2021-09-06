package dipesh.zookeeper.client;

import dipesh.zookeeper.client.exception.ZkException;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZkConnection {

    private static final Logger LOG = LoggerFactory.getLogger(ZkConnection.class);

    private ZooKeeper _zk = null;
    private final Lock _zookeeperLock = new ReentrantLock();

    private final String _servers;
    private final int _sessionTimeOut;

    public ZkConnection(String zkServers, int sessionTimeOut) {
        _servers = zkServers;
        _sessionTimeOut = sessionTimeOut;
    }

    public void connect(Watcher watcher) {
        _zookeeperLock.lock();
        try {
            if (_zk != null) {
                throw new IllegalStateException("zk client has already been started");
            }
            try {
                LOG.debug("Creating new ZookKeeper instance to connect to " + _servers + ".");
                _zk = new ZooKeeper(_servers, _sessionTimeOut, watcher);
            } catch (IOException e) {
                throw new ZkException("Unable to connect to " + _servers, e);
            }
        } finally {
            _zookeeperLock.unlock();
        }
    }
    public void close() throws InterruptedException {
        _zookeeperLock.lock();
        try {
            if (_zk != null) {
                LOG.debug("Closing ZooKeeper connected to " + _servers);
                _zk.close();
                _zk = null;
            }
        } finally {
            _zookeeperLock.unlock();
        }
    }

    public String create(String path, byte[] data, CreateMode mode) throws KeeperException, InterruptedException {
        return _zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
    }

    public void delete(String path) throws InterruptedException, KeeperException {
        _zk.delete(path, -1);
    }

    public boolean exists(String path, boolean watch) throws KeeperException, InterruptedException {
        return _zk.exists(path, watch) != null;
    }

    public List<String> getChildren(final String path, final boolean watch) throws KeeperException, InterruptedException {
        return _zk.getChildren(path, watch);
    }

    public byte[] readData(String path, Stat stat, boolean watch) throws KeeperException, InterruptedException {
        return _zk.getData(path, watch, stat);
    }

    public Stat writeData(String path, byte[] data, int version) throws KeeperException, InterruptedException {
        return _zk.setData(path, data, version);
    }

    public ZooKeeper.States getZookeeperState() {
        return _zk != null ? _zk.getState() : null;
    }

    public long getCreateTime(String path) throws KeeperException, InterruptedException {
        Stat stat = _zk.exists(path, false);
        if (stat != null) {
            return stat.getCtime();
        }
        return -1;
    }

    public String getServers() {
        return _servers;
    }

    public ZooKeeper getZooKeeper() {
        return _zk;
    }
}
