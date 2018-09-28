package p2p4j.demo.model;

/**
 * <pre>
 * 1.一个请求类型对应一个响应类型
 * 2. 一个请求可以fork成多个类型请求,fork的请求保持一样的id，表示同一个请求源
 * </pre>
 * 
 * @author dzh
 * @date Sep 25, 2018 8:23:19 PM
 * @version 0.0.1
 */
public interface ProtocolType {
    int REQ_PING = 0;
    int REQ_OUT_ADDR = 1;// 获取外部地址
    int REQ_CHECK_SYMMETRIC = 2;// 检查是否对称型NAT
    int REQ_CHECK_CONE = 3;// 检查是否锥形NAT，请求的端口返回
    int REQ_CHECK_CONE_SHADOW = 4;// 检查是否锥形NAT,请求端口的另一个端口返回消息
    int REQ_CHECK_CONE_PARTNER = 5;// 检查是否锥形NAT，请求端口的另一台机器返回
    int REQ_EXCHANGE_PEER_INFO = 6; // 交换对等节点信息，包括Peer的outAddr和NatType

    int REQ_CALLBACK_PEER_INFO = 7; // 通知另一个节点对自己发消息

    int REQ_TRAVERSAL_ADD = 1000; // 透传请求加法
    int REQ_TRAVERSAL_PING = 1001; // 连接透传的ping

    //////////////////////////////////////////////////////////

    int RSP_PING = 10000;
    int RSP_OUT_ADDR = 10001; //
    int RSP_CHECK_SYMMETRIC = 10002;// 检查是否NAT
    int RSP_CHECK_CONE = 10003;// 检查是否锥形NAT,请求的端口返回
    int RSP_CHECK_CONE_SHADOW = 10004;// 检查是否锥形NAT,请求端口的另一个端口返回消息
    int RSP_CHECK_CONE_PARTNER = 10005;// 检查是否锥形NAT,请求端口的另一台机器返回
    int RSP_EXCHANGE_PEER_INFO = 10006; //
    int RSP_CALLBACK_PEER_INFO = 7; // 通知另一个节点对自己发消息

    int RSP_TRAVERSAL_ADD = 11000; // 响应透传请求加法的结果

}