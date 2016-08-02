package cloudoll.rest.rpc;

import cloudoll.rest.exceptions.CloudollException;
import cloudoll.rest.meta.App;
import cloudoll.rest.meta.CloudollResponse;
import cloudoll.rest.meta.Host;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 这是远程访问 注册中心的服务
 */
public class Cloudeer {

    public Cloudeer() throws CloudollException {

        if (App.cloudeerHost == null) {
            throw new CloudollException(-1, "必须指定 Cloudeer 服务的地址信息", "System");
        }
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(App.cloudeerHost)
                    .setPort(App.cloudeerPort)
                    .setPath("/load-config")
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new CloudollException(-1, "创建 url 错误, 请检查您的 App 配置", App.myName);
        }
        httpDownload = new HttpGet(uri);
        httpclient = HttpClients.createDefault();
    }

    private HttpGet httpDownload;
    private CloseableHttpClient httpclient;

    private void download() throws CloudollException {
        try {
            CloseableHttpResponse response = httpclient.execute(httpDownload);
            String oriRes = EntityUtils.toString(response.getEntity());
            CloudollResponse cloudeerServers = JSONObject.parseObject(oriRes, CloudollResponse.class);
            Map servers = (Map) cloudeerServers.getData();

            if (App.servers == null) {
                App.servers = servers;
            } else {
                synchronized (App.servers) {
                    App.servers = servers;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CloudollException(-1, "下载服务错误", App.myName);
        }
    }

    public void startDownloadService() throws CloudollException {
        System.out.println("下载服务已经启动....");
        new Thread(() -> {
            while (true) {
                try {
                    this.download();
                    TimeUnit.SECONDS.sleep(App.downServerInterval);
                } catch (CloudollException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /***
     * 从 serviceName 中抽随机抽取一个 host
     */
    private Host getHost(String serviceName) throws CloudollException {
        if (App.servers == null) {
            throw new CloudollException(-1, "cloudeer 系统尚未被初始化,请稍等...", App.myName);
        }
        Map serverGroup = (Map) App.servers.get(serviceName);
        List<Host> hosts = JSONArray.parseArray(serverGroup.get("hosts").toString(), Host.class);

        Random rand = new Random();
        return hosts.get(rand.nextInt(hosts.size()));
    }

    public CloudollResponse get(String serviceName, String uri) throws CloudollException {
        return this.get(serviceName, uri, null);
    }

    public CloudollResponse postAsJson(String serviceName, String uri, Object param) throws CloudollException {
        Host host = this.getHost(serviceName);
        URI getUri;
        try {
            getUri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host.getHost())
                    .setPort(host.getPort())
                    .setPath(host.getBaseUri() + (uri.startsWith("/") ? uri : "/" + uri))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new CloudollException(-1, "错误的请求地址", App.myName);
        }
        HttpPost httpPost = new HttpPost(getUri);
        System.out.println(JSONObject.toJSONString(param));
        try {
            httpPost.setEntity(new StringEntity(JSONObject.toJSONString(param)));
            httpPost.setHeader("Content-type", "application/json");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CloudollException(-1, "请求的字符串编码错误", App.myName);
        }
        return this.executeHttp(httpPost);
    }

    public CloudollResponse postAsUrl(String serviceName, String uri, Map<String, String> data) throws CloudollException {
        Host host = this.getHost(serviceName);
        URI getUri;
        try {
            getUri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host.getHost())
                    .setPort(host.getPort())
                    .setPath(host.getBaseUri() + (uri.startsWith("/") ? uri : "/" + uri))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new CloudollException(-1, "错误的请求地址", App.myName);
        }
        HttpPost httpPost = new HttpPost(getUri);
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            data.forEach((k, v) -> nvps.add(new BasicNameValuePair(k, v)));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CloudollException(-1, "请求的字符串编码错误", App.myName);
        }
        return this.executeHttp(httpPost);
    }

    public CloudollResponse get(String serviceName, String uri, List<NameValuePair> param) throws CloudollException {
        Host host = this.getHost(serviceName);
        URI getUri;
        try {
            URIBuilder builder = new URIBuilder()
                    .setScheme("http")
                    .setHost(host.getHost())
                    .setPort(host.getPort())
                    .setPath(host.getBaseUri() + (uri.startsWith("/") ? uri : "/" + uri));
            if (param != null) {
                builder = builder.setParameters(param);
            }
            getUri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new CloudollException(-1, "错误的请求地址", App.myName);
        }
        HttpGet httpGet = new HttpGet(getUri);
        return this.executeHttp(httpGet);
    }

    private CloudollResponse executeHttp(HttpRequestBase httpRequest) throws CloudollException {
        try {
            CloseableHttpResponse response = httpclient.execute(httpRequest);
            String oriRes = EntityUtils.toString(response.getEntity());

//            System.out.println("----------------------------------------");
//            System.out.println(response.getStatusLine());
//            System.out.println(oriRes);
//            System.out.println("----------------------------------------");

            CloudollResponse rtn = JSONObject.parseObject(oriRes, CloudollResponse.class);
            if (rtn.getErrno() != 0) {
                throw new CloudollException(rtn.getErrno(), rtn.getErrText(), rtn.getService());
            }
            return rtn;

        } catch (IOException e) {
            e.printStackTrace();
            throw new CloudollException(-1, "IO 请求错误", App.myName);
        }
    }

}
