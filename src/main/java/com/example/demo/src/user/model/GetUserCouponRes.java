package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetUserCouponRes {
    private int id;
    private String name;
    private Timestamp deadline_to_start_using;
    private Timestamp deadline_for_use;
    private int discount_amount;
    private int discount_rate;
    private int maximum_discount_amount;
    private String terms_of_use;
    private String excluded_accommodation;
}