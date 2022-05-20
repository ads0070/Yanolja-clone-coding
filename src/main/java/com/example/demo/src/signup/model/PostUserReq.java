package com.example.demo.src.signup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String email;
    private String password;
    private String phone_number;
    private String nickname;
    private LocalDateTime create_at;
}
