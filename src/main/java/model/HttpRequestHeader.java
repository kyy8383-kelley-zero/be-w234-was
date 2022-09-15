package model;

import java.util.Map;
import java.util.Objects;

public class HttpRequestHeader {
    private Method method;
    private String uri;
    private Map<String, String> params;

    public HttpRequestHeader(Method method, String uri, Map<String, String> params) {
        this.method = method;
        this.uri = uri;
        this.params = params;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestHeader that = (HttpRequestHeader) o;
        return method == that.method && Objects.equals(uri, that.uri) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, uri, params);
    }
}
