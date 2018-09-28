package p2p4j.demo.model;

import java.net.InetSocketAddress;

/**
 * @author dzh
 * @date Sep 28, 2018 7:18:30 PM
 * @version 0.0.1
 */
public class PeerInfo {

    private String clientId;
    private InetSocketAddress outAddr; // 外网出口地址和端口
    private NatType natType;

    public InetSocketAddress getOutAddr() {
        return outAddr;
    }

    public void setOutAddr(InetSocketAddress outAddr) {
        this.outAddr = outAddr;
    }

    public NatType getNatType() {
        return natType;
    }

    public void setNatType(NatType natType) {
        this.natType = natType;
    }

    @Override
    public String toString() {
        return "outAddr is" + outAddr + " and natType is" + natType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
