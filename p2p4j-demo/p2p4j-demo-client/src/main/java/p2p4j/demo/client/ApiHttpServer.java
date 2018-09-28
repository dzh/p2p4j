package p2p4j.demo.client;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jframe.core.plugin.DefPlugin;
import p2p4j.demo.client.http.P2PResource;
import p2p4j.demo.client.http.TestResource;
import p2p4j.demo.model.P2P4jDemoConst;

/**
 * @author dzh
 * @date Jul 17, 2018 6:07:32 PM
 * @version 0.0.1
 */
class ApiHttpServer extends Application {

    static Logger LOG = LoggerFactory.getLogger(ApiHttpServer.class);

    DefPlugin plugin;

    private NettyJaxrsServer netty;

    ApiHttpServer(DefPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(TestResource.class);
        classes.add(P2PResource.class);
        return classes;
    }

    public void startHttpServer() {
        try {
            int port = Integer.parseInt(plugin.getConfig(P2P4jDemoConst.HTTP_PORT, "80"));
            String host = plugin.getConfig(P2P4jDemoConst.HTTP_HOST, "0.0.0.0");
            int bossCount = Integer.parseInt(plugin.getConfig(P2P4jDemoConst.HTTP_BOSS_THREADS, "-1"));
            bossCount = bossCount < 0 ? Runtime.getRuntime().availableProcessors() : bossCount;
            int workCount = Integer.parseInt(plugin.getConfig(P2P4jDemoConst.HTTP_WORK_THREADS, "10"));

            LOG.info("Starting http server, listen on {}:{}", host, port);
            netty = new NettyJaxrsServer();
            netty.setIoWorkerCount(bossCount);
            netty.setExecutorThreadCount(workCount);

            ResteasyDeployment deployment = new ResteasyDeployment();
            deployment.setProviderFactory(new ResteasyProviderFactory());
            // deployment.getProviderFactory().register(ResteasyJacksonProvider.class);
            deployment.setApplication(this);
            netty.setDeployment(deployment);
            netty.setHostname(host);
            netty.setPort(port);
            netty.setRootResourcePath(plugin.getConfig(P2P4jDemoConst.HTTP_ROOT, "/"));

            // netty.setSecurityDomain(null);
            // if (isHttpsEnabled()) {
            // SelfSignedCertificate ssc = new SelfSignedCertificate();
            // netty.setSSLContext(SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build());
            // }

            netty.start();
            LOG.info("Start http server successfully!");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * default value is false
     * 
     * @return
     */
    // private boolean isHttpsEnabled() {
    // try {
    // return Boolean.parseBoolean(plugin.getConfig(ApiConst.HTTPS_ENABLED, "false"));
    // } catch (Exception e) {
    // LOG.error(e.getMessage());
    // }
    // return false;
    // }

    public void stopHttpServer() {
        if (netty != null) {
            netty.stop();
        }
        LOG.info("Stop httpserver successfully!");
    }

}
