package webserver;

import webserver.controller.UserController;
import model.http.request.HttpRequest;
import model.http.request.Method;
import model.http.response.HttpResponse;
import model.http.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpRequestHeaderParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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

            write(dos, httpResponse, httpRequest);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpResponse getResponse(HttpRequest httpRequest) throws IOException {
        if (httpRequest.getUri().contains("user") && httpRequest.getMethod().equals(Method.POST))
            return UserController.getInstance().response(httpRequest);

        return new HttpResponse.HttpResponseBuilder()
                .status(Status.OK)
                .body(Files.readAllBytes(new File("./webapp" + File.separator + httpRequest.getUri()).toPath()))
                .build();
    }

    private void write(DataOutputStream dos, HttpResponse httpResponse, HttpRequest httpRequest) {
        try {
            writeHeaders(dos, httpRequest, httpResponse);
            writeBody(dos, httpResponse.getBody());
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHeaders(DataOutputStream dos, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        dos.writeBytes(httpResponse.getVersion() + " " + httpResponse.getStatus().getCode() + " " + httpResponse.getStatus().getMessage() + " " + "\r\n");
        dos.writeBytes("Content-Type: text/" + httpRequest.getContentType() + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + httpResponse.getBody().length + "\r\n");

        httpResponse.getHeaders().forEach(
                (key, values) -> {
                    String joinString = String.join(";", values);
                    try {
                        dos.writeBytes(key + ": " + joinString + "\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        dos.writeBytes("\r\n");
    }

    private void writeBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}