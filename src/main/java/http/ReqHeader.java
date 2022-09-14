package http;

import java.util.HashMap;

public class ReqHeader {
    public String method;
    public String url;
    public String httpVersion;
    public HashMap<String, String> entity = new HashMap<>();

    // 1개의 요청 -> 1개의 HttpPacket -> 1개의 스레드 이므로 clear로 재활용을 기대하기는 어려울듯?
    // 아마도 필요없는 메서드
    public void clear() {
        method = "";
        url = "";
        httpVersion = "";
        entity.clear();
    }

    public void prn() {
        System.out.printf("%s %s %s\n", method, url, httpVersion);
        entity.forEach((key, value) -> {
            System.out.println(key + " " + value);
        });
    }
}
