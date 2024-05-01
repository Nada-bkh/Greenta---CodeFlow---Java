package com.example.greenta.Models;


import com.example.greenta.Utils.Type;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String password;
    private Type roles;
    private Boolean is_active;
    private Boolean is_banned;

    public User() {
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int id, String firstname, String lastname, String email, String phone, String password, Type roles, boolean is_banned) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roles = roles;
        this.is_banned = is_banned;

    }

    public User(String firstname, String lastname, String email, String phone, String password, Type roles) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roles = roles;

    }

    public User(int id, String firstname, String lastname, String email, String phone, String password, String type) {
        this.id=id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Type getRoles() {
        return roles;
    }

    public void setRoles(Type roles) {
        this.roles = roles;
    }

    public Boolean getIsActive() {
        return is_active;
    }

    public void setIsActive(Boolean is_active) {
        this.is_active = is_active;
    }

    public Boolean getIsBanned() {
        return is_banned;
    }

    public void setIsBanned(Boolean is_banned) {
        this.is_banned = is_banned;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", is_active=" + is_active +
                ", is_banned=" + is_banned +
                '}';
    }
}