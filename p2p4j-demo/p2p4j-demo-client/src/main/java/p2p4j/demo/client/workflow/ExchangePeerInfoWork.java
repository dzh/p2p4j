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
import p2p4j.demo.model.PeerInfo;
import p2p4j.demo.model.ProtocolType;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * 相同token的组成一对,获取对方的nat类型, 获取不到时一直重试打印日志
 * 
 * @author dzh
 * @date Sep 27, 2018 3:53:46 PM
 * @version 0.0.1
 */
public class ExchangePeerInfoWork extends P2PWorkflow<PeerInfo> {
    static Logger LOG = LoggerFactory.getLogger(ExchangePeerInfoWork.class);

    private PeerInfo peerInfo;

    private CountDownLatch recvPeerLatch;

    private String peerId; // peer's clientId

    public ExchangePeerInfoWork(P2P4jDemoClient client) {
        super(client);
    }

    @Override
    public PeerInfo call() throws Exception {
        int retries = 0;
        while (true) {
            if (retries > 10) {
                setState(S_FAIL);
                LOG.error("ExchangePeerInfoWork overflow max-retries {}", 10);
                break;
            }

            boolean closed = this.client().isClosed();
            if (closed) {
                LOG.warn("failed to exchange {}", peerId);
                break;
            }

            // 发送获取连接请求
            SimpleDemoProtocol p = SimpleDemoProtocol.create(ProtocolType.REQ_EXCHANGE_PEER_INFO);
            p.setClientId(client().getClientId());
            p.setNatType(client().getNatType().getType());
            Map<String, String> data = new HashMap<>();
            data.put(SimpleDemoProtocol.K_CLIENT_ID, peerId);
            p.setData(data);
            client().conn().send2Nat(p);

            recvPeerLatch = new CountDownLatch(1);
            if (recvPeerLatch.await(5, TimeUnit.SECONDS)) {
                this.setState(S_SUCC);
                return peerInfo;
            } else {
                LOG.info("wait to exchange");
            }
            ++retries;
        }
        this.setState(S_FAIL);
        return null;
    }

    public String getPeerId() {
        return peerId;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void signal(SimpleDemoProtocol p) {
        if (p.getType() == ProtocolType.RSP_EXCHANGE_PEER_INFO) {
            Map<String, String> data = p.getData();
            String peerIp = data.get(SimpleDemoProtocol.K_IP);
            String peerPort = data.get(SimpleDemoProtocol.K_PORT);
            String peerNatType = data.get(SimpleDemoProtocol.K_NAT_TYPE);
            String peerId = data.get(SimpleDemoProtocol.K_CLIENT_ID); // peerId

            peerInfo = new PeerInfo();
            peerInfo.setNatType(NatType.find(Integer.parseInt(peerNatType)));
            peerInfo.setOutAddr(new InetSocketAddress(peerIp, Integer.parseInt(peerPort)));
            peerInfo.setClientId(peerId);
            recvPeerLatch.countDown();
        }
    }

    @Override
    public P2PWorkflow<?> next(Object value) {
        if (peerInfo.getNatType().getType() == NatType.FULL_CONE.getType()) {
            FullConeNatPeerWork workFlow = new FullConeNatPeerWork(this.client());
            workFlow.setFullCone(peerInfo);

            return workFlow;
        }
        // TODO

        return null;
    }

}
