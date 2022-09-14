package http;

public class Body {
    public String content = "";

    public void append(String line) {
        // Todo: Doubling 가능할까?
        content = content + line;
    }

    public void prn() {
        System.out.println(content);
    }
}
