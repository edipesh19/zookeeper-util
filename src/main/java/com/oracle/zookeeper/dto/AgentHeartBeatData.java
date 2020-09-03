package com.oracle.zookeeper.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AgentHeartBeatData {

    private String msgId;
    private long lastUpdateTime;
    private String aggregatorId;
    private String agentId;
    private String agentInstanceId;
    private String msgType;
    private String payload;
    private String payloadType;

}
