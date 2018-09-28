package p2p4j.demo.server.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.model.NatType;
import p2p4j.demo.model.PeerInfo;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;
import p2p4j.demo.server.InnerConn;

/**
 * @author dzh
 * @date Sep 28, 2018 8:41:13 PM
 * @version 0.0.1
 */
public class ExchangePeerInfoHandler extends ProtocolHandler {

    static Logger LOG = LoggerFactory.getLogger(ExchangePeerInfoHandler.class);

    public ExchangePeerInfoHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.REQ_EXCHANGE_PEER_INFO };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol,
     * java.net.InetSocketAddress)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remote) {
        String peerId = p.getData().get(SimpleDemoProtocol.K_CLIENT_ID);
        PeerInfo peerInfo = conn().server().peerInfo().get(peerId);
        if (peerInfo != null && peerInfo.getNatType().getType() != NatType.UNKNOWN.getType()) {

            SimpleDemoProtocol rsp = SimpleDemoProtocol.create(10000 + p.getType());
            Map<String, String> data = new HashMap<>();
            data.put(SimpleDemoProtocol.K_CLIENT_ID, peerId);
            data.put(SimpleDemoProtocol.K_IP, peerInfo.getOutAddr().getAddress().getHostAddress());
            data.put(SimpleDemoProtocol.K_PORT, String.valueOf(peerInfo.getOutAddr().getPort()));
            data.put(SimpleDemoProtocol.K_NAT_TYPE, String.valueOf(peerInfo.getNatType().getType()));
            data.put(SimpleDemoProtocol.K_REQ_ID, p.getId());
            rsp.setData(data);

            try {
                conn().send(rsp, remote);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

}
