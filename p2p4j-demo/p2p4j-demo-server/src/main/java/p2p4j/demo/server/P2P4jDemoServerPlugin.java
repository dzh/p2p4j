package p2p4j.demo.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jframe.core.plugin.DefPlugin;
import jframe.core.plugin.PluginException;

/**
 * @author dzh
 * @date Sep 25, 2018 1:00:59 AM
 * @version 0.0.1
 */
public class P2P4jDemoServerPlugin extends DefPlugin {

    static Logger LOG = LoggerFactory.getLogger(P2P4jDemoServerPlugin.class);

    private P2P4jDemoServer server;

    public void start() throws PluginException {
        super.start();

        server = new P2P4jDemoServer(this);

        try {
            server.initConn();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    public P2P4jDemoServer server() {
        return this.server;
    }

    public void stop() throws PluginException {
        super.stop();

        try {
            server.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
