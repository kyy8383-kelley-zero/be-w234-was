package controller;

import error.DuplicatedUserException;
import error.FailedLoginException;
import model.http.request.HttpRequest;
import model.http.request.Method;
import model.http.response.HttpResponse;
import model.http.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.RequestHandler;


public class UserController {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final UserController instance = new UserController();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    private final UserService userService = UserService.getInstance();

    public HttpResponse response(HttpRequest httpRequest){
        if (httpRequest.getUri().contains("create") && httpRequest.getMethod().equals(Method.POST))
            return singUp(httpRequest);
        else if (httpRequest.getUri().contains("login") && httpRequest.getMethod().equals(Method.POST)) {
            return login(httpRequest);
        } else return new HttpResponse.HttpResponseBuilder().build();
    }


    public HttpResponse singUp(HttpRequest httpRequest){
        String userId = httpRequest.getBody().get("userId");
        String password = httpRequest.getBody().get("password");
        String name = httpRequest.getBody().get("name");
        String email = httpRequest.getBody().get("email");

        try {
            userService.signUp(userId, password, name, email);
            log.debug("sign up success!");
            return new HttpResponse.HttpResponseBuilder()
                    .status(Status.FOUND)
                    .headers("Location", "/index.html")
                    .build();
        } catch (DuplicatedUserException e) {
            log.error("sign up failed! : " + e.getMessage(), e);
            return new HttpResponse.HttpResponseBuilder()
                    .status(Status.FOUND)
                    .headers("Location", "/signup_error.html")
                    .build();
        }
    }


    public HttpResponse login(HttpRequest httpRequest){
        String id = httpRequest.getBody().get("userId");
        String pw = httpRequest.getBody().get("password");

        try {
            userService.login(id, pw);
            log.debug("login success!");
            return new HttpResponse.HttpResponseBuilder()
                    .status(Status.OK)
                    .headers("Location", "/index.html")
                    .headers("Set-Cookie", "logined=true; Path=/")
                    .build();

        } catch (FailedLoginException e) {
            log.error("login failed! : " + e.getMessage(), e);
            return new HttpResponse.HttpResponseBuilder()
                    .status(Status.FOUND)
                    .headers("Location", "/user/login_failed.html")
                    .build();
        }
    }
}
