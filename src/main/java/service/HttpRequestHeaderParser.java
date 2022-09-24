package service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import error.HttpRequestException;
import model.http.request.HttpRequest;
import model.http.request.Method;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class HttpRequestHeaderParser {
    private static final String QUERY_STRING_DELIMITER = "\\?";
    private static final String DOT_DELIMITER = "\\.";

    public static HttpRequest parseHttpRequestHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
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
        Map<String, String> body = getBody(br);

        return new HttpRequest(method, uri, params, type, body);
    }


    private static String[] getHeaderLine(String line) {
        return line.split(" ");
    }

    private static Method getMethod(String line) {
        return Method.valueOf(getHeaderLine(line)[0]);
    }

    private static String getType(String line) {
        return Stream.of(getHeaderLine(line)[1].split(DOT_DELIMITER)).reduce((first, last) -> last).get();
    }

    private static Map<String, String> getParams(String[] uriLine) {
        if (uriLine.length > 1) {
            return HttpRequestUtils.parseQueryString(uriLine[1]);
        }
        return Maps.newHashMap();
    }

    private static Map<String, String> getBody(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> bodyMap = new HashMap<>();
        String line;

        while (!"".equals((line = br.readLine())) && line != null) {
            String[] rawRequest = line.split(": ");
            headers.put(rawRequest[0], rawRequest[1]);
        }

        if (headers.containsKey("Content-Length")) {
            String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            bodyMap.putAll(HttpRequestUtils.parseQueryString(body));
        }
        return bodyMap;
    }


    private static String getUri(String[] uriLine) {
        return uriLine[0];
    }
}
