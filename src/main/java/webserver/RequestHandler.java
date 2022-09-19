package webserver;

import model.User;
import model.http.HttpRequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpRequestHeaderParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequestHeader httpRequestHeader = HttpRequestHeaderParser.parseHttpRequestHeader(br.readLine());
            log.debug("HttpRequestHeader : {}", httpRequestHeader);

            String url = httpRequestHeader.getUri();

            if (url.contains("/user/create")) {
                sendSignUpRequest(httpRequestHeader);
            }

            response200Header(dos, getRequestBody(url).length, httpRequestHeader.getContentType());
            responseBody(dos, getRequestBody(url));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] getRequestBody(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    private void sendSignUpRequest(HttpRequestHeader httpRequestHeader) {
        Map<String, String> params = httpRequestHeader.getParams();
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        log.debug("User : {}", user);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
