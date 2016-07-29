package cloudoll.rest.annotation;

import cloudoll.rest.meta.ServiceTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is the Controller annotation for class
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    String baseRoute() default "/open";

    ServiceTypes serviceType() default ServiceTypes.Open;
}
