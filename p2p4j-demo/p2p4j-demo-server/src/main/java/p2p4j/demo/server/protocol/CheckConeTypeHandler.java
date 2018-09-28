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
 * @date Sep 27, 2018 7:41:07 PM
 * @version 0.0.1
 */
public class CheckConeTypeHandler extends ProtocolHandler {

    static Logger LOG = LoggerFactory.getLogger(CheckConeTypeHandler.class);

    public CheckConeTypeHandler(InnerConn<?> conn) {
        super(conn);
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#protocolTypes()
     */
    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.REQ_CHECK_CONE, ProtocolType.REQ_CHECK_CONE_PARTNER, ProtocolType.REQ_CHECK_CONE_SHADOW };
    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.server.protocol.ProtocolHandler#handle(p2p4j.demo.model.SimpleDemoProtocol,
     * java.net.InetSocketAddress)
     */
    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remote) {
        int type = p.getType();
        switch (type) {
        case ProtocolType.REQ_CHECK_CONE: {
            response(p, remote, ProtocolType.RSP_CHECK_CONE);
            // shadow
            SimpleDemoProtocol req = p.fork(ProtocolType.REQ_CHECK_CONE_SHADOW);
            conn().receiveProtocol(req, remote);
            // partner
            req = p.fork(ProtocolType.REQ_CHECK_CONE_PARTNER);
            Map<String, String> data = new HashMap<>();
            data.put(SimpleDemoProtocol.K_IP, remote.getAddress().getHostAddress());
            data.put(SimpleDemoProtocol.K_PORT, String.valueOf(remote.getPort()));
            req.setData(data);
            conn().sendPartner(p);
            break;
        }
        case ProtocolType.REQ_CHECK_CONE_SHADOW: {
            SimpleDemoProtocol rsp = SimpleDemoProtocol.create(ProtocolType.RSP_CHECK_CONE_SHADOW);
            Map<String, String> data = new HashMap<>();
            data.put(SimpleDemoProtocol.K_REQ_ID, p.getId());
            rsp.setData(data);
            try {
                conn().sendShadow(rsp, remote);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            break;
        }
        case ProtocolType.REQ_CHECK_CONE_PARTNER: {
            SimpleDemoProtocol rsp = SimpleDemoProtocol.create(ProtocolType.RSP_CHECK_CONE_PARTNER);
            Map<String, String> data = new HashMap<>();
            data.put(SimpleDemoProtocol.K_REQ_ID, p.getId());
            rsp.setData(data);

            String remoteIp = p.getData().get(SimpleDemoProtocol.K_IP);
            String remotePort = p.getData().get(SimpleDemoProtocol.K_PORT);
            try {
                conn().send(rsp, new InetSocketAddress(remoteIp, Integer.parseInt(remotePort)));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        }

    }

    private void response(SimpleDemoProtocol p, InetSocketAddress remote, int type) {
        SimpleDemoProtocol rsp = SimpleDemoProtocol.create(type);
        Map<String, String> data = new HashMap<>();
        data.put(SimpleDemoProtocol.K_REQ_ID, p.getId());
        rsp.setData(data);
        try {
            conn().send(rsp, remote);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
