package Interfaces;

import Exceptions.*;
import Models.User;

import java.util.List;

public interface UserInterface {
    public void addUser(User p) throws EmptyFieldException, InvalidEmailException, IncorrectPasswordException, InvalidPhoneNumberException;

    public void updateUser( User p) throws EmptyFieldException,InvalidPhoneNumberException, InvalidEmailException, IncorrectPasswordException, UserNotFoundException;

    public void deleteUser(User p);

    public List<User> getUsers();

    public User getUserbyID(int id) throws UserNotFoundException;
    public User getUserbyEmail(String email) throws UserNotFoundException;
}
