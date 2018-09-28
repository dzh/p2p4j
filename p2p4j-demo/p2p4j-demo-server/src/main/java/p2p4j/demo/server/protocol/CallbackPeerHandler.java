package p2p4j.demo.server.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.model.PeerInfo;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;
import p2p4j.demo.server.InnerConn;

/**
 * @author dzh
 * @date Sep 28, 2018 11:50:00 PM
 * @version 0.0.1
 */
public class CallbackPeerHandler extends ProtocolHandler {

    static Logger LOG = LoggerFactory.getLogger(CallbackPeerHandler.class);

    public CallbackPeerHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.REQ_CALLBACK_PEER_INFO };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol,
     * java.net.InetSocketAddress)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remote) {
        String clientId = p.getClientId();
        PeerInfo clientInfo = this.conn().server().peerInfo().get(clientId);

        SimpleDemoProtocol rsp = SimpleDemoProtocol.create(ProtocolType.RSP_CALLBACK_PEER_INFO);
        Map<String, String> data = new HashMap<>();
        data.put(SimpleDemoProtocol.K_CLIENT_ID, clientId);
        data.put(SimpleDemoProtocol.K_IP, clientInfo.getOutAddr().getAddress().getHostAddress());
        data.put(SimpleDemoProtocol.K_PORT, String.valueOf(clientInfo.getOutAddr().getPort()));
        data.put(SimpleDemoProtocol.K_NAT_TYPE, String.valueOf(clientInfo.getNatType().getType()));
        data.put(SimpleDemoProtocol.K_REQ_ID, p.getId());
        rsp.setData(data);

        String peerId = p.getData().get(SimpleDemoProtocol.K_CLIENT_ID);
        PeerInfo peerInfo = this.conn().server().peerInfo().get(peerId);
        try {
            this.conn().send(rsp, peerInfo.getOutAddr());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
