package p2p4j.demo.client.protocol;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 28, 2018 2:48:44 PM
 * @version 0.0.1
 */
public class PingHandler extends ProtocolHandler {

    static Logger LOG = LoggerFactory.getLogger(PingHandler.class);

    public PingHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.RSP_PING };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol,
     * java.net.InetSocketAddress)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remoteAddr) {
        String ip = p.getData().get(SimpleDemoProtocol.K_IP);
        String port = p.getData().get(SimpleDemoProtocol.K_PORT);

        if (conn().client().getOutAddr() == null) {
            InetSocketAddress outAddr = new InetSocketAddress(ip, Integer.parseInt(port));
            conn().client().setOutAddr(outAddr);
        }
        LOG.info("{} -> {} {}", remoteAddr, ip, port);
    }

}
