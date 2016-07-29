package cloudoll.rest;


import cloudoll.rest.meta.App;


public class Server {

    public static void main(String[] args) {
        App.cloudeerHost = "localhost";
        App.cloudeerPort = 8801;
        App.serviceName = "cloudoll-java-test";
        App.servicePort = 2000;

        String[] packages = {"cloudoll.rest.test"};
        ServiceDiscover.startService(packages);
    }

}
