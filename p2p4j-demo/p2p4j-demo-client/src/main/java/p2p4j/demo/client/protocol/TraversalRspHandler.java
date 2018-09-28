package p2p4j.demo.client.protocol;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 28, 2018 10:35:33 PM
 * @version 0.0.1
 */
public class TraversalRspHandler extends ProtocolHandler {

    static Logger LOG = LoggerFactory.getLogger(TraversalRspHandler.class);

    public TraversalRspHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.RSP_TRAVERSAL_ADD };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol,
     * java.net.InetSocketAddress)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remoteAddr) {
        int type = p.getType();
        if (type == ProtocolType.RSP_TRAVERSAL_ADD) {
            String a = p.getData().get(SimpleDemoProtocol.K_ADD_A);
            String b = p.getData().get(SimpleDemoProtocol.K_ADD_B);
            String r = p.getData().get(SimpleDemoProtocol.K_ADD_R);
            LOG.info("{} calc {} + {} = {}", p.getClientId(), a, b, r);
        }

    }

}
