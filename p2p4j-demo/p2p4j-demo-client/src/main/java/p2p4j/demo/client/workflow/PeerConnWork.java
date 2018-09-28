package p2p4j.demo.client.workflow;

import p2p4j.demo.client.P2P4jDemoClient;
import p2p4j.demo.client.PeerConn;

/**
 * @author dzh
 * @date Sep 27, 2018 11:05:53 AM
 * @version 0.0.1
 */
public abstract class PeerConnWork extends P2PWorkflow<PeerConn> {

    public PeerConnWork(P2P4jDemoClient client) {
        super(client);
    }

}
