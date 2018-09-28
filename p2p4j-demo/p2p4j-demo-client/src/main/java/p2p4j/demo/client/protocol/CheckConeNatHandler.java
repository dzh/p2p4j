package p2p4j.demo.client.protocol;

import java.net.InetSocketAddress;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 27, 2018 5:04:20 PM
 * @version 0.0.1
 */
public class CheckConeNatHandler extends ProtocolHandler {

    public CheckConeNatHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.RSP_CHECK_CONE, ProtocolType.RSP_CHECK_CONE_SHADOW, ProtocolType.RSP_CHECK_CONE_PARTNER };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol,
     * java.net.InetSocketAddress)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remoteAddr) {
        conn().client().signalWork(p);
    }

}
