package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class GetRoomRes {
    private int id;
    private String name;
    private int standard_personnel;
    private int maximum_personnel;
    private String basic_info;
    private String reservation_notice;
    private int lodging_price;
    private int lodging_selling_price;
    private int lodging_discount_rate;
    private Time check_in_time;
    private Time check_out_time;
    private int total_count;
    private int book_count;
    private int remain_count;
    private int dayuse_price;
    private int dayuse_selling_price;
    private int dayuse_discount_rate;
    private String storage_name;
    private String path;
}
