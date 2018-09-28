package p2p4j.demo.client.workflow;

import java.io.IOException;
import java.net.InetSocketAddress;
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
 * 相同token的组成一对,获取对方的nat类型, 获取不到时一直重试打印日志
 * 
 * @author dzh
 * @date Sep 27, 2018 3:53:46 PM
 * @version 0.0.1
 */
public class ExchangePeerInfoWork extends P2PWorkflow<InetSocketAddress> {
    static Logger LOG = LoggerFactory.getLogger(ExchangePeerInfoWork.class);

    private InetSocketAddress peerOutAddr;
    private NatType peerNatType = NatType.UNKNOWN;

    private CountDownLatch recvPeerLatch;

    public ExchangePeerInfoWork(P2P4jDemoClient client) {
        super(client);
    }

    @Override
    public InetSocketAddress call() throws Exception {
        while (true) {
            // 发送获取连接请求
            String token = client().getToken();
            SimpleDemoProtocol p = SimpleDemoProtocol.create(ProtocolType.REQ_EXCHANGE_PEER_INFO);
            Map<String, String> data = new HashMap<>();
            data.put(SimpleDemoProtocol.K_TOKEN, token);
            p.setData(data);
            client().conn().send2Nat(p);

            recvPeerLatch = new CountDownLatch(1);
            recvPeerLatch.await(5, TimeUnit.SECONDS);

            if (peerOutAddr != null) {
                this.setState(S_SUCC);
                return peerOutAddr;
            } else {
                LOG.info("token {} wait to receive peer info...", token);
            }

        }
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void signal(SimpleDemoProtocol p) {
        Map<String, String> data = p.getData();
        String peerIp = data.get(SimpleDemoProtocol.K_IP);
        String peerPort = data.get(SimpleDemoProtocol.K_PORT);
        String peerNatType = data.get(SimpleDemoProtocol.K_NAT_TYPE);

        this.peerOutAddr = new InetSocketAddress(peerIp, Integer.parseInt(peerPort));
        this.peerNatType = NatType.find(Integer.parseInt(peerNatType));
        recvPeerLatch.countDown();
    }

    @Override
    public P2PWorkflow<?> next(Object value) {
        // InetSocketAddress TODO
        return null;
    }

}
