package p2p4j.demo.client.workflow;

import java.io.IOException;

import p2p4j.demo.client.P2P4jDemoClient;
import p2p4j.demo.client.PeerConn;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 27, 2018 3:34:39 PM
 * @version 0.0.1
 */
public class FullConeNatPeerWork extends PeerConnWork {

    public FullConeNatPeerWork(P2P4jDemoClient client) {
        super(client);
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public PeerConn call() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.workflow.P2PWorkflow#signal(p2p4j.demo.model.SimpleDemoProtocol)
     */
    @Override
    public void signal(SimpleDemoProtocol p) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see p2p4j.demo.client.workflow.P2PWorkflow#next(java.lang.Object)
     */
    @Override
    public P2PWorkflow<?> next(Object value) {
        // TODO Auto-generated method stub
        return null;
    }

}
