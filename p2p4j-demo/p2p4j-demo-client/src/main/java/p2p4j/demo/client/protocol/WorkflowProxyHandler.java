package p2p4j.demo.client.protocol;

import java.net.InetSocketAddress;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 28, 2018 8:32:47 PM
 * @version 0.0.1
 */
public class WorkflowProxyHandler extends ProtocolHandler {

    public WorkflowProxyHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.RSP_EXCHANGE_PEER_INFO };
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
