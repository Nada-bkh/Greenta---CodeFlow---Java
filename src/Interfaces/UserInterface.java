package Interfaces;

import Models.User;

import java.util.List;

public interface UserInterface {
    public void addUser(User p);

    public void updateUser( User p);

    public void deleteUser(User p);

    public List<User> getUsers();

    public User getUserbyID(int id);
}
