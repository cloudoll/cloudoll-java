# cloudoll 的 java 版本

目的是辅助快速创建可分布的微服务。

一下是一些特性

* 使用 [sparkjava](http://sparkjava.com/) 创建 restful 的服务

* 开发者只需要创建出商业逻辑方法, 将 jar 包丢入寻找路径, 即可映射出微服务接口。


```java

import static spark.Spark.port;


public class Server {

    public static void main(String[] args) {
        port(2000);
        String[] packages = {"cloudoll.rest.test"};
        ServiceDiscover.startService(packages);
    }

}

```

## 商业逻辑类的写法


示例


```java
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

    @Method(title = "GET多个参数")
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


    @Method(title = "POST多个参数")
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

```

运行之后测试一下:

```
http://localhost:2000/open/account/test-many?a=xxx&b=2&c=1.1&name=猫
```

上面的例子中 a b c 分别会被映射到方法 testMany 中的对应参数,
name=猫 这个参数会被映射到 Animal 的 name 属性。

## 自动映射规则

* 只有注解成 cloudoll.rest.annotation.Service  的类才会被自动路由。

* 只有被注解成 cloudoll.rest.annotation.Method  的方法才会被自动路由。

* 方法中的参数规则

 > 注解成 cloudoll.rest.annotation.Param 的参数, 会自动从 request 中获取值, 并做类型转换。

 > 支持 POJO 类的参数映射, 这个无需注解, POJO 可以直接从 request 中去解析, 并赋值。

 > 可以支持 sparkjava 方式的方法, 参数中只有  request 和 response 两个参数。

 > 方法默认会映射成 GET, 方法名以 「$」 开头的将被映射成 POST。