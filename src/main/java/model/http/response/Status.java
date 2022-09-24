package model.http.response;

public enum Status {
    OK(200, "OK"),
    FOUND(302, "FOUND"),
    CONFLICT(409, "CONFLICT");

    private final int code;
    private final String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

