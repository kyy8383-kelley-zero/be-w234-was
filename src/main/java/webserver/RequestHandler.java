package webserver;

import model.http.request.HttpRequest;
import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpRequestHeaderParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static webserver.ResponseHandler.getResponse;
import static webserver.ResponseHandler.writeResponse;

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

            HttpRequest httpRequest = HttpRequestHeaderParser.parseHttpRequestHeader(br);
            log.debug("HttpRequest : {}", httpRequest);
            HttpResponse httpResponse = getResponse(httpRequest);
            log.debug("HttpResponse : {}", httpResponse);

            writeResponse(dos, httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}