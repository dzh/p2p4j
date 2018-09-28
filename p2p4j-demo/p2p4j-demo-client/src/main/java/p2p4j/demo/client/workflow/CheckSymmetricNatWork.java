package p2p4j.demo.client.workflow;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.P2P4jDemoClient;
import p2p4j.demo.model.NatType;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * 发送请求到nat和predict服务器，看返回的外网端口是否相同，相同表示Cone，不同表示Symmetric
 * 
 * @author dzh
 * @date Sep 27, 2018 11:04:40 AM
 * @version 0.0.1
 */
public class CheckSymmetricNatWork extends P2PWorkflow<NatType> {
    static Logger LOG = LoggerFactory.getLogger(CheckSymmetricNatWork.class);

    // (id,outAddress)
    private Map<String, InetSocketAddress> checkSymm = Collections.synchronizedMap(new HashMap<String, InetSocketAddress>(2, 1));

    private CountDownLatch recvPeerLatch;

    public CheckSymmetricNatWork(P2P4jDemoClient client) {
        super(client);
    }

    @Override
    public NatType call() throws Exception {
        int retries = 0;
        while (true) {
            if (retries > 3) {
                setState(S_FAIL);
                LOG.error("CheckSymmetricNatWork overflow max-retries {}", 3);
                break;
            }

            checkSymm.clear();
            SimpleDemoProtocol p = SimpleDemoProtocol.create(ProtocolType.REQ_CHECK_SYMMETRIC);
            client().conn().send2Nat(p);
            checkSymm.put(p.getId(), null);

            p = SimpleDemoProtocol.create(ProtocolType.REQ_CHECK_SYMMETRIC);
            client().conn().send2Predict(p);
            checkSymm.put(p.getId(), null);

            recvPeerLatch = new CountDownLatch(2);
            if (recvPeerLatch.await(5000, TimeUnit.SECONDS)) {
                InetSocketAddress[] outAddrs = checkSymm.values().toArray(new InetSocketAddress[2]);
                InetSocketAddress outAddr1 = outAddrs[0];
                InetSocketAddress outAddr2 = outAddrs[1];
                if (outAddr1 != null && outAddr2 != null) {
                    this.setState(S_SUCC);

                    if (outAddr1.equals(outAddr2)) {
                        LOG.info("local nat is some cone");
                        return NatType.CONE;
                    } else {
                        LOG.info("local nat is symmetric");
                        return NatType.SYMMETRIC;
                    }
                }
            } else { // retry
                LOG.warn("SymmetricNatWork checked timeout! checkSymm: {}", checkSymm);
                ++retries;
            }
        }
        this.setState(S_FAIL);
        return NatType.UNKNOWN;
    }

    @Override
    public void signal(SimpleDemoProtocol p) {
        if (p.getType() == ProtocolType.RSP_CHECK_SYMMETRIC) {
            String id = p.getData().get(SimpleDemoProtocol.K_REQ_ID);
            if (checkSymm.containsKey(id)) {
                String ip = p.getData().get(SimpleDemoProtocol.K_IP);
                String port = p.getData().get(SimpleDemoProtocol.K_PORT);
                InetSocketAddress isa = new InetSocketAddress(ip, Integer.parseInt(port));
                checkSymm.put(id, isa);

                recvPeerLatch.countDown();
            }
        }
    }

    @Override
    public P2PWorkflow<?> next(Object value) {
        NatType natType = (NatType) value;
        client().setNatType(natType);

        if (natType == NatType.SYMMETRIC) {
            // return new ExchangePeerInfoWork(client());
            return null;
        }
        return new CheckConeNatWork(this.client());// 检查cone类型
    }

    @Override
    public void close() throws IOException {
        checkSymm.clear();
    }

}
