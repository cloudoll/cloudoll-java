package cloudoll.rest.meta;

import com.alibaba.fastjson.JSONObject;

public class CloudollResponse {
    private Integer errno;
    private String errText;
    private String service;
    private Object data;

    public String getErrText() {
        return errText;
    }

    public void setErrText(String errText) {
        this.errText = errText;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
