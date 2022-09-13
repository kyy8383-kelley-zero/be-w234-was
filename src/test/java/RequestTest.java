import exception.HttpErrorMessage;
import exception.HttpException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.RequestParser;

import static org.assertj.core.api.Assertions.*;

public class RequestTest {

    @Test
    @DisplayName("url parse 테스트")
    void verifyUrl() {
        assertThat(RequestParser.getPath("GET /index.html HTTP/1.1")).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("빈 url 검증")
    void verifyEmptyUrl() {
        assertThatThrownBy(() -> { RequestParser.getPath(""); }).isInstanceOf(HttpException.class)
                .hasMessageContaining(HttpErrorMessage.EMPTY_REQUEST.getMessage());
        assertThatThrownBy(() -> { RequestParser.getPath(null); }).isInstanceOf(HttpException.class)
                .hasMessageContaining(HttpErrorMessage.EMPTY_REQUEST.getMessage());
    }

    @Test
    @DisplayName("잘못된 url 검증")
    void verifyInvalidUrl() {
        assertThatThrownBy(() -> { RequestParser.getPath("GET"); }).isInstanceOf(HttpException.class)
                .hasMessageContaining(HttpErrorMessage.INVALID_REQUEST.getMessage());
    }
}
