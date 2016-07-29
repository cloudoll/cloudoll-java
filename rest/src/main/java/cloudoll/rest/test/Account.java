package cloudoll.rest.test;

import cloudoll.rest.annotation.Method;
import cloudoll.rest.annotation.Param;
import cloudoll.rest.annotation.Service;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

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
    public Object testMany(
            @Param(name = "a", require = true) String a,
            @Param(name = "b", require = true) int b,
            @Param(name = "c", require = true) float c,
            int d,
            Animal animal) {
        Map<String, Object> map = new HashMap<>();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        map.put("d", d);
        map.put("list", new String[]{"1", "2", "3"});
        map.put("animal", animal);
        return map;
    }


    @Method(title = "多个参数")
    public Object $testMany(
            @Param(name = "a", require = true) String a,
            @Param(name = "b", require = true) int b,
            @Param(name = "c", require = true) float c,
            int d,
            Animal animal) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", new String[]{"1", "2", "3"});
        map.put("animal", animal);
        return map;
    }


    @Method(title = "测试 spark")
    public String testSpark(Request request, Response response) {
        return "Hello Spark <br />" + request.queryParams("a");
    }
}


