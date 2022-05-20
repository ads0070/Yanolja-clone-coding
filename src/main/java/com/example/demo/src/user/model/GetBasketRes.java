package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class GetBasketRes {
    private int id;
    private int accommodation_id;
    private String acc_name;
    private String sido;
    private String sigungu;
    private String ri;
    private String beonji;
    private String room_name;
    private String basic_info;
    private int standard_personnel;
    private int maximum_personnel;
    private Time check_in;
    private Time check_out;
    private int price;
    private int selling_price;
    private int discount_rate;
    private String storage_name;
    private String path;
}
