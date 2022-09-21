package service;

import db.Database;
import model.User;
import error.DuplicatedUserException;
import error.FailedLoginException;

public class UserService {
    private static UserService instance = new UserService();

    private UserService() {

    }

    public static UserService getInstance() {
        return instance;
    }

    public User signUp(String userId, String password, String name, String email) {
        if (Database.findUserById(userId) != null) {
            throw new DuplicatedUserException("이미 존재하는 user 입니다.");
        }
        User user = User.of(userId, password, name, email);
        Database.addUser(user);
        return user;
    }

    public boolean login(String userId, String password){
        User user = Database.findUserById(userId);
        if(user == null || !user.getPassword().equals(password)){
            throw new FailedLoginException("id나 password 를 잘못 입력하셨습니다");
        }
        return true;
    }
}
