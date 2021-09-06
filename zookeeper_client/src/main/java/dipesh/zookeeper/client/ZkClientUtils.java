package dipesh.zookeeper.client;

import java.io.IOException;
import java.net.*;

public class ZkClientUtils {
    public final static String OVERWRITE_HOSTNAME_SYSTEM_PROPERTY = "zkclient.hostname.overwritten";

    public static String getLocalhostName() {
        String property = System.getProperty(OVERWRITE_HOSTNAME_SYSTEM_PROPERTY);
        if (property != null && property.trim().length() > 0) {
            return property;
        }
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (final UnknownHostException e) {
            throw new RuntimeException("unable to retrieve localhost name");
        }
    }

    public static boolean isPortFree(int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", port), 200);
            socket.close();
            return false;
        }
        catch (SocketTimeoutException e) {
            return true;
        }
        catch (ConnectException e) {
            return true;
        }
        catch (SocketException e) {
            if (e.getMessage().equals("Connection reset by peer")) {
                return true;
            }
            throw new RuntimeException(e);
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static RuntimeException convertToRuntimeException(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        retainInterruptFlag(e);
        return new RuntimeException(e);
    }

    public static void retainInterruptFlag(Throwable catchedException) {
        if (catchedException instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}