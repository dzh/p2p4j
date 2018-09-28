package p2p4j.demo.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.model.P2P4jDemoConst;
import p2p4j.demo.model.SimpleDemoProtocol;
import p2p4j.demo.server.protocol.CheckConeTypeHandler;
import p2p4j.demo.server.protocol.OutAddrHandler;
import p2p4j.demo.server.protocol.PingHandler;
import p2p4j.demo.server.protocol.ProtocolHandler;

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

    private P2P4jDemoServer server;

    protected volatile int connState;

    protected InetSocketAddress partnerAddr;

    private T socket;

    private T shadowSocket;

    protected InnerConn(P2P4jDemoServer server) throws SocketException {
        this.server = server;

        partnerAddr = parseSocketAddress(getConnConf(P2P4jDemoConst.P2P4J_SERVER_PARTNER));

        //
        String port = getConnConf(P2P4jDemoConst.P2P4J_SERVER_UDP_PORT).trim();
        socket = initConn(Integer.parseInt(port));
        connState = CONN_STATE_SUCC;

        // 辅助检测NAT的socket
        port = getConnConf(P2P4jDemoConst.P2P4J_SERVER_SHADOW_UDP_PORT).trim();
        shadowSocket = initConn(Integer.parseInt(port));

        // 注册协议处理
        registerProtocolHandler();
        // 启动接收数据包线程
        startReceiveThread();

        LOG.info("NATConnection connState {}", connState);
    }

    private List<ProtocolHandler> pHandlers;

    private void registerProtocolHandler() {
        pHandlers = new LinkedList<>();
        pHandlers.add(new PingHandler(this));
        pHandlers.add(new OutAddrHandler(this));
        pHandlers.add(new CheckConeTypeHandler(this));
    }

    private InetSocketAddress parseSocketAddress(String addr) {
        if (addr == null || "".equals(addr)) return null;

        String[] ipPort = addr.split(":");
        return new InetSocketAddress(ipPort[0], Integer.parseInt(ipPort[1]));
    }

    // 接收的数据包
    public void receiveProtocol(SimpleDemoProtocol p, InetSocketAddress remote) {
        LOG.info("{} -> {}", remote, p);
        for (ProtocolHandler ph : pHandlers) {
            if (ph.accept(p)) {
                ph.handle(p, remote);
            }
        }
    }

    public P2P4jDemoServer server() {
        return this.server;
    }

    public String getConnConf(String key) {
        return server.serverConf(key);
    }

    /*
     * (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
        if (socket != null) socket.close();
        if (shadowSocket != null) shadowSocket.close();
        connState = CONN_STATE_CLOSED;
        LOG.info("NATConnection connState {}", connState);
    }

    public T socket() {
        return this.socket;
    }

    public T shadowSocket() {
        return this.shadowSocket;
    }

    protected abstract void startReceiveThread();

    protected abstract T initConn(int port) throws SocketException;

    public abstract void send(SimpleDemoProtocol p, InetSocketAddress remote) throws IOException;

    public abstract void sendShadow(SimpleDemoProtocol p, InetSocketAddress remote) throws IOException;

    public void sendPartner(SimpleDemoProtocol p) {
        try {
            send(p, partnerAddr);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e); // TODO
        }
    }

}
