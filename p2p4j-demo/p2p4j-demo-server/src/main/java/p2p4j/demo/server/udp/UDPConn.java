package p2p4j.demo.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.model.SimpleDemoProtocol;
import p2p4j.demo.server.InnerConn;
import p2p4j.demo.server.P2P4jDemoServer;

/**
 * @author dzh
 * @date Sep 25, 2018 5:16:58 PM
 * @version 0.0.1
 */
public class UDPConn extends InnerConn<DatagramSocket> {

    static Logger LOG = LoggerFactory.getLogger(UDPConn.class);

    public UDPConn(P2P4jDemoServer server) throws SocketException {
        super(server);
    }

    @Override
    protected DatagramSocket initConn(int port) throws SocketException {
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
        socket.bind(new InetSocketAddress(port));
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
    public void send(SimpleDemoProtocol p, InetSocketAddress remote) throws IOException {
        LOG.info("{} -> {}", p, remote);
        byte[] data = p.encode();
        if (data.length > SimpleDemoProtocol.MAX_SIZE) {
            LOG.error("overflow max-size {}", data);
            return;
        }
        DatagramPacket dp = new DatagramPacket(data, data.length, remote);
        socket().send(dp);
    }

    @Override
    public void sendShadow(SimpleDemoProtocol p, InetSocketAddress remote) throws IOException {
        byte[] data = p.encode();
        if (data.length > SimpleDemoProtocol.MAX_SIZE) {
            LOG.error("overflow max-size {}", data);
            return;
        }
        DatagramPacket dp = new DatagramPacket(data, data.length, remote);
        shadowSocket().send(dp);
    }

}
