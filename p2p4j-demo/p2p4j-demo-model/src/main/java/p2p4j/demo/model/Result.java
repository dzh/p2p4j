package p2p4j.demo.model;

import p2p4j.demo.model.util.GsonUtil;

/**
 * @author dzh
 * @date Aug 16, 2018 4:13:59 PM
 * @version 0.0.1
 */
public class Result<T> {

    private int code = 0;

    private T data;

    private String msg;

    private Integer count; // if data is array, count is the array's size

    private Integer version;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String toString() {
        return GsonUtil.toJson(this);
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
