package service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import model.http.HttpRequestHeader;
import model.http.Method;
import util.HttpRequestUtils;
import webserver.error.HttpRequestException;

import java.util.Map;
import java.util.stream.Stream;

public class HttpRequestHeaderParser implements HttpRequestParser {
    private static final String QUERY_STRING_DELIMITER = "\\?";
    private static final String DOT_DELIMITER = "\\.";

    public HttpRequestHeader parseHttpRequestHeader(String line) {
        if (Strings.isNullOrEmpty(line)) {
            throw new HttpRequestException("header가 비어있습니다.");
        }
        String[] headerLine = getHeaderLine(line);
        String requestUri = headerLine[1];
        String[] uriLine = requestUri.split(QUERY_STRING_DELIMITER);

        Method method = getMethod(line);
        String uri = getUri(uriLine);
        Map<String, String> params = getParams(uriLine);
        String type = getType(line);

        return new HttpRequestHeader(method, uri, params, type);
    }

    private String[] getHeaderLine(String line){
        return line.split(" ");
    }
    
    private Method getMethod(String line){
        return Method.valueOf(getHeaderLine(line)[0]);
    }

    private String getType(String line){
        return Stream.of(getHeaderLine(line)[1].split(DOT_DELIMITER)).reduce((first, last) -> last).get();
    }

    private Map<String, String> getParams(String[] uriLine){
        if(uriLine.length > 1) {
            return HttpRequestUtils.parseQueryString(uriLine[1]);
        }
        return Maps.newHashMap();

    }

    private String getUri(String[] uriLine){
        return uriLine[0];
    }
}
