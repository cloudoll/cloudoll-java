package cloudoll.rest;


import cloudoll.rest.exceptions.CloudollException;
import cloudoll.rest.meta.App;
import cloudoll.rest.meta.CloudollResponse;
import cloudoll.rest.rpc.Cloudeer;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Server {

    public static void main(String[] args) throws InterruptedException, CloudollException {
        //App.cloudeerHost = "localhost";
        App.cloudeerHost = "127.0.0.1";

        App.cloudeerPort = 8801;
        App.myName = "cloudoll-java-test";
        App.myPort = 2000;

        String[] packages = {"cloudoll.rest.test"};
        ServiceDiscover.startService(packages);

        Cloudeer cloudeer = new Cloudeer();
        try {
            cloudeer.startDownloadService();
        } catch (CloudollException e) {
            e.printStackTrace();
        }
        Thread.sleep(2000);

        //---- 测试 get
        CloudollResponse res = cloudeer.get("cloudarling", "/open/district/children");
        System.out.println(res);

        //---- 测试 post
        Map<String, String> postData = new HashMap<>();
        postData.put("passport", "13006699866");
        postData.put("password", "111111");
        CloudollResponse resP = cloudeer.postAsUrl("cloudarling", "/open/account/login", postData);

        System.out.println(resP);


    }

}
