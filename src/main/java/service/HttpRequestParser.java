package service;

import model.http.HttpRequestHeader;

public interface HttpRequestParser {
    HttpRequestHeader getHttpRequestHeader(String line);
}
