# cloudoll 的 java 版本

目的是辅助快速创建可分布的微服务。

一下是一些特性

* 使用 [sparkjava](http://sparkjava.com/) 创建 restful 的服务

* 开发者只需要创建出逻辑方法, 将 jar 包丢入寻找路径, 即可映射出微服务接口

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