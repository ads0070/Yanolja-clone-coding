package com.example.demo.src.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostRoomReq {
    private String name;
    private int accommodation_id;
    private int standard_personnel;
    private int maximum_personnel;
    private String basic_info;
    private String reservation_notice;
}
