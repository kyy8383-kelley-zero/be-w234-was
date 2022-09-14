package webserver;

import db.Database;
import model.User;

import java.util.*;

public class Backend {
    Map<String, Runnable> path = new HashMap<>();
    private String method;
    private Map<String, String> params;
    private Database db = new Database();

    public Backend() {
        path.put("/user/create", () -> userCreate());
    }

    public void get(String method, String url, String queryString) {
        setMethod(method);
        setParams(queryString);
        path.get(url).run();
    }

    private void setMethod(String method) {
        this.method = method;
    }

    private void setParams(String queryString) {
        for (String param : queryString.split("&")) {
            String[] kv = param.split("=");
            this.params.put(kv[0], kv[1]);
        }
    }

    private int userCreate() {
        // http://localhost:8080/user/create?userId=a&password=b&name=c&email=d%40v.c
        HashSet<String> defined_methods = new HashSet<>(Arrays.asList("GET"));
        if (!defined_methods.contains(this.method))
            return 404;
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        db.addUser(user);
        return 200;
    }
}
