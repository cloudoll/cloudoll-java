package cloudoll.rest.exceptions;

/**
 * 自定义错误
 */
public class CloudollException extends Exception {

    public CloudollException(int errno, String message, String service) {
        super(message + " [" + errno + "] @ " + service);
        this.errno = errno;
        this.errText = message;
        this.service = service;
    }


    private Integer errno;
    private String errText;
    private String service;

    public String getErrText() {
        return errText;
    }

    public void setErrText(String errText) {
        this.errText = errText;
    }

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
