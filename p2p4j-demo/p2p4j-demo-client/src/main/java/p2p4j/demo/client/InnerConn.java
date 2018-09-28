package p2p4j.demo.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.protocol.CheckConeNatHandler;
import p2p4j.demo.client.protocol.CheckSymmetricNatHandler;
import p2p4j.demo.client.protocol.OutAddrHandler;
import p2p4j.demo.client.protocol.PingHandler;
import p2p4j.demo.client.protocol.ProtocolHandler;
import p2p4j.demo.model.P2P4jDemoConst;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 25, 2018 5:41:52 PM
 * @version 0.0.1
 */
public abstract class InnerConn<T extends Closeable> implements Closeable {

    static Logger LOG = LoggerFactory.getLogger(InnerConn.class);

    public static final int CONN_STATE_INIT = 1;
    public static final int CONN_STATE_CONNECTING = 2;
    public static final int CONN_STATE_SUCC = 3;
    public static final int CONN_STATE_FAIL = 4;
    public static final int CONN_STATE_CLOSED = 5;

    private P2P4jDemoClient client;

    protected volatile int connState;

    protected InetSocketAddress serverNat;
    protected InetSocketAddress serverPredict;

    private T socket;

    protected InnerConn(P2P4jDemoClient client) throws SocketException {
        this.client = client;

        serverNat = parseSocketAddress(getConnConf(P2P4jDemoConst.P2P4J_CLIENT_SERVER_NAT));
        serverPredict = parseSocketAddress(getConnConf(P2P4jDemoConst.P2P4J_CLIENT_SERVER_PREDICT));

        socket = initConn();
        connState = CONN_STATE_INIT;

        // String natServer = getConnConf(P2P4jDemoConst.P2P4J_CLIENT_SERVER_NAT);
        // boolean succ = false;
        // int retries = 0;
        // while (true) { // 这步多余
        // if (retries > 10) break;
        //
        // connState = CONN_STATE_CONNECTING;
        // succ = connServer(natServer);
        // if (succ) {
        // connState = CONN_STATE_SUCC;
        // LOG.info("succ to connect {}", natServer);
        // } else {
        // ++retries;
        // LOG.warn("failed to connect {}, retry after sleeping 10s", natServer);
        // try {
        // Thread.sleep(10 * 1000);
        // } catch (InterruptedException e) {
        // LOG.error(e.getMessage());
        // break;
        // }
        // }
        // }
        // if (succ) {
        // // 注册协议处理
        // registerProtocolHandler();
        // // 启动接收数据包线程
        // startReceiveThread();
        // // 获取外网地址和端口
        // sendOutAddrReq();
        // } else {
        // connState = CONN_STATE_FAIL;
        // }
        // LOG.info("NATConnection connState {} retries {}", connState, retries);

        // 注册协议处理
        registerProtocolHandler();
        // 启动接收数据包线程
        startReceiveThread();
        // 与NAT之间的心跳
        startPingThread();
    }

    private void sendOutAddrReq() {
        SimpleDemoProtocol p = SimpleDemoProtocol.create(ProtocolType.REQ_OUT_ADDR);
        try {
            send2Nat(p);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);// TODO retry
        }
    }

    private List<ProtocolHandler> handlers;

    private void registerProtocolHandler() {
        handlers = new LinkedList<>();
        handlers.add(new OutAddrHandler(this));
        handlers.add(new CheckSymmetricNatHandler(this));
        handlers.add(new CheckConeNatHandler(this));
        handlers.add(new PingHandler(this));
    }

    private InetSocketAddress parseSocketAddress(String addr) {
        String[] ipPort = addr.split(":");
        return new InetSocketAddress(ipPort[0], Integer.parseInt(ipPort[1]));
    }

    // 接收的数据包
    protected void receiveProtocol(SimpleDemoProtocol p, InetSocketAddress remoteAddr) {
        LOG.info("{} -> {}", remoteAddr, p);
        for (ProtocolHandler ph : handlers) {
            if (ph.accept(p)) {
                ph.handle(p, remoteAddr);
            }
        }
    }

    public P2P4jDemoClient client() {
        return this.client;
    }

    public String getConnConf(String key) {
        return client.getConf(key);
    }

    /*
     * (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
        if (socket != null) socket.close();
        connState = CONN_STATE_CLOSED;
        LOG.info("NATConnection connState {}", connState);
    }

    public T socket() {
        return this.socket;
    }

    public InetSocketAddress getServerNat() {
        return serverNat;
    }

    public InetSocketAddress getServerPredict() {
        return serverPredict;
    }

    protected abstract void startReceiveThread();

    protected abstract void startPingThread();

    protected abstract boolean connServer(String natServer);

    protected abstract T initConn() throws SocketException;

    public abstract void send2Nat(SimpleDemoProtocol p) throws IOException;

    public abstract void send2Predict(SimpleDemoProtocol p) throws IOException;

    public abstract void send(SimpleDemoProtocol p, InetSocketAddress remote) throws IOException;

}
