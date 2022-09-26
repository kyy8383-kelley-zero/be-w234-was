package webserver;

import model.http.request.HttpRequest;
import model.http.request.Method;
import model.http.response.HttpResponse;
import model.http.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.UserController;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String WEB_PATH = "./webapp";

    public static HttpResponse getResponse(HttpRequest httpRequest) throws IOException {
        if (httpRequest.getUri().contains("user") && httpRequest.getMethod().equals(Method.POST))
            return UserController.getInstance().response(httpRequest);

        return new HttpResponse.HttpResponseBuilder()
                .status(Status.OK)
                .body(Files.readAllBytes(new File(WEB_PATH + File.separator + httpRequest.getUri()).toPath()))
                .build();
    }

    public static void writeResponse(DataOutputStream dos, HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            writeHeaders(dos, httpRequest, httpResponse);
            writeBody(dos, httpResponse.getBody());
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHeaders(DataOutputStream dos, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
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

    private static void writeBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
