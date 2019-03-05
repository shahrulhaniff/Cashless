package com.myraxsoft.cashless;

public class User {
    private String first_name, last_name, email, pwd;

    public User(String create_fname, String create_lname, String create_email, String create_pwd) {
        this.first_name = create_fname;
        this.last_name = create_lname;
        this.email = create_email;
        this.pwd = create_pwd;
    }


    public String getFirstName() { return first_name; }
    public String getLastName() { return last_name; }
    public String getEmail() { return email; }
}
