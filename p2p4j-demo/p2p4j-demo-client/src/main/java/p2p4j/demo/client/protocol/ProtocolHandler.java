package p2p4j.demo.client.protocol;

import java.net.InetSocketAddress;

import p2p4j.demo.client.InnerConn;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * @author dzh
 * @date Sep 26, 2018 6:58:35 PM
 * @version 0.0.1
 */
public abstract class ProtocolHandler {

    public abstract int[] protocolTypes(); // 支持的协议类型

    private InnerConn<?> conn;

    public ProtocolHandler(InnerConn<?> conn) {
        this.conn = conn;
    }

    protected InnerConn<?> conn() {
        return this.conn;
    }

    public boolean accept(SimpleDemoProtocol p) {
        int[] types = protocolTypes();
        for (int t : types) { // TODO
            if (t == p.getType()) return true;
        }
        return false;
    }

    public abstract void handle(SimpleDemoProtocol p, InetSocketAddress remoteAddr);

}
