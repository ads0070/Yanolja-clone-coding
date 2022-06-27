package com.example.demo.src.accommodation;

import com.example.demo.src.accommodation.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

@Repository
public class AccDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetAccRes> getAccsByCategory(String category, String sigungu) {
        String getAccsByCategoryQuery = "SELECT accommodation_id, name, event_notice, advertising_grade, qr_check_in_availability, room_id,\n" +
                "       short_way_to_come, basic_info, storage_name, path, review_count, review_avg, lodging_price,\n" +
                "       lodging_selling_price, check_in_time, dayuse_price, dayuse_selling_price, maximum_time\n" +
                "FROM (SELECT ac.accommodation_id, ac.name, ac.event_notice, ac.advertising_grade, ac.qr_check_in_availability, ac.room_id,\n" +
                "        ac.short_way_to_come, ac.basic_info, arp.storage_name, arp.path, review_count, review_avg\n" +
                "FROM (SELECT a.accommodation_id, sigungu, a.name, event_notice, advertising_grade, qr_check_in_availability,\n" +
                "             short_way_to_come, main_category, accommodation_room.id room_id, basic_info, review_count, review_avg\n" +
                "      FROM (SELECT a.accommodation_id, sigungu, name, event_notice, advertising_grade, qr_check_in_availability,\n" +
                "                   short_way_to_come, main_category, review_count, review_avg\n" +
                "            FROM (SELECT accommodation_id, name, event_notice, advertising_grade, qr_check_in_availability,\n" +
                "                   short_way_to_come, main_category,\n" +
                "                         count(*) review_count,\n" +
                "                         round((sum(review.kindness_rating)\n" +
                "                             + sum(review.cleanliness_rating) + sum(review.conveniencerrating)\n" +
                "                             + sum(review.equipment_satisfaction_rating)) / (count(*) * 4), 1) review_avg\n" +
                "                  from review JOIN accommodation a\n" +
                "                      on a.id = review.accommodation_id\n" +
                "                  group by accommodation_id) as a JOIN accommodation_addr aa\n" +
                "                on a.accommodation_id = aa.accommodation_id\n" +
                "            where main_category = ? and sigungu = ?\n" +
                "            group by a.accommodation_id) as a JOIN accommodation_room\n" +
                "          on a.accommodation_id = accommodation_room.accommodation_id\n" +
                "      group by a.accommodation_id) as ac JOIN accommodation_representative_picture arp\n" +
                "          on ac.accommodation_id = arp.accommodation_id) as ac LEFT JOIN\n" +
                " (SELECT l.accommodation_room_id, l.price lodging_price, l.selling_price lodging_selling_price, l.check_in_time,\n" +
                "             d.price dayuse_price, d.selling_price dayuse_selling_price, d.maximum_time\n" +
                "      FROM lodging l LEFT JOIN dayuse d\n" +
                "    on l.accommodation_room_id = d.accommodation_room_id) as ld\n" +
                "    on ld.accommodation_room_id = ac.room_id\n" +
                "order by ac.advertising_grade desc";
        Object[] getAccsByCategoryParams = new Object[]{category, sigungu};
        return this.jdbcTemplate.query(getAccsByCategoryQuery,
                (rs, rowNum) -> new GetAccRes(
                        rs.getInt("accommodation_id"),
                        rs.getString("name"),
                        rs.getString("event_notice"),
                        rs.getInt("advertising_grade"),
                        rs.getInt("qr_check_in_availability"),
                        rs.getInt("room_id"),
                        rs.getString("short_way_to_come"),
                        rs.getString("basic_info"),
                        rs.getString("storage_name"),
                        rs.getString("path"),
                        rs.getInt("review_count"),
                        rs.getFloat("review_avg"),
                        rs.getInt("lodging_price"),
                        rs.getInt("lodging_selling_price"),
                        rs.getTime("check_in_time"),
                        rs.getInt("dayuse_price"),
                        rs.getInt("dayuse_selling_price"),
                        rs.getInt("maximum_time")
                ),
                getAccsByCategoryParams);
    }

    public List<GetAccReviewRes> getAccReviews(int accId) {
        String getAccReivewsQuery = "# 후기\n" +
                "SELECT rr.id as review_id, nickname, name room_name, content, kindness_rating, cleanliness_rating,\n" +
                "       conveniencerrating, equipment_satisfaction_rating, create_at, storage_name,\n" +
                "       sa_id answer_id, sa_context answer_context, sa_create_at answer_create_at\n" +
                "FROM (SELECT ri.id, ri.ui, ri.name, u.nickname\n" +
                "      FROM (SELECT ri.id, ri.user_id ui, ar.name\n" +
                "            FROM reservation_info ri\n" +
                "                     JOIN accommodation_room ar on ar.id = ri.accommodation_room_id) ri\n" +
                "               JOIN user u\n" +
                "                    on u.id = ri.ui) uu\n" +
                "         JOIN (SELECT *\n" +
                "               FROM (SELECT r.id, r.accommodation_id, r.user_id, r.reservation_info_id, r.content, r.kindness_rating,\n" +
                "                            r.cleanliness_rating, r.conveniencerrating, r.equipment_satisfaction_rating, r.create_at,\n" +
                "                            r.is_best, rp.original_name, rp.storage_name, rp.path\n" +
                "                     FROM review r\n" +
                "                              LEFT JOIN review_picture rp\n" +
                "                                        on r.id = rp.review_id\n" +
                "                     WHERE r.accommodation_id = ?) as r\n" +
                "                        LEFT JOIN (SELECT id sa_id, review_id, store_owner_id, context sa_context, create_at sa_create_at\n" +
                "                                   FROM store_answer) sa\n" +
                "                                  on r.id = sa.review_id) as rr on uu.ui = rr.user_id and rr.reservation_info_id = uu.id\n" +
                "ORDER BY is_best desc, rr.create_at";
        int getAccReivewsParams = accId;
        return this.jdbcTemplate.query(getAccReivewsQuery,
                (rs, rowNum) -> new GetAccReviewRes(
                        rs.getInt("review_id"),
                        rs.getString("nickname"),
                        rs.getString("room_name"),
                        rs.getString("content"),
                        rs.getInt("kindness_rating"),
                        rs.getInt("cleanliness_rating"),
                        rs.getInt("conveniencerrating"),
                        rs.getInt("equipment_satisfaction_rating"),
                        rs.getTimestamp("create_at"),
                        rs.getString("storage_name"),
                        rs.getInt("answer_id"),
                        rs.getString("answer_context"),
                        rs.getTimestamp("answer_create_at")
                ),
                getAccReivewsParams);
    }

    public List<GetRoomRes> getAccRooms(int accId, Date startDate, Date endDate) {
        String betweenDate = " '" + startDate + "' and '" + endDate + "'\n";
        String getAccRoomsQuery = "select id, name, standard_personnel, maximum_personnel, basic_info, reservation_notice, lodging_price, lodging_selling_price,\n" +
                "       lodging_discount_rate, check_in_time, check_out_time, total_count, book_count, total_count-book_count remain_count, dayuse_price,\n" +
                "       dayuse_selling_price, dayuse_discount_rate, storage_name, path\n" +
                "FROM (SELECT acr.id, acr.name, acr.standard_personnel, acr.maximum_personnel, acr.basic_info, acr.reservation_notice,\n" +
                "       arp.original_name, arp.storage_name, arp.path\n" +
                "      FROM accommodation_room acr JOIN accommodation_room_picture arp\n" +
                "          on acr.id = arp.accommodation_room_id\n" +
                "      where accommodation_id = ?) as ri JOIN ( SELECT ar.accommodation_room_id, ar.lodging_price, ar.lodging_selling_price,\n" +
                "                                                          ar.lodging_discount_rate, ar.check_in_time, ar.check_out_time, ar.total_count,\n" +
                "                                                          ar.dayuse_price, ar.dayuse_selling_price,\n" +
                "                                                          ar.dayuse_max_time, ar.dayuse_discount_rate, b.book_count\n" +
                "                                             FROM (SELECT l.accommodation_room_id, l.price lodging_price, l.selling_price lodging_selling_price,\n" +
                "                                                          l.discount_rate lodging_discount_rate, l.check_in_time, l.check_out_time, l.total_count,\n" +
                "                                                          d.price dayuse_price, d.selling_price dayuse_selling_price,\n" +
                "                                                          d.maximum_time dayuse_max_time, d.discount_rate dayuse_discount_rate\n" +
                "                                                   FROM (SELECT * FROM lodging) as l JOIN (SELECT * FROM dayuse) as d\n" +
                "                                                       on l.accommodation_room_id = d.accommodation_room_id) as ar\n" +
                "                                                 JOIN (SELECT accommodation_room_id, count(*) book_count\n" +
                "                                                       FROM reservation_info\n" +
                "                                                       where DATE(check_in_date) between" + betweenDate +
                "                                                       group by accommodation_room_id) as b\n" +
                "                                                     on ar.accommodation_room_id = b.accommodation_room_id) as ci\n" +
                "          on ri.id = ci.accommodation_room_id";
        int getAccRoomsParams = accId;
        return this.jdbcTemplate.query(getAccRoomsQuery,
                (rs,rowNum) -> new GetRoomRes(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("standard_personnel"),
                        rs.getInt("maximum_personnel"),
                        rs.getString("basic_info"),
                        rs.getString("reservation_notice"),
                        rs.getInt("lodging_price"),
                        rs.getInt("lodging_selling_price"),
                        rs.getInt("lodging_discount_rate"),
                        rs.getTime("check_in_time"),
                        rs.getTime("check_out_time"),
                        rs.getInt("total_count"),
                        rs.getInt("book_count"),
                        rs.getInt("remain_count"),
                        rs.getInt("dayuse_price"),
                        rs.getInt("dayuse_selling_price"),
                        rs.getInt("dayuse_discount_rate"),
                        rs.getString("storage_name"),
                        rs.getString("path")
                ),
                getAccRoomsParams);
    }

    public int createAcc(PostAccInfoReq postAccInfoReq){
        String createAccQuery = "insert into accommodation (name, store_phone_number, create_at, main_category, sub_category) VALUES (?,?,?,?,?)";
        Object[] createAccParams = new Object[]{postAccInfoReq.getName(), postAccInfoReq.getStore_phone_number(), postAccInfoReq.getCreate_at(),
                postAccInfoReq.getMain_category(), postAccInfoReq.getSub_category()};
        return this.jdbcTemplate.update(createAccQuery, createAccParams);
    }

    public int getLastInsertId() {
        String getLastInsertIdQuery = "SELECT LAST_INSERT_ID()";
        return this.jdbcTemplate.queryForObject(getLastInsertIdQuery, Integer.class);
    }

    public int insertConnectAcc(int accId, int ownerId) {
        String insertConnectAccQuery = "insert into owner_acc_connect (owner_id, accommodation_id) VALUES (?,?)";
        Object[] insertConnectAccParams = new Object[]{ownerId, accId};
        return this.jdbcTemplate.update(insertConnectAccQuery, insertConnectAccParams);
    }

    public int insertAddr(PostAccAddrReq postAccAddrReq) {
        String insertAddrQuery = "insert into accommodation_addr (accommodation_id, sido, sigungu, eubmyeondong, ri, beonji, detail, create_at) VALUES (?,?,?,?,?,?,?,?)";
        Object[] insertAddrParams = new Object[]{postAccAddrReq.getAccommodation_id(), postAccAddrReq.getSido(), postAccAddrReq.getSigungu(),
                postAccAddrReq.getEubmyeondong(), postAccAddrReq.getRi(), postAccAddrReq.getBeonji(),
                postAccAddrReq.getDetail(), postAccAddrReq.getCreate_at()};
        return this.jdbcTemplate.update(insertAddrQuery, insertAddrParams);
    }

    public int modifyAccInfo(PatchAccInfoReq patchAccInfoReq) {
        String modifyAccInfoQuery = "update accommodation set name = ?, short_notice = ?, event_notice = ?, store_phone_number = ?,\n" +
                "                         advertising_grade = ?, pick_up_availability = ?, qr_check_in_availability = ?,\n" +
                "                         update_at = ?, short_way_to_come = ?, main_category = ?, sub_category = ? where id = ?";
        Object[] modifyAccInfoParams = new Object[]{patchAccInfoReq.getName(), patchAccInfoReq.getShort_notice(), patchAccInfoReq.getEvent_notice(), patchAccInfoReq.getStore_phone_number(),
                patchAccInfoReq.getAdvertising_grade(), patchAccInfoReq.getPick_up_availability(), patchAccInfoReq.getQr_check_in_availability(), patchAccInfoReq.getUpdate_at(),
                patchAccInfoReq.getShort_way_to_come(), patchAccInfoReq.getMain_category(), patchAccInfoReq.getSub_category(), patchAccInfoReq.getId()};
        return this.jdbcTemplate.update(modifyAccInfoQuery, modifyAccInfoParams);
    }

    public int modifyAccAddr(PatchAccAddrReq patchAccAddrReq) {
        String modifyAccAddrQuery = "update accommodation_addr set sido = ?, sigungu = ?, eubmyeondong = ?, ri = ?, beonji = ?, detail = ?, update_at = ? where id = ?";
        Object[] modifyAccAddrParams = new Object[]{patchAccAddrReq.getSido(), patchAccAddrReq.getSigungu(),
                patchAccAddrReq.getEubmyeondong(), patchAccAddrReq.getRi(), patchAccAddrReq.getBeonji(),
                patchAccAddrReq.getDetail(), patchAccAddrReq.getUpdate_at(), patchAccAddrReq.getId()};
        return this.jdbcTemplate.update(modifyAccAddrQuery, modifyAccAddrParams);
    }

    public int modifyRoom(PatchRoomReq patchRoomReq) {
        String modifyRoomQuery = "update accommodation_room set name = ?, standard_personnel = ?, maximum_personnel = ?, basic_info = ?, reservation_notice = ? where id = ?";
        Object[] modifyRoomParams = new Object[]{patchRoomReq.getName(), patchRoomReq.getStandard_personnel(), patchRoomReq.getMaximum_personnel(),
                patchRoomReq.getBasic_info(), patchRoomReq.getReservation_notice(), patchRoomReq.getId()};
        return this.jdbcTemplate.update(modifyRoomQuery, modifyRoomParams);
    }

    public int insertRoom(PostRoomReq postRoomReq) {
        String insertRoomQuery = "insert into accommodation_room (name, accommodation_id, standard_personnel, maximum_personnel, basic_info, reservation_notice) VALUES (?,?,?,?,?,?)";
        Object[] insertRoomParams = new Object[]{postRoomReq.getName(), postRoomReq.getAccommodation_id(), postRoomReq.getStandard_personnel(),
                postRoomReq.getMaximum_personnel(), postRoomReq.getBasic_info(), postRoomReq.getReservation_notice()};
        return this.jdbcTemplate.update(insertRoomQuery, insertRoomParams);
    }
}
