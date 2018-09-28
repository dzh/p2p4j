package p2p4j.demo.client.workflow;

import java.io.IOException;

import p2p4j.demo.client.P2P4jDemoClient;
import p2p4j.demo.client.PeerConn;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 27, 2018 3:37:07 PM
 * @version 0.0.1
 */
public class SymmetricNatPeerWork extends PeerConnWork {

    public SymmetricNatPeerWork(P2P4jDemoClient client) {
        super(client);
    }

    @Override
    public PeerConn call() throws Exception {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void signal(SimpleDemoProtocol p) {

    }

    @Override
    public P2PWorkflow<?> next(Object value) {
        return null;
    }

}
