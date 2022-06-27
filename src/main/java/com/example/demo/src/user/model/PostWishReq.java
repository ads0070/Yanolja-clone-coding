package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostWishReq {
    private int user_id;
    private int accommodation_id;
    private LocalDateTime create_at;
}