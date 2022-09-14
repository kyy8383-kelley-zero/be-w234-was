package webserver;

import db.Database;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Backend {
    private Map<String, Runnable> path = new HashMap<>();
    private String method;
    private Map<String, String> params;
    private Database db = new Database();
    private byte[] response;

    public Backend() {
        path.put("/", () -> home());
        path.put("/index.html", () -> home());
        path.put("/user/form.html", () -> userCreateform());
        path.put("/user/create", () -> userCreate());
    }

    public void route(String method, String url, Map params) {
        setMethod(method);
        setParams(params);
        try {
            path.get(url).run();
        } catch (Exception e) {
            System.out.println("[Err] " + url);
        }
    }

    public byte[] getResponse() {
        return response;
    }

    private void setMethod(String method) {
        this.method = method;
    }

    private void setParams(Map params) {
        this.params = params;
    }

    private void home() {
        try {
            this.response = Files.readAllBytes(new File("./webapp/index.html").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void userCreateform() {
        try {
            this.response = Files.readAllBytes(new File("./webapp/user/form.html").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void userCreate() {
        // http://localhost:8080/user/create?userId=a&password=b&name=c&email=d%40v.c
        HashSet<String> defined_methods = new HashSet<>(Arrays.asList("GET"));
        if (!defined_methods.contains(this.method))
            this.response = "[Failed] 500".getBytes();
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        db.addUser(user);
        this.response = "[Completed] sign-up".getBytes();
    }
}
