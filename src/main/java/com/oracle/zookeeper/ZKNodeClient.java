package com.oracle.zookeeper;

import com.oracle.zookeeper.dto.AgentHeartBeatData;
import org.apache.zookeeper.data.Stat;

public interface ZKNodeClient {
    void createZNode(String path, AgentHeartBeatData agentHeartBeatData);

    Stat zNodeExists(String path);

    AgentHeartBeatData getZNodeData(String path);

    void updateZNodeData(String path, AgentHeartBeatData agentHeartBeatData);

    void deleteZNode(String path);
}
