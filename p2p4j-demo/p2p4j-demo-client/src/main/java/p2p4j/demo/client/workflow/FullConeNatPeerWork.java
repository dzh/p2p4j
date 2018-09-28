package p2p4j.demo.client.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import p2p4j.demo.client.P2P4jDemoClient;
import p2p4j.demo.client.PeerConn;
import p2p4j.demo.model.PeerInfo;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 27, 2018 3:34:39 PM
 * @version 0.0.1
 */
public class FullConeNatPeerWork extends PeerConnWork {

    private PeerInfo fullCone;

    public FullConeNatPeerWork(P2P4jDemoClient client) {
        super(client);
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public PeerConn call() throws Exception {
        int retries = 0;
        while (true) {
            boolean closed = this.client().isClosed();
            if (closed) {
                break;
            }
            if (retries > 10) {
                setState(S_SUCC);
                LOG.error("FullConeNatPeerWork overflow max-retries {}", 10);
                break;
            }

            // 发送获取连接请求
            SimpleDemoProtocol p = SimpleDemoProtocol.create(ProtocolType.REQ_TRAVERSAL_ADD);
            p.setClientId(client().getClientId());
            p.setNatType(client().getNatType().getType());

            Map<String, String> data = new HashMap<>();
            data.put(SimpleDemoProtocol.K_ADD_A, String.valueOf(ThreadLocalRandom.current().nextInt(1, 100)));
            data.put(SimpleDemoProtocol.K_ADD_B, String.valueOf(ThreadLocalRandom.current().nextInt(1, 100)));
            p.setData(data);
            client().conn().send(p, fullCone.getOutAddr());

            Thread.sleep(3000);
            ++retries;
        }
        // this.setState(S_FAIL);
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {

    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.workflow.P2PWorkflow#signal(p2p4j.demo.model.SimpleDemoProtocol)
     */
    @Override
    public void signal(SimpleDemoProtocol p) {

    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.workflow.P2PWorkflow#next(java.lang.Object)
     */
    @Override
    public P2PWorkflow<?> next(Object value) {
        return null;
    }

    public PeerInfo getFullCone() {
        return fullCone;
    }

    public void setFullCone(PeerInfo fullCone) {
        this.fullCone = fullCone;
    }

}
