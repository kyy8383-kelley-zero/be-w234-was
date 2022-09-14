package http;

import java.io.*;

public class RequestPacket {
    public ReqHeader header = new ReqHeader();
    public Body body = new Body();
    private BufferedReader br;
    private String line;
    private String[] lineitem;

    public void prn() {
        header.prn();
        body.prn();
    }

    public RequestPacket(InputStream in) {
        // > GET /index.html HTTP/1.1
        // > Host: ark10806.iptime.org
        // > User-Agent: curl/7.82.0
        // > Accept: */*
        try {
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            parse();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse() {
        parseGeneralHeader();
        parseSpecialHeader();
        parseEntityHeader();
        parseBody();
    }

    private void parseGeneralHeader() {

    }
    private void parseSpecialHeader() {
        try {
            lineitem = br.readLine().split(" ");
            this.header.method = lineitem[0];
            this.header.url = lineitem[1];
            this.header.httpVersion = lineitem[2];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void parseEntityHeader() {
        try {
            line = br.readLine();
            while (!line.equals("")) {
                lineitem = line.split(" ");
                this.header.entity.put(lineitem[0], lineitem[1]);
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseBody() {
        try {
            if (br.ready()) {
                line = br.readLine();
                while (line != null) {
                    this.body.append("line\n");
                    br.readLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
