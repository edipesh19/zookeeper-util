package com.oracle.zookeeper;

import java.io.IOException;

public class ConnectOnExpiration implements Runnable {

    @Override
    public void run() {
        try {
            ZKNodeClientImpl.reconnect();
        } catch (IOException | InterruptedException e) {
            String msg = "Error while reconnecting to ZooKeeper after expiration";
            System.out.println(msg);
        }
    }
}
