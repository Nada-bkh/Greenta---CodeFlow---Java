package Interfaces;

import Exceptions.EmptyFieldException;
import Exceptions.IncorrectPasswordException;
import Exceptions.InvalidEmailException;
import Exceptions.UserNotFoundException;
import Models.User;

import java.util.List;

public interface UserInterface {
    public void addUser(User p)throws EmptyFieldException, InvalidEmailException, IncorrectPasswordException;

    public void updateUser( User p)throws EmptyFieldException, InvalidEmailException, IncorrectPasswordException, UserNotFoundException;

    public void deleteUser(User p);

    public List<User> getUsers();

    public User getUserbyID(int id);
    public User getUserbyEmail(String email);
}
