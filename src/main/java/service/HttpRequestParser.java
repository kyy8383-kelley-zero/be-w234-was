package service;

import model.http.HttpRequestHeader;

public interface HttpRequestParser {
    HttpRequestHeader parseHttpRequestHeader(String line);
}
