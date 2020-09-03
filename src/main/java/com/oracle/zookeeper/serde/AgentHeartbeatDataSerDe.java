package com.oracle.zookeeper.serde;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.zookeeper.dto.AgentHeartBeatData;

public class AgentHeartbeatDataSerDe {

    private static ObjectMapper c_objMapper;

    public AgentHeartbeatDataSerDe() {
        c_objMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String serialize(AgentHeartBeatData agentHeartBeatData) {
        try {
            if (agentHeartBeatData == null) {
                return null;
            } else {
                return c_objMapper.writeValueAsString(agentHeartBeatData);
            }
        } catch (JsonProcessingException e) {
            String errMsg = "Error when serializing AgentMessageDTO to String";
            System.out.println(errMsg);
            return null;
        }
    }

    public AgentHeartBeatData deserialize(String string) {
        try {
            if (string == null) {
                return null;
            } else {
                return c_objMapper.readValue(string, AgentHeartBeatData.class);
            }
        } catch (IOException e) {
            String errMsg = "Error when de-serializing String to AgentMessageDTO";
            e.printStackTrace();
            System.out.println(errMsg);
            return null;
        }
    }
}
