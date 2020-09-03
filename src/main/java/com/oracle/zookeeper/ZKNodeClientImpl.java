package com.oracle.zookeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.oracle.zookeeper.dto.AgentHeartBeatData;
import com.oracle.zookeeper.serde.AgentHeartbeatDataSerDe;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZKNodeClientImpl implements ZKNodeClient {
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static CountDownLatch connectedSignal;
    private static ZooKeeper zooKeeper;
    private static AgentHeartbeatDataSerDe agentHeartbeatDataSerDe;
    private static ZKWatcher zkWatcher;

    public ZKNodeClientImpl() {
        connectedSignal = new CountDownLatch(1);
        zkWatcher = new ZKWatcher();
        agentHeartbeatDataSerDe = new AgentHeartbeatDataSerDe();
    }

    public void connect() throws InterruptedException, IOException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 4000, zkWatcher);
        connectedSignal.await();
    }

    public static void reconnect() throws IOException, InterruptedException {
        zooKeeper.close();
        connectedSignal = new CountDownLatch(1);
        zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 4000, zkWatcher);
        connectedSignal.await();
        System.out.println("Reconnected....");
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void closeConnection() throws InterruptedException {
        zooKeeper.close();
        scheduler.shutdown();
    }

    @Override
    public void createZNode(String path, AgentHeartBeatData agentHeartBeatData) {
        try {
            byte[] data = agentHeartbeatDataSerDe.serialize(agentHeartBeatData).getBytes();
            zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            System.out.println("Error while creating znode:" + path + e);
        }
    }

    @Override
    public Stat zNodeExists(String path) {
        try {
            return zooKeeper.exists(path, false);
        } catch (Exception e) {
            System.out.println("Error while checking znode existence:" + path + e);
            return null;
        }
    }

    @Override
    public AgentHeartBeatData getZNodeData(String path) {
        try {
            Stat stat = zNodeExists(path);
            if (stat != null) {
                byte[] bn = zooKeeper.getData(path, false, stat);
                return agentHeartbeatDataSerDe.deserialize(new String(bn, StandardCharsets.UTF_8));
            } else {
                System.out.println("Node does not exists:" + path);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error while getting data from znode:" + path + e);
            return null;
        }
    }

    @Override
    public void updateZNodeData(String path, AgentHeartBeatData agentHeartBeatData) {
        try {
            Stat stat = zNodeExists(path);
            if (stat != null) {
                byte[] data = agentHeartbeatDataSerDe.serialize(agentHeartBeatData).getBytes();
                zooKeeper.setData(path, data, stat.getVersion());
            } else {
                createZNode(path, agentHeartBeatData);
            }
        } catch (Exception e) {
            System.out.println("Error while updating znode:" + path + e);
        }
    }

    @Override
    public void deleteZNode(String path) {
        try {
            Stat stat = zNodeExists(path);
            if (stat != null) {
                zooKeeper.delete(path, stat.getVersion());
            } else {
                System.out.println("Node does not exists:" + path);
            }
        } catch (Exception e) {
            System.out.println("Error while deleting znode:" + path + e);
        }
    }

    private static class ZKWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            if (event.getState() == Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            } else if (event.getState() == Event.KeeperState.Expired) {
                System.out.println("Connection to Zookeeper expired. Attempting to reconnect...");
                scheduler.schedule(new ConnectOnExpiration(), 0, TimeUnit.SECONDS);
            } else {
                System.out.println("Connection to Zookeeper failed. Connection is in {} state" + event.getState());
            }
        }
    }
}
