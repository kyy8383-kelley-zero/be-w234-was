package webserver;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "Hello World".getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.info("run() error");
            logger.error(e.getMessage());
        }

        try {
            connection.close();
            logger.info("Connection closed");
        } catch(Exception ee){
            logger.info("connection.close() error in run()");
            logger.error(ee.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.write("HTTP/1.1 200 OK \r\n".getBytes());
            dos.write("Content-Type: text/html;charset=utf-8\r\n".getBytes());
            dos.write(("Content-Length: " + lengthOfBodyContent + "\r\n").getBytes());
            dos.write("\r\n".getBytes());
        } catch (IOException e) {
            logger.info("response200Header() error");
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
