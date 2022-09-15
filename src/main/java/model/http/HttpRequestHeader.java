package model.http;

import java.util.Map;
import java.util.Objects;

public class HttpRequestHeader {
    private final Method method;
    private final String uri;
    private final Map<String, String> params;
    private final String contentType;

    public HttpRequestHeader(Method method, String uri, Map<String, String> params, String contentType) {
        this.method = method;
        this.uri = uri;
        this.params = params;
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "HttpRequestHeader{" +
                "method=" + method +
                ", uri='" + uri + '\'' +
                ", params=" + params +
                ", contentType='" + contentType + '\'' +
                '}';
    }

    public Method getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestHeader that = (HttpRequestHeader) o;
        return method == that.method && Objects.equals(uri, that.uri) && Objects.equals(params, that.params) && Objects.equals(contentType, that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, uri, params, contentType);
    }
}
