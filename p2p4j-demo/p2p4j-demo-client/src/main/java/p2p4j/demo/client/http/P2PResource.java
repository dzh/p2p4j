package p2p4j.demo.client.http;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jframe.core.plugin.annotation.InjectPlugin;
import jframe.core.plugin.annotation.Injector;
import p2p4j.demo.client.P2P4jDemoClientPlugin;
import p2p4j.demo.model.Result;

/**
 * @author dzh
 * @date Sep 28, 2018 1:51:34 PM
 * @version 0.0.1
 */
@Injector
@Path("p2p")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class P2PResource {

    static Logger LOG = LoggerFactory.getLogger(TestResource.class);

    @InjectPlugin
    static P2P4jDemoClientPlugin Plugin;

    /**
     * 查看本地的NAT信息
     * 
     * @param ui
     * @return
     */
    @GET
    @Path("info/local")
    public Result<Map<String, Object>> infoLocal(@Context UriInfo ui) {
        LOG.info("{}", ui.getPath());

        Map<String, Object> data = new HashMap<>();
        data.put("natType", Plugin.client().getNatType());
        data.put("outAddr", Plugin.client().getOutAddr());

        Result<Map<String, Object>> r = new Result<>();
        r.setData(data);

        return r;
    }

    @GET
    @Path("peer/{clientId}")
    public Result<Map<String, Object>> peerInfo(@Context UriInfo ui, @PathParam("clientId") String clientId) {
        LOG.info("{}", ui.getPath());

        Map<String, Object> data = new HashMap<>();
        data.put("natType", Plugin.client().getNatType());
        data.put("outAddr", Plugin.client().getOutAddr());

        Result<Map<String, Object>> r = new Result<>();
        r.setData(data);

        return r;
    }

    /**
     * 建立p2p连接
     * 
     * @param ui
     * @param token
     * @return
     */
    @GET
    @Path("conn/{clientId}")
    public Result<String> connClient(@Context UriInfo ui, @PathParam("clientId") String clientId) {
        LOG.info("{}", ui.getPath());

        Result<String> r = new Result<>();
        // r.setData(ManagementFactory.getRuntimeMXBean().getName() + " " + System.currentTimeMillis());
        return r;
    }

    /**
     * 断开p2p连接
     * 
     * @param ui
     * @param token
     * @return
     */
    @GET
    @Path("disconn/{clientId}")
    public Result<String> disconnClient(@Context UriInfo ui, @PathParam("clientId") String clientId) {
        LOG.info("{}", ui.getPath());
        Result<String> r = new Result<>();
        // r.setData(ManagementFactory.getRuntimeMXBean().getName() + " " + System.currentTimeMillis());
        return r;
    }

}
