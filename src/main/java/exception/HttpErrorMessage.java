package exception;

public enum HttpErrorMessage {
    EMPTY_REQUEST("요청 내용이 비어있습니다."),
    INVALID_REQUEST("지원하지 않는 형식의 요청입니다.");

    private String message;

    HttpErrorMessage(String errorMessage) {
        this.message = errorMessage;
    }

    public String getMessage() {
        return message;
    }
}
