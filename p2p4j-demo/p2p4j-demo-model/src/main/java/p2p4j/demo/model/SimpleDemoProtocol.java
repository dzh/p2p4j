package p2p4j.demo.model;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import p2p4j.demo.model.util.GsonUtil;
import p2p4j.demo.model.util.IDUtil;

/**
 * 测试用的简单的json协议
 * 
 * @author dzh
 * @date Sep 25, 2018 8:17:19 PM
 * @version 0.0.1
 */
public class SimpleDemoProtocol implements ProtocolType {

    public static final Integer MAX_SIZE = 1024;
    public static final String ENCODING = "utf-8";

    public static final String K_IP = "ip"; // ip
    public static final String K_PORT = "port";
    public static final String K_REQ_ID = "req_id"; // 服务端返回时带源id
    public static final String K_NAT_TYPE = "nat_type";
    public static final String K_TOKEN = "token";

    // public static final String K_REMOTESERVER = "removeServer"; // nat or predict
    // public static final String V_REMOTESERVER_NAT = "nat";
    // public static final String V_REMOTESERVER_PREDICT = "predict";

    private int type;

    private String id; // 每个消息包都有一个唯一的id

    private Map<String, String> data;

    private SimpleDemoProtocol() {}

    public static final SimpleDemoProtocol create(int type) {
        SimpleDemoProtocol p = new SimpleDemoProtocol();
        p.id = IDUtil.uuid3();
        p.type = type;
        return p;
    }

    public SimpleDemoProtocol fork(int type) {
        SimpleDemoProtocol newP = SimpleDemoProtocol.create(type);
        newP.setId(this.id);
        if (data != null) {
            newP.setData(new HashMap<String, String>(data));
        }
        return newP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public byte[] encode() {
        try {
            return GsonUtil.toJson(this).getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            // TODO
        }
        return new byte[0];
    }

    public static SimpleDemoProtocol decode(String json) {
        return GsonUtil.fromJson(json, SimpleDemoProtocol.class);
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

}
