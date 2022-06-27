package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBasketReq {
    private int accommodation_room_id;
    private int user_id;
    private String check_in_date;
    private String check_out_date;
}
