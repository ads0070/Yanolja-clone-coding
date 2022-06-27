package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PatchAccInfoReq {
    private int id;
    private String name;
    private String short_notice;
    private String event_notice;
    private String store_phone_number;
    private int advertising_grade;
    private int pick_up_availability;
    private int qr_check_in_availability;
    private LocalDateTime update_at;
    private String short_way_to_come;
    private String main_category;
    private String sub_category;
}
