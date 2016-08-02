package cloudoll.rest;

import cloudoll.rest.annotation.Param;
import cloudoll.rest.exceptions.CloudollException;
import cloudoll.rest.exceptions.FormatException;
import cloudoll.rest.meta.App;
import cloudoll.rest.meta.CloudollMethod;
import cloudoll.rest.meta.CloudollParam;
import cloudoll.rest.meta.CloudollResponse;
import spark.Request;
import spark.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllFields;

/**
 * 工具类
 */
class Tools {
    static String toRestUrlName(String javaName) {
        char[] chars = javaName.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char aChar : chars) {
            if (aChar >= 65 && aChar <= 90) {
                sb.append('-');
                sb.append(Character.toLowerCase(aChar));
            } else if (aChar != '$') {
                sb.append(aChar);
            }
        }
        if (sb.charAt(0) == '-') {
            sb.delete(0, 1);
        }
        return sb.toString();
    }


    static boolean isSparkMethod(Parameter[] parameters) {
        if (parameters.length == 2) {
            Parameter req = parameters[0];
            Parameter res = parameters[1];
            return (req.getType() == Request.class && res.getType() == Response.class);
        }
        return false;
    }


    static Object toObject(Class clazz, String value) throws FormatException {
        try {
            if (Boolean.class == clazz || boolean.class == clazz)
                return value != null && Boolean.parseBoolean(value);
            if (Byte.class == clazz || byte.class == clazz) return value == null ? 0 : Byte.parseByte(value);
            if (Short.class == clazz || short.class == clazz) return value == null ? 0 : Short.parseShort(value);
            if (Integer.class == clazz || int.class == clazz) return value == null ? 0 : Integer.parseInt(value);
            if (Long.class == clazz || long.class == clazz) return value == null ? 0 : Long.parseLong(value);
            if (Float.class == clazz || float.class == clazz) return value == null ? 0 : Float.parseFloat(value);
            if (Double.class == clazz || double.class == clazz) return value == null ? 0 : Double.parseDouble(value);
            return value;
        } catch (Throwable e) {
            throw new FormatException("格式错误: 「" + value + "」 不能被格式化成 " + clazz.getName(), App.myName);
        }
    }

    static boolean isPOJO(Class clazz) {
        return !(
                String.class == clazz ||
                        Boolean.class == clazz
                        || boolean.class == clazz
                        || Byte.class == clazz
                        || byte.class == clazz
                        || Short.class == clazz
                        || short.class == clazz
                        || Integer.class == clazz
                        || int.class == clazz
                        || Long.class == clazz
                        || long.class == clazz
                        || Float.class == clazz
                        || float.class == clazz
                        || Double.class == clazz
                        || double.class == clazz);
    }

    static List<CloudollParam> arrageParameters(CloudollMethod ms, Parameter[] parameters) {
        List<CloudollParam> params = new ArrayList<>();
        if (!ms.isSpark()) {
            for (Parameter parameter : parameters) {
                CloudollParam pp = new CloudollParam();
                pp.setClazz(parameter.getType());
                pp.setPOJO(isPOJO(parameter.getType()));
                Annotation[] annotations = parameter.getAnnotations();
                if (annotations.length > 0) {
                    Param an = (Param) annotations[0];
                    pp.setRequestKey(true);
                    pp.setName(an.name());
                    pp.setRegexPattern(an.regexPattern());
                    pp.setRequire(an.require());
                } else {
                    pp.setRequestKey(false);
                }
                params.add(pp);
            }
        }
        return params;
    }

    static Object bindPOJO(Class clazz, Request request) {
        try {
            Object instance = clazz.newInstance();
            Set<Field> fields = getAllFields(clazz);
            fields.forEach(
                    field -> {
                        String val = request.queryParams(field.getName());
                        String fieldName = field.getName();
                        String methodName = "set" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
                        try {
                            Method setMethod = clazz.getDeclaredMethod(methodName, field.getType());
                            setMethod.invoke(instance, toObject(field.getType(), val));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                    }
            );
            return instance;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }

    static Object invoke(CloudollMethod ms, List<CloudollParam> params, Request request, Response response)
            throws CloudollException {

        try {
            Method method = ms.getMethod();
            if (ms.isSpark()) {
                return method.invoke(method.getDeclaringClass().newInstance(), request, response);
            } else {

                List<Object> inParams = new ArrayList<>();
                for (CloudollParam pp : params) {
                    String val = request.queryParams(pp.getName());
                    if (pp.isRequestKey() && !pp.isPOJO()) {
                        inParams.add(Tools.toObject(pp.getClazz(), val));
                    } else if (pp.isPOJO()) {
                        inParams.add(bindPOJO(pp.getClazz(), request));
                    } else {
                        inParams.add(Tools.toObject(pp.getClazz(), null));
                    }
                }
                response.type("application/json");
                Object data = method.invoke(method.getDeclaringClass().newInstance(), inParams.toArray());
                CloudollResponse res = new CloudollResponse();
                res.setData(data);
                res.setErrno(0);
                return res;
            }
        } catch (CloudollException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new CloudollException(-1, "未知错误", App.myName);
        }
    }

}
