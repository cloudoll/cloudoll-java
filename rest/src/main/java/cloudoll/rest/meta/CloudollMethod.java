package cloudoll.rest.meta;

import java.lang.reflect.Method;

/**
 * 描述一个服务
 */
public class CloudollMethod {
    public CloudollMethod() {
        this.httpMethod = "GET";
    }

    public CloudollMethod(
            String path, String title,
            Method method, String httpMethod,
            ServiceTypes serviceType
    ) {
        this.path = path;
        this.title = title;
        this.method = method;
        if (httpMethod == null) {
            httpMethod = "GET";
        }
        this.httpMethod = httpMethod;
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "{\n" +
                "  httpMethod: " + this.httpMethod + ", \n" +
                "  path: " + this.path + ", \n" +
                "  title: " + this.title + ", \n" +
                "  method: " + method.getName() + "\n" +
                "  serviceType: " + this.serviceType + "\n" +
                "}";
    }

    private String httpMethod;
    private ServiceTypes serviceType;
    private String title;
    private Method method;
    private String path;
    private boolean isSpark;

    public boolean isSpark() {
        return isSpark;
    }

    public void setSpark(boolean spark) {
        isSpark = spark;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }


    public ServiceTypes getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypes serviceType) {
        this.serviceType = serviceType;
    }
}
