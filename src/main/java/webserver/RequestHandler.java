package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private final String RESOURCE_DIR = "src/main/webapp";

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private RequestParser requestParser;

    public RequestHandler(Socket connectionSocket) throws Exception {
        this.connection = connectionSocket;
        this.requestParser = new RequestParser(connection.getInputStream());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        printRequest();

        try (OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = generateBody();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void printRequest() {
        logger.debug("method : " + requestParser.method);
        logger.debug("path : " + requestParser.path);
        logger.debug("version : " + requestParser.version);

        for (Map.Entry<String, String> entry : requestParser.headers.entrySet()) {
            logger.debug(entry.getKey() + " : " + entry.getValue());
        }
    }

    private byte[] generateBody() {
        try {
            return generateHtmlBody(getResourcePath(requestParser.path));
        } catch (Exception e) {
            return generateDefaultBody();
        }
    }

    private String getResourcePath(String path) {
        return RESOURCE_DIR + path;
    }

    private byte[] generateHtmlBody(String path) throws IOException {
        return Files.readAllBytes(new File(path).toPath());
    }

    private byte[] generateDefaultBody() {
        return "Hello World".getBytes();
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
