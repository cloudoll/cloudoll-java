package cloudoll.rest.meta;

/**
 * 重新绑定到一个容易读的类中
 */
public class CloudollParam {
    private boolean isRequestKey;
    private boolean isPOJO;
    private String name;
    private boolean require;
    private Class clazz;
    private String regexPattern;

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequire() {
        return require;
    }

    public void setRequire(boolean require) {
        this.require = require;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isRequestKey() {
        return isRequestKey;
    }

    public void setRequestKey(boolean requestKey) {
        isRequestKey = requestKey;
    }

    public boolean isPOJO() {
        return isPOJO;
    }

    public void setPOJO(boolean POJO) {
        isPOJO = POJO;
    }
}
