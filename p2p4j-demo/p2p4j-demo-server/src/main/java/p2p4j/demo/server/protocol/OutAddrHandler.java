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
 * @date Sep 27, 2018 10:16:36 AM
 * @version 0.0.1
 */
public class OutAddrHandler extends ProtocolHandler {

    static Logger LOG = LoggerFactory.getLogger(OutAddrHandler.class);

    public OutAddrHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.REQ_OUT_ADDR, ProtocolType.REQ_CHECK_SYMMETRIC };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remote) {
        SimpleDemoProtocol rsp = SimpleDemoProtocol.create(10000 + p.getType());
        Map<String, String> data = new HashMap<>();
        data.put(SimpleDemoProtocol.K_IP, remote.getAddress().getHostAddress());
        data.put(SimpleDemoProtocol.K_PORT, String.valueOf(remote.getPort()));
        data.put(SimpleDemoProtocol.K_REQ_ID, p.getId());
        rsp.setData(data);

        try {
            conn().send(rsp, remote);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
