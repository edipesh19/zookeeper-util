package com.oracle.zookeeper;

import com.oracle.zookeeper.dto.AgentHeartBeatData;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperApplication {

    public static void main(String[] args) {
        try {
            ZKNodeClientImpl zkManager = new ZKNodeClientImpl();
            zkManager.connect();
            for (int i = 0; i < 3; i++) {
                AgentHeartBeatData agentHeartBeatData = createAgentMessageDTO(i);
                String path = "/uytrjhgfhjk" + i;

                zkManager.createZNode(path, agentHeartBeatData);

                Stat stat = zkManager.zNodeExists(path);
                System.out.println("Stat:" + stat.toString());
                AgentHeartBeatData mydata = zkManager.getZNodeData(path);
                System.out.println("Znode data: " + mydata);

                agentHeartBeatData = createNewAgentMessageDTO(i);
                zkManager.updateZNodeData(path, agentHeartBeatData);
                mydata = zkManager.getZNodeData(path);
                System.out.println("Znode new data: " + mydata);

                zkManager.deleteZNode(path);
                stat = zkManager.zNodeExists(path);
                System.out.println("Stat after deletion: " + stat);

                System.out.println("************************************************");
                Thread.sleep(10000);
                zkManager.getZooKeeper().getTestable().injectSessionExpiration();
                Thread.sleep(5000);
            }
            zkManager.closeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static AgentHeartBeatData createNewAgentMessageDTO(int i) {
        AgentHeartBeatData agentHeartBeatData = new AgentHeartBeatData();
        agentHeartBeatData.setAgentId("fghhtrgfhgfdnbvttehrgtrrrtrd");
        agentHeartBeatData.setAgentInstanceId("qwertyuiop");
        agentHeartBeatData.setAggregatorId("1234");
        agentHeartBeatData.setMsgId(i + "4321");
        return agentHeartBeatData;
    }

    private static AgentHeartBeatData createAgentMessageDTO(int i) {
        AgentHeartBeatData agentHeartBeatData = new AgentHeartBeatData();
        agentHeartBeatData.setAgentId("fghhtrgfhgfdnbvttehrgtrrrtrd");
        agentHeartBeatData.setAgentInstanceId("qwertyuiop");
        agentHeartBeatData.setAggregatorId("1234");
        agentHeartBeatData.setMsgId("1234" + i);
        return agentHeartBeatData;
    }
}
