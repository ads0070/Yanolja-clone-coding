package com.example.demo.src.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KakaoToken {
    private String access_token;
    private String refresh_token;
}
