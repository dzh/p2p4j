package p2p4j.demo.server;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.server.udp.UDPConn;

/**
 * @author dzh
 * @date Sep 27, 2018 9:30:15 AM
 * @version 0.0.1
 */
public class P2P4jDemoServer implements Closeable {

    static Logger LOG = LoggerFactory.getLogger(P2P4jDemoServer.class);

    private P2P4jDemoServerPlugin plugin;

    private InnerConn<?> conn;

    public P2P4jDemoServer(P2P4jDemoServerPlugin plugin) {
        this.plugin = plugin;
    }

    public P2P4jDemoServerPlugin plugin() {
        return plugin;
    }

    public String serverConf(String key) {
        return plugin.getConfig(key);
    }

    @Override
    public void close() throws IOException {
        if (conn != null) conn.close();
    }

    public void initConn() throws IOException {
        conn = new UDPConn(this);
    }

}
