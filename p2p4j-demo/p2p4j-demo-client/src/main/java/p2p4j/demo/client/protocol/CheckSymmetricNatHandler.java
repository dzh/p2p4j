package p2p4j.demo.client.protocol;

import java.net.InetSocketAddress;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 27, 2018 2:14:41 PM
 * @version 0.0.1
 */
public class CheckSymmetricNatHandler extends ProtocolHandler {

    public CheckSymmetricNatHandler(InnerConn<?> conn) {
        super(conn);
    }

    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.RSP_CHECK_SYMMETRIC };
    }

    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remoteAddr) {
        conn().client().signalWork(p);
    }

}
