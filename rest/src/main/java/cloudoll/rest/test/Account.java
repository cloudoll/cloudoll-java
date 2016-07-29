package cloudoll.rest.test;

import cloudoll.rest.annotation.Method;
import cloudoll.rest.annotation.Param;
import cloudoll.rest.annotation.Service;
import spark.Request;
import spark.Response;

@Service
public class Account {
    @Method(title = "获取A")
    public String getA() {
        return "a";
    }

    @Method(title = "提交B")
    public String $addB() {
        return "b";
    }

    @Method(title = "多个参数")
    public String testMany(
            @Param(name = "a", require = true) String a,
            @Param(name = "b", require = true) int b,
            @Param(name = "c", require = true) float c,
            int d,
            Animal animal) {
        return a + b + c + "<br>"
                + d + "<br>"
                + animal.getName() + "<br>" + (b * c);
    }


    @Method(title = "多个参数")
    public String $testMany(
            @Param(name = "a", require = true) String a,
            @Param(name = "b", require = true) int b,
            @Param(name = "c", require = true) float c,
            int d,
            Animal animal) {
        return a + b + c + "<br>"
                + d + "<br>"
                + "动物名字: " +  animal.getName()  + "<br>"
                + "动物重量: " +  animal.getWeight()  + "<br>"
                + "<br>" + (b * c);
    }


    @Method(title = "测试 spark")
    public String testSpark(Request request, Response response) {
        return "Hello Spark <br />" + request.queryParams("a");
    }
}


