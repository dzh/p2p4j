package p2p4j.demo.model;

/**
 * @author dzh
 * @date Sep 25, 2018 10:40:42 AM
 * @version 0.0.1
 */
public interface P2P4jDemoConst {

    // http
    String HTTP_BOSS_THREADS = "p2p4j.http.boss.threads";
    String HTTP_WORK_THREADS = "p2p4j.http.work.threads";
    String HTTP_HOST = "p2p4j.http.host";
    String HTTP_PORT = "p2p4j.http.port";
    String HTTPS_ENABLED = "p2p4j.https.enabled";
    String HTTP_ROOT = "p2p4j.http.root";

    // client
    String P2P4J_CLIENT_TOKEN = "p2p4j.client.token";
    String P2P4J_CLIENT_NAT_UDP = "p2p4j.client.nat.udp"; // port 本地
    String P2P4J_CLIENT_SERVER_NAT = "p2p4j.client.server.nat"; // ost:port NAT类型检查服务器
    String P2P4J_CLIENT_SERVER_PREDICT = "p2p4j.client.server.predict";// host:port 对称型NAT时用户端口预测

    // server
    String P2P4J_SERVER_UDP_PORT = "p2p4j.server.udp.port"; // port 监听
    String P2P4J_SERVER_SHADOW_UDP_PORT = "p2p4j.server.shadow.udp.port"; // 用户NAT类型的辅助检测
    String P2P4J_SERVER_PARTNER = "p2p4j.server.partner"; // host:port 用于NAT类型验证的伙伴节点

}
