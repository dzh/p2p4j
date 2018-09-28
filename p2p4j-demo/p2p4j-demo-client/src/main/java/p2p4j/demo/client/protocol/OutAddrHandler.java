package p2p4j.demo.client.protocol;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 27, 2018 9:46:41 AM
 * @version 0.0.1
 */
public class OutAddrHandler extends ProtocolHandler {

    static Logger LOG = LoggerFactory.getLogger(OutAddrHandler.class);

    public OutAddrHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.RSP_OUT_ADDR };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remoteAddr) {
        String ip = p.getData().get(SimpleDemoProtocol.K_IP);
        String port = p.getData().get(SimpleDemoProtocol.K_PORT);
        InetSocketAddress outAddr = new InetSocketAddress(ip, Integer.parseInt(port));
        conn().client().setOutAddr(outAddr);
        LOG.info("{} -> outAddr {}", remoteAddr, outAddr);
    }

}
