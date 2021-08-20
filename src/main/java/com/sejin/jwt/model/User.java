package com.sejin.jwt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String role; // USER,ADMIN

    public List<String> getRoleList() {
        if (this.role.length()>0){
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    @Builder
    public User(long id, String username, String password, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = roles;
    }
}
