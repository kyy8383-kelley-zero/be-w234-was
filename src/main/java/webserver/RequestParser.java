package webserver;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.google.common.base.Charsets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    public String method;
    public String path;
    public String version;
    public Map<String, String> headers;

    public RequestParser(InputStream in) throws Exception {
        init(in);
    }

    private void init(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));

        String requestLine = br.readLine();
        parseRequestLine(requestLine);
        parseHeaders(br);
    }

    private void parseRequestLine(String str) {
        String[] splits = str.split(" ");

        this.method = splits[0];
        this.path = splits[1];
        this.version = splits[2];
    }

    private void parseHeaders(BufferedReader br) throws IOException {
        this.headers = new HashMap<>();
        String line;

        while (StringUtils.isNotBlank(line = br.readLine())) {
            String[] splits = line.split(":", 2);
            this.headers.put(splits[0].trim().toLowerCase(), splits[1].trim().toLowerCase());
        }
    }
}
