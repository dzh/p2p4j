package p2p4j.demo.client.workflow;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import p2p4j.demo.client.P2P4jDemoClient;
import p2p4j.demo.model.NatType;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 27, 2018 3:28:59 PM
 * @version 0.0.1
 */
public class CheckConeNatWork extends P2PWorkflow<NatType> {

    private Set<Integer> rspTypes = new HashSet<>(); // 接收到的响应类型

    public CheckConeNatWork(P2P4jDemoClient client) {
        super(client);
    }

    @Override
    public NatType call() throws Exception {
        int retries = 0;
        while (true) {
            if (retries > 3) {
                this.setState(S_FAIL);
                LOG.error("CheckSymmetricNatWork overflow max-retries {}", 3);
                break;
            }

            // 1
            SimpleDemoProtocol p = SimpleDemoProtocol.create(ProtocolType.REQ_CHECK_CONE);
            client().conn().send2Nat(p);
            Thread.sleep(1000); // TODO 防止可能的丢包概率
            // 2
            p = SimpleDemoProtocol.create(ProtocolType.REQ_CHECK_CONE);
            client().conn().send2Nat(p);

            Thread.sleep(5000); // TODO

            int size = rspTypes.size();
            if (size > 0) {
                if (rspTypes.contains(ProtocolType.RSP_CHECK_CONE_PARTNER)) {
                    return NatType.FULL_CONE;
                } else if (rspTypes.contains(ProtocolType.RSP_CHECK_CONE_SHADOW)) {
                    return NatType.RESTRICTED_CONE;
                } else {
                    return NatType.PORT_RESTRICTED_CONE;
                }
            }

            ++retries;
        }
        // TODO
        return NatType.CONE;
    }

    @Override
    public void signal(SimpleDemoProtocol p) {
        rspTypes.add(p.getType());
    }

    @Override
    public P2PWorkflow<?> next(Object value) {
        NatType natType = (NatType) value;

        client().setNatType(natType);
        return new ExchangePeerInfoWork(client());
    }

    @Override
    public void close() throws IOException {
        rspTypes.clear();
    }

}
