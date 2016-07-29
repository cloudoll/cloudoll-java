package cloudoll.rest.exceptions;


/**
 * 格式错误
 */
public class FormatException extends CloudollException {
    public FormatException(String message, String service) {
        super(-1003, message, service);
    }
}
