package p2p4j.demo.server.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;
import p2p4j.demo.server.InnerConn;

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
        return new int[] { ProtocolType.REQ_PING };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol,
     * java.net.InetSocketAddress)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remoteAddr) {
        SimpleDemoProtocol rsp = SimpleDemoProtocol.create(ProtocolType.RSP_PING);
        Map<String, String> data = new HashMap<>();
        data.put(SimpleDemoProtocol.K_IP, remoteAddr.getAddress().getHostAddress());
        data.put(SimpleDemoProtocol.K_PORT, String.valueOf(remoteAddr.getPort()));
        data.put(SimpleDemoProtocol.K_REQ_ID, p.getId());
        rsp.setData(data);

        try {
            conn().send(rsp, remoteAddr);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
