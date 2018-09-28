package p2p4j.demo.client.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 28, 2018 10:33:02 PM
 * @version 0.0.1
 */
public class TraversalReqHandler extends ProtocolHandler {

    static Logger LOG = LoggerFactory.getLogger(TraversalReqHandler.class);

    public TraversalReqHandler(InnerConn<?> conn) {
        super(conn);
    }

    @Override
    public int[] protocolTypes() {
        return new int[] { ProtocolType.REQ_TRAVERSAL_ADD };
    }

    @Override
    public void handle(SimpleDemoProtocol p, InetSocketAddress remoteAddr) {
        if (p.getType() == ProtocolType.REQ_TRAVERSAL_ADD) {
            String a = p.getData().get(SimpleDemoProtocol.K_ADD_A);
            String b = p.getData().get(SimpleDemoProtocol.K_ADD_B);

            SimpleDemoProtocol rsp = SimpleDemoProtocol.create(ProtocolType.RSP_TRAVERSAL_ADD);
            rsp.setClientId(conn().client().getClientId());
            rsp.setNatType(conn().client().getNatType().getType());

            Map<String, String> data = new HashMap<String, String>();
            data.put(SimpleDemoProtocol.K_ADD_R, String.valueOf(Integer.parseInt(a) + Integer.parseInt(b)));
            data.put(SimpleDemoProtocol.K_ADD_A, a);
            data.put(SimpleDemoProtocol.K_ADD_B, b);

            rsp.setData(data);

            try {
                conn().send(rsp, remoteAddr);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

}
