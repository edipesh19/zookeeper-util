package dipesh.zookeeper.client.listner;

import java.util.List;

public interface IZkChildListener {
    public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception;
}
