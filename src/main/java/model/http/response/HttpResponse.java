package model.http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final Status status;
    private final String version = "HTTP/1.1";
    private final Map<String, String> headers;
    private final byte[] body;

    public HttpResponse(HttpResponseBuilder builder) {
        this.status = builder.status;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                '}';
    }

    public static class HttpResponseBuilder {
        private Status status;
        private final Map<String, String> headers = new HashMap<>();
        private byte[] body = new byte[0];

        public HttpResponseBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public HttpResponseBuilder headers(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public HttpResponseBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    public String getVersion() {
        return version;
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }


}

