package p2p4j.demo.model.util;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author dzh
 * @date Apr 2, 2018 4:30:17 PM
 * @version 0.0.1
 */
public class IDUtil {

    public static final String uuid3() {
        String ts = Long.toHexString(System.currentTimeMillis());
        String hash = Integer.toHexString(ManagementFactory.getRuntimeMXBean().getName().hashCode());
        int ranlen = 32 - ts.length() - hash.length();

        StringBuilder buf = new StringBuilder(32);
        buf.append(ts);
        // buf.append("-");
        buf.append(hash);
        // buf.append("-");
        for (int i = 0; i < ranlen; i++) {
            buf.append(Integer.toHexString(ThreadLocalRandom.current().nextInt(0, 16)));
        }
        return buf.toString();
    }

}
