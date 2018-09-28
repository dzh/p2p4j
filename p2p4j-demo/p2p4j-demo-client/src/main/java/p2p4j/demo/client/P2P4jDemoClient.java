package p2p4j.demo.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.udp.UDPConn;
import p2p4j.demo.client.workflow.CheckSymmetricNatWork;
import p2p4j.demo.client.workflow.P2PWorkflow;
import p2p4j.demo.model.NatType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * socket分两类: nat检测和p2p连接
 * 
 * @author dzh
 * @date Sep 25, 2018 4:10:09 PM
 * @version 0.0.1
 */
public class P2P4jDemoClient implements Closeable {

    static Logger LOG = LoggerFactory.getLogger(P2P4jDemoClient.class);
    private InnerConn<?> conn;

    private volatile boolean closed = false;
    private NatType natType = NatType.UNKNOWN;
    private InetSocketAddress outAddr; // 外网出口地址和端口

    // private ConeConnection natConn;
    // private SymmetricConnection prodictConn;
    // private PeerConnection peerConn;

    private P2P4jDemoClientPlugin plugin;

    private String token;

    protected P2P4jDemoClient(P2P4jDemoClientPlugin plugin) {
        this.plugin = plugin;
        this.token = getConf(SimpleDemoProtocol.K_TOKEN);
    }

    protected void initConn() throws IOException {
        conn = new UDPConn(this);
        startWorkflow();

        offerWorkflow(new CheckSymmetricNatWork(this));
    }

    public void signalWork(SimpleDemoProtocol p) {
        if (work != null) work.signal(p);
    }

    private volatile P2PWorkflow<?> work; // 当前的工作

    private final ArrayBlockingQueue<P2PWorkflow<?>> workQueue = new ArrayBlockingQueue<>(10);

    public boolean offerWorkflow(P2PWorkflow<?> work) {
        return workQueue.offer(work);
    }

    private void startWorkflow() {
        Thread t = new Thread(() -> {
            while (true) {
                if (closed) break;

                try {
                    work = workQueue.poll(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {}

                if (work == null) continue;

                while (true) {
                    if (work == null || work.isLast()) break;

                    Object value = null;
                    try {
                        LOG.info("{} is working", work.getClass().getName());
                        value = work.call(); // blocked until work finished
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    } finally {
                        try {
                            work.close();
                        } catch (IOException e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }

                    if (work.isSuss()) {
                        work = work.next(value); // next work
                    } else {
                        LOG.error("{} failed to work", work.getClass().getName());
                        work.setLast(true);
                    }
                }
                LOG.info("{} exit", work.getClass().getName());
            }
            LOG.info("workflowThread exited");
        }, "workflowThread");
        t.setDaemon(true);
        t.start();
    }

    public InnerConn<?> conn() {
        return this.conn;
    }

    public NatType getNatType() {
        return natType;
    }

    public void setNatType(NatType natType) {
        this.natType = natType;
    }

    public String getConf(String key) {
        return plugin.getConfig(key);
    }

    public InetSocketAddress getOutAddr() {
        return outAddr;
    }

    public void setOutAddr(InetSocketAddress outAddr) {
        this.outAddr = outAddr;
    }

    public String getToken() {
        return token;
    }

    @Override
    public void close() throws IOException {
        if (conn != null) conn.close();
        // if (work != null) work.close();// TODO 重复关闭
        closed = true;
        LOG.info("client closed");
    }

    // @Override
    // public String toString() {
    // this.conn.
    // }

}
