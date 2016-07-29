package cloudoll.rest;


import cloudoll.rest.annotation.Service;
import cloudoll.rest.annotation.Method;
import cloudoll.rest.meta.CloudollMethod;
import cloudoll.rest.meta.CloudollParam;
import cloudoll.rest.meta.ServiceTypes;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

import static org.reflections.ReflectionUtils.*;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 *
 */
public class ServiceDiscover {


    public ServiceDiscover(String[] packages) {
        this.packages = packages;
    }

    public Set<CloudollMethod> scan() {
        List s = Arrays.asList(this.packages);
        Set<CloudollMethod> allServices = new HashSet<>();
        s.forEach((pkg) -> {
            System.out.println("开始分析 " + pkg);
            Reflections reflections = new Reflections(pkg);

            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Service.class);
            controllers.forEach((controller) -> {
                String baseRoute = "/";
                ServiceTypes serviceType = ServiceTypes.Open;
                Annotation[] annotations = controller.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Service) {
                        Service ctr = (Service) annotation;
                        baseRoute = ctr.baseRoute();
                        serviceType = ctr.serviceType();
                    }
                }

                String controllerName = controller.getName();
                controllerName = controllerName.substring(controllerName.lastIndexOf('.') + 1);
                String pathName = baseRoute + "/" + Tools.toRestUrlName(controllerName);



                Set<java.lang.reflect.Method> methods = getAllMethods(controller,
                        withModifier(Modifier.PUBLIC), withAnnotation(Method.class));

                ServiceTypes finalServiceType = serviceType;
                methods.forEach(method -> {
                    Annotation[] annotations1 = method.getAnnotations();
                    for (Annotation annotation : annotations1) {
                        if (annotation instanceof Method) {
                            Method srv = (Method) annotation;
                            String path = srv.path();
                            String title = srv.title();
                            String methodName = method.getName();
                            String httpMethod = "GET";

                            if (methodName.charAt(0) == '$') {
                                httpMethod = "POST";
                            }
                            if (path.isEmpty()) {
                                path = Tools.toRestUrlName(method.getName());
                            }

                            CloudollMethod ms = new CloudollMethod(
                                    pathName + "/" + path,
                                    title,
                                    method,
                                    httpMethod,
                                    finalServiceType);
                            allServices.add(ms);
                        }
                    }

                });

            });

        });
        return allServices;
    }


   public static void startService(String[] packages){
        ServiceDiscover mf = new ServiceDiscover(packages);
        Set<CloudollMethod> map = mf.scan();
        map.forEach((ms) -> {
            System.out.println("自动路由: " + ms.getHttpMethod() + " " + ms.getPath());
            java.lang.reflect.Method method = ms.getMethod();
            Parameter[] parameters = method.getParameters();
            ms.setSpark(Tools.isSparkMethod(parameters));

            List<CloudollParam> params = Tools.arrageParameters(ms, parameters);

            if (ms.getHttpMethod().equals("POST")) {
                post(ms.getPath(), (request, response) -> Tools.invoke(ms, params, request, response));
            } else {
                get(ms.getPath(), (request, response) -> Tools.invoke(ms, params, request, response));
            }
        });
    }



    public String[] getPackages() {
        return packages;
    }

    public void setPackages(String[] packages) {
        this.packages = packages;
    }

    private String[] packages;


}
