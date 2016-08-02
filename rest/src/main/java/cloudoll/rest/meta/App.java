package cloudoll.rest.meta;

import java.util.Map;

/**
 * 应用的全局基础数据
 */
public class App {

    /**
     * 当前微服务的名称
     */
    public static String myName;

    /**
     * 当前服务的端口
     */
    public static int myPort;

    /**
     * 注册服务器的地址
     */
    public static String cloudeerHost;

    /**
     * 注册服务器的端口
     */
    public static int cloudeerPort;

    /**
     * 下载微服务地址的时间间隔（秒）
     */
    public static long downServerInterval = 10;

    /**
     * 提交微服务地址的时间间隔（为了保持心跳, 不被剔除）
     */
    public static int upServerInterval = 9;

    /**
     * 是否自动路由 /cloudeer/get 和 /cloudeer/post
     */
    public static boolean autoRouteCloudeer = true;

    public static Map servers = null;

}
