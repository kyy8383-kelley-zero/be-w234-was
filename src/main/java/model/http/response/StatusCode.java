package model.http.response;

public enum StatusCode {
    OK(200, "OK"),
    REDIRECT(302, "REDIRECT"),
    CONFLICT(409, "Conflict"),

    BAD_REQUEST(400, "BAD_REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED");

    private final int statusCode;
    private final String message;

    StatusCode(int status, String message) {
        this.statusCode = status;
        this.message = message;
    }

    public int getStatus() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}

