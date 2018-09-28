package p2p4j.demo.model.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.model.SysInfo;

/**
 * @author dzh
 * @date Sep 25, 2018 2:06:05 PM
 * @version 0.0.1
 */
public class SysInfoUtil {

    static Logger LOG = LoggerFactory.getLogger(SysInfoUtil.class);

    public static final SysInfo getSysInfo() {
        OperatingSystemMXBean osm = ManagementFactory.getOperatingSystemMXBean();
        String arch = osm.getArch();
        int cpuCount = osm.getAvailableProcessors();
        double cpuLoadAverage = osm.getSystemLoadAverage();
        // LOG.info("arch-{} cpuCount-{} cpuLoadAverage-{}", arch, cpuCount, cpuLoadAverage);

        String name = ManagementFactory.getRuntimeMXBean().getName();
        // LOG.info("name-{}", name);

        SysInfo sysInfo = new SysInfo();
        sysInfo.setName(name);
        sysInfo.setArch(arch);
        sysInfo.setAvailableProcessors(cpuCount);
        sysInfo.setSystemLoadAverage(new BigDecimal(cpuLoadAverage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return sysInfo;
    }

}
