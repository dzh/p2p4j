package p2p4j.demo.client.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

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
        return new int[] { ProtocolType.RSP_CALLBACK_PEER_INFO };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol,
     * java.net.InetSocketAddress)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remote) {
        SimpleDemoProtocol rsp = SimpleDemoProtocol.create(ProtocolType.REQ_TRAVERSAL_PING);
        rsp.setClientId(this.conn().client().getClientId());

        String ip = p.getData().get(SimpleDemoProtocol.K_IP);
        String port = p.getData().get(SimpleDemoProtocol.K_PORT);
        InetSocketAddress peerAddress = new InetSocketAddress(ip, Integer.parseInt(port));

        try {
            this.conn().send(rsp, peerAddress);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
