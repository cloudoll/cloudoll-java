package cloudoll.rest;


import static spark.Spark.port;


public class Server {

    public static void main(String[] args) {
        port(2000);
        String[] packages = {"cloudoll.rest.test"};
        ServiceDiscover.startService(packages);
    }

}
