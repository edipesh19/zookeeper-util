package dipesh.zookeeper.client.listner;

public interface IZkDataListener {
    public void handleDataChange(String dataPath, byte[] data) throws Exception;
    public void handleDataDeleted(String dataPath) throws Exception;
}
