package p2p4j.demo.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jframe.core.plugin.DefPlugin;
import jframe.core.plugin.PluginException;

/**
 * @author dzh
 * @date Sep 25, 2018 1:02:23 AM
 * @version 0.0.1
 */
public class P2P4jDemoClientPlugin extends DefPlugin {

    static Logger LOG = LoggerFactory.getLogger(P2P4jDemoClientPlugin.class);

    private P2P4jDemoClient client;

    private ApiHttpServer httpServer;

    public void start() throws PluginException {
        super.start();

        httpServer = new ApiHttpServer(this);
        client = new P2P4jDemoClient(this);

        try {
            httpServer.startHttpServer();
            client.initConn();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    public P2P4jDemoClient client() {
        return this.client;
    }

    public void stop() throws PluginException {
        super.stop();

        try {
            httpServer.stopHttpServer();
            client.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
