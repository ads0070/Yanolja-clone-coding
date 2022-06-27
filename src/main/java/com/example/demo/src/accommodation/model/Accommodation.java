package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Accommodation {
    private int id;
    private int views;
    private String name;
    private String short_notice;
    private String event_notice;
    private String store_phone_number;
    private int advertising_grade;
    private int pick_up_availability;
    private int qr_check_in_availability;
    private String short_way_to_come;
    private String main_category;
    private String sub_category;
}
