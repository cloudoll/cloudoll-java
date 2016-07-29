package cloudoll.rest.test;

import cloudoll.rest.annotation.Method;
import cloudoll.rest.annotation.Service;
import cloudoll.rest.meta.ServiceTypes;

@Service(baseRoute = "/admin", serviceType = ServiceTypes.Admin)
public class Account2 {
    @Method(title = "服务器获取A")
    public String getA(String x) {
        return "a";
    }

    @Method(title = "服务器提交B")
    public String $addB() {
        return "b";
    }
}
