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

    public HttpRequestHeader getHttpRequestHeader(String line) {
        if (Strings.isNullOrEmpty(line)) {
            throw new HttpRequestException("header가 비어있습니다.");
        }
        String[] headerLine = line.split(" ");
        Method method = Method.valueOf(headerLine[0]);

        String[] uriLine = headerLine[1].split(QUERY_STRING_DELIMITER);
        String uri = uriLine[0];

        Map<String, String> params = Maps.newHashMap();

        String type = Stream.of(headerLine[1].split(DOT_DELIMITER)).reduce((first, last) -> last).get();

        if(uriLine.length > 1) {
            params = HttpRequestUtils.parseQueryString(uriLine[1]);
        }

        return new HttpRequestHeader(method, uri, params, type);
    }
}
