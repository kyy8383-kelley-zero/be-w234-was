import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import webserver.RequestParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class RequestParserTest {

    @Test
    void testRequestParsing() throws Exception {
        InputStream in1 = new ByteArrayInputStream((
                "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*").getBytes());
        InputStream in2 = new ByteArrayInputStream((
                "A A\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*").getBytes());

        // index.html 호출
        RequestParser requestParser1 = new RequestParser(in1);

        assertThat(requestParser1.method).isEqualTo("GET");
        assertThat(requestParser1.path).isEqualTo("/index.html");
        assertThat(requestParser1.version).isEqualTo("HTTP/1.1");

        assertThat(requestParser1.headers.size()).isEqualTo(3);
        assertThat(requestParser1.headers.get("host")).isEqualTo("localhost:8080");
        assertThat(requestParser1.headers.get("connection")).isEqualTo("keep-alive");
        assertThat(requestParser1.headers.get("accept")).isEqualTo("*/*");

        Assertions.assertThrows(Exception.class, () -> {
            // 유효하지 않은 호출
            RequestParser requestParser2 = new RequestParser(in2);
        });
    }
}