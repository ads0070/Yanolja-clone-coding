package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class PostReserveReq {
    private int user_id;
    private int accommodation_room_id;
    private Date check_in_date;
    private Date check_out_date;
    private String payment_type;
    private int amount_of_payment;
}
