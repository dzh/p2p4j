package p2p4j.demo.client.workflow;

import java.io.Closeable;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import p2p4j.demo.client.P2P4jDemoClient;
import p2p4j.demo.model.SimpleDemoProtocol;

/**
 * 检查NAT类型 检测双方节点是否在线(交换NAT类型信息) 开始建立连接
 * 
 * @author dzh
 * @date Sep 27, 2018 10:48:10 AM
 * @version 0.0.1
 */
public abstract class P2PWorkflow<V> implements Callable<V>, Closeable {

    static Logger LOG = LoggerFactory.getLogger(P2PWorkflow.class);

    public static final int S_START = 0;
    public static final int S_SUCC = 1;
    public static final int S_FAIL = 2;

    private volatile int state;

    private P2P4jDemoClient client;

    private boolean last; // loat work

    public P2PWorkflow(P2P4jDemoClient client) {
        this.client = client;
        this.state = S_START;
        this.last = false;
    }

    protected P2P4jDemoClient client() {
        return client;
    }

    public abstract void signal(SimpleDemoProtocol p);

    public abstract P2PWorkflow<?> next(Object value); // callable's value

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getState() {
        return state;
    }

    public boolean isSuss() {
        return state == S_SUCC;
    }

    public void setState(int state) {
        this.state = state;
    }

}
