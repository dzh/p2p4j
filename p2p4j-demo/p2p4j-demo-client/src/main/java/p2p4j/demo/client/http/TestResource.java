package p2p4j.demo.client.http;

import java.lang.management.ManagementFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.model.Result;

/**
 * @author dzh
 * @date May 12, 2018 7:39:27 PM
 * @version 0.0.1
 */
@Path("test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    static Logger LOG = LoggerFactory.getLogger(TestResource.class);

    @GET
    @Path("ping")
    public Result<String> ping(@Context UriInfo ui) {
        LOG.info("{}", ui.getPath());
        Result<String> r = new Result<>();
        r.setData(ManagementFactory.getRuntimeMXBean().getName() + " " + System.currentTimeMillis());
        return r;
    }

}
