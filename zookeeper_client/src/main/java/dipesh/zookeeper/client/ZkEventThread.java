package dipesh.zookeeper.client;

import dipesh.zookeeper.client.exception.ZkInterruptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ZkEventThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(ZkEventThread.class);
    private final BlockingQueue<ZkEvent> _events = new LinkedBlockingQueue<ZkEvent>();

    private static final AtomicInteger _eventId = new AtomicInteger(0);

    private volatile boolean shutdown = false;

    public ZkEventThread(String name) {
        setDaemon(true);
        setName("ZkClient-EventThread-" + getId() + "-" + name);
    }

    @Override
    public void run() {
        try {
            LOG.info("Starting ZkClient event thread.");
            while (!isShutdown()) {
                ZkEvent zkEvent = _events.take();
                int eventId = _eventId.incrementAndGet();
                LOG.debug("Delivering event #" + eventId + " " + zkEvent);
                try {
                    zkEvent.run();
                } catch (InterruptedException e) {
                    shutdown();
                } catch (ZkInterruptedException e) {
                    shutdown();
                } catch (Throwable e) {
                    LOG.error("Error handling event " + zkEvent, e);
                }
                LOG.debug("Delivering event #" + eventId + " done");
            }
        } catch (InterruptedException e) {
            LOG.info("Terminate ZkClient event thread.");
        }
    }

    /**
     * @return the shutdown
     */
    public boolean isShutdown() {
        return shutdown || isInterrupted();
    }

    public void shutdown() {
        this.shutdown = true;
        this.interrupt();
    }

    public void send(ZkEvent event) {
        if (!isShutdown()) {
            LOG.debug("New event: " + event);
            _events.add(event);
        }
    }

    static abstract class ZkEvent {

        private final String _description;
        public ZkEvent(String description) {
            _description = description;
        }
        public abstract void run() throws Exception;
        @Override
        public String toString() {
            return "ZkEvent[" + _description + "]";
        }
    }

}
