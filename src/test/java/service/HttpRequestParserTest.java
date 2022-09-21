package service;

import com.google.common.collect.Maps;
import model.http.request.HttpRequest;
import model.http.request.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import error.HttpRequestException;

import java.util.Map;
import java.util.stream.Stream;

class HttpRequestParserTest {

    @ParameterizedTest(name = "{index} : {0}")
    @MethodSource("provideNormalParams")
    void parseHttpRequestHeader(
            String description,
            String headerLine,
            HttpRequest expected
    ) {
        HttpRequest sut = HttpRequestHeaderParser.parseHttpRequestHeader(headerLine);
        Assertions.assertEquals(expected, sut);
    }

    @ParameterizedTest(name = "{index} : {0}")
    @MethodSource("provideAbnormalParams")
    void notParseHttpRequestHeader(
            String description,
            String headerLine,
            String errorMessage
    ) {
        HttpRequestException httpRequestException = Assertions.assertThrows(HttpRequestException.class,
                () -> HttpRequestHeaderParser.parseHttpRequestHeader(headerLine));
        String message = httpRequestException.getMessage();
        Assertions.assertEquals(errorMessage, message);

    }

    private static Stream<Arguments> provideNormalParams() {
        return Stream.of(
                Arguments.arguments("query params가 없는 http request header를 parse 할 수 있다.",
                        "GET ./css/style.css HTTP/1.1",
                        new HttpRequest(
                                Method.GET,
                                "./css/style.css",
                                Maps.newHashMap(),
                                "css")
                ),
                Arguments.arguments("query params가 있는 http request header를 parse 할 수 있다.",
                        "GET /user/create?userId=kimkelley&password=password&name=kelley&email=kelley@naver.com",
                        new HttpRequest(
                                Method.GET,
                                "/user/create",
                                Map.ofEntries(
                                        Map.entry("userId", "kimkelley"),
                                        Map.entry("password", "password"),
                                        Map.entry("name", "kelley"),
                                        Map.entry("email", "kelley@naver.com")),
                                "com")
                )
        );
    }

    private static Stream<Arguments> provideAbnormalParams() {
        return Stream.of(
                Arguments.arguments(
                        "빈 http request header를 parse 할 수 없다.",
                        "",
                        "header가 비어있습니다."
                )
        );
    }

}