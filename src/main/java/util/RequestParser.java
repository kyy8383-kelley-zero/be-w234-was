package util;

import exception.HttpErrorMessage;
import exception.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public static String getPath(String firstLine) {
        if (firstLine == null || firstLine.equals("")) {
            throw new HttpException(HttpErrorMessage.EMPTY_REQUEST);
        }

        String[] token = firstLine.split(" ");

        if (token.length != 3) {
            throw new HttpException(HttpErrorMessage.INVALID_REQUEST);
        }

        logger.debug("protocol : {}", token[0]);
        logger.debug("path : {}", token[1]);
        return token[1];
    }
}
