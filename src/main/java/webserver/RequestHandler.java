package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private final String HTML_DIR = "src/main/webapp";

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = generateBody(in);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] generateBody(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));

        String line = br.readLine();
        logger.info(line);
        String htmlPath = getHtmlPath(line);

        while (StringUtils.isNotBlank(line = br.readLine())) {
            logger.info(line);
        }

        try {
            return generateHtmlBody(htmlPath);
        } catch (Exception e) {
            return generateDefaultBody();
        }
    }

    private String getHtmlPath(String line) {
        return HTML_DIR + line.split(" ")[1];
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
