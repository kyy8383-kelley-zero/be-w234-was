package model.http.response;

import java.util.Map;

public class HttpResponse {
    private final StatusCode statusCode;
    private final Map<String, String> headers;
    private final byte[] body;

    public HttpResponse( StatusCode statusCode, Map<String, String> headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public String getVersion() {
        String version = "HTTP/1.1";
        return version;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

}

