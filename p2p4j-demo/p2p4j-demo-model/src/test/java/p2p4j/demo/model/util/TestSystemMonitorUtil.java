package p2p4j.demo.model.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dzh
 * @date Sep 25, 2018 2:07:09 PM
 * @version 0.0.1
 */
public class TestSystemMonitorUtil {

    static Logger LOG = LoggerFactory.getLogger(TestSystemMonitorUtil.class);

    @Test
    public void testSysInfo() {
        OperatingSystemMXBean osm = ManagementFactory.getOperatingSystemMXBean();
        String arch = osm.getArch();
        int cpuCount = osm.getAvailableProcessors();
        double cpuLoadAverage = osm.getSystemLoadAverage();

        String name = ManagementFactory.getRuntimeMXBean().getName();

        LOG.info("name-{} arch-{} cpuCount-{} cpuLoadAverage-{} {}", name, arch, cpuCount, cpuLoadAverage,
                new BigDecimal(cpuLoadAverage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    @Test
    public void testInetAddr() throws UnknownHostException {
        InetAddress addr = InetAddress.getByName("192.168.1.1");
        LOG.info("ip-{}", addr.getHostAddress());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", 1);
        map.put("b", null);
        LOG.info("values-{}", map.values().size());
    }

}
