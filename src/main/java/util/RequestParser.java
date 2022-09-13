package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestParser {
    private static final Logger  logger = LoggerFactory.getLogger(RequestParser.class);

    public static String getPath(String firstLine) {
        System.out.println(firstLine);
        String[] splited = firstLine.split(" ");
        String path = splited[1];
        logger.debug("request path: {}", path);
        return path;
    }
}
