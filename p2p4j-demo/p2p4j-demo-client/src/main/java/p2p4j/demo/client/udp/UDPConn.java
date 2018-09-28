package p2p4j.demo.client.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.client.P2P4jDemoClient;
import p2p4j.demo.model.P2P4jDemoConst;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 25, 2018 5:16:58 PM
 * @version 0.0.1
 */
public class UDPConn extends InnerConn<DatagramSocket> {

    static Logger LOG = LoggerFactory.getLogger(UDPConn.class);

    public UDPConn(P2P4jDemoClient client) throws SocketException {
        super(client);
    }

    @Override
    protected DatagramSocket initConn() throws SocketException {
        String udpPort = getConnConf(P2P4jDemoConst.P2P4J_CLIENT_NAT_UDP).trim();

        DatagramSocket socket = new DatagramSocket(null);
        socket.setSoTimeout(30 * 1000); // 30s
        socket.setSendBufferSize(SimpleDemoProtocol.MAX_SIZE);
        socket.setReceiveBufferSize(SimpleDemoProtocol.MAX_SIZE);

        // if (localAddr.indexOf(":") > 0) {
        // String[] ipPort = localAddr.split(":");
        // socket.bind(new InetSocketAddress(ipPort[0], Integer.parseInt(ipPort[1])));
        // } else {
        // socket.bind(new InetSocketAddress(Integer.parseInt(localAddr)));
        // }
        socket.bind(new InetSocketAddress(Integer.parseInt(udpPort)));
        return socket;
    }

    @Override
    protected void startReceiveThread() {
        Thread recvT = new Thread(() -> {
            while (true) {
                int connState = UDPConn.this.connState;
                if (connState == CONN_STATE_CLOSED) break;

                // while (connState != CONN_STATE_SUCC) {
                // try {
                // Thread.sleep(5000);
                // } catch (InterruptedException e) {
                // break;
                // }
                // }
                // if (connState == CONN_STATE_SUCC) {
                try {
                    int bufLen = SimpleDemoProtocol.MAX_SIZE;
                    DatagramPacket p = new DatagramPacket(new byte[bufLen], bufLen);
                    socket().receive(p);

                    String json = new String(p.getData(), 0, p.getLength(), SimpleDemoProtocol.ENCODING);
                    receiveProtocol(SimpleDemoProtocol.decode(json), (InetSocketAddress) p.getSocketAddress());
                } catch (SocketTimeoutException e) {
                    // ignore receive timeout
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
                // }
            }
            LOG.info("UDPConn startReceiveThread stopped!");
        }, "UDPConn-startReceiveThread");
        recvT.setDaemon(true);
        recvT.start();
    }

    @Override
    protected boolean connServer(String natServer) {
        if (natServer == null || "".equals(natServer)) return false;
        natServer = natServer.trim();
        String[] ipPort = natServer.split(":"); // TODO
        try {
            socket().connect(new InetSocketAddress(ipPort[0], Integer.parseInt(ipPort[1])));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public void send2Nat(SimpleDemoProtocol p) throws IOException {
        send(p, serverNat);
    }

    @Override
    public void send2Predict(SimpleDemoProtocol p) throws IOException {
        send(p, serverPredict);
    }

    @Override
    public void send(SimpleDemoProtocol p, InetSocketAddress remote) throws IOException {
        byte[] data = p.encode();
        if (data.length > SimpleDemoProtocol.MAX_SIZE) {
            LOG.error("overflow max-size {}", data);
            return;
        }
        LOG.info("{} -> {}", p, remote);
        DatagramPacket dp = new DatagramPacket(data, data.length, remote);
        socket().send(dp);
    }

    @Override
    protected void startPingThread() {
        Thread recvT = new Thread(() -> {
            while (true) {
                int connState = UDPConn.this.connState;
                if (connState == CONN_STATE_CLOSED) break;

                SimpleDemoProtocol p = SimpleDemoProtocol.create(ProtocolType.REQ_PING);
                try {
                    UDPConn.this.send2Nat(p);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {}
            }
            LOG.info("UDPConn startReceiveThread stopped!");
        }, "UDPConn-startPingThread");
        recvT.setDaemon(true);
        recvT.start();
    }

}
