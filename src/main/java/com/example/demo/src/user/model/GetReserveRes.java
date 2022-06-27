package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetReserveRes {
    private int reservation_id;
    private int accommodation_id;
    private int room_id;
    private String acc_name;
    private String room_name;
    private String storage_name;
    private String path;
    private Time check_in_time;
    private Time check_out_time;
    private Timestamp create_at;
    private Date check_in_date;
    private Date check_out_date;
}
