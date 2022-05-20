package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class GetWishRes {
    private int wish_id;
    private int acc_id;
    private String acc_name;
    private String short_notice;
    private String event_notice;
    private String short_way_to_come;
    private int standard_personnel;
    private int maximum_personnel;
    private Time check_in_time;
    private int price;
    private int selling_price;
    private int discount_rate;
    private int review_count;
    private float review_avg;
    private String storage_name;
    private String path;
}
