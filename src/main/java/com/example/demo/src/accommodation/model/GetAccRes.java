package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class GetAccRes {
    private int accommodation_id;
    private String name;
    private String event_notice;
    private int advertising_grade;
    private int qr_check_in_availability;
    private int room_id;
    private String short_way_to_come;
    private String basic_info;
    private String storage_name;
    private String path;
    private int review_count;
    private float review_avg;
    private int lodging_price;
    private int lodging_selling_price;
    private Time check_in_time;
    private int dayuse_price;
    private int dayuse_selling_price;
    private int maximum_time;
}
