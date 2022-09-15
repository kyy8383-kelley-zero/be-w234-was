package service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import model.HttpRequestHeader;
import model.Method;
import util.HttpRequestUtils;

import java.util.Map;

public class HttpRequestHeaderParser {
    public static final String QUERY_STRING_DELIMITER = "\\?";

    public static HttpRequestHeader getHttpRequestHeader(String line) {
        if (Strings.isNullOrEmpty(line)) {
            throw new RuntimeException("header가 비어있습니다.");
        }

        String[] headerLine = line.split(" ");

        Method method = Method.valueOf(headerLine[0]);
        String[] uriLine = headerLine[1].split(QUERY_STRING_DELIMITER);
        String uri = uriLine[0];
        Map<String, String> params = Maps.newHashMap();

        if(uriLine.length > 1) {
            params = HttpRequestUtils.parseQueryString(uriLine[1]);
        }

        return new HttpRequestHeader(method, uri, params);
    }
}
