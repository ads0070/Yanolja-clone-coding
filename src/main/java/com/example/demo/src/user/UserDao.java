package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from user";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("nickname"))
        );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from user where email = ?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("nickname")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from user where id = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("nickname")),
                getUserParams);
    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update user set nickname = ? where id = ?";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickname(), patchUserReq.getId()};
        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int checkPwd(PatchPwdReq patchPwdReq){
        String checkPwdQuery = "select if((select password from user where id = ?) = ?, 1, 0)";
        Object[] checkPwdParams = new Object[]{patchPwdReq.getId(), patchPwdReq.getOriginPwd()};
        return this.jdbcTemplate.queryForObject(checkPwdQuery,int.class,checkPwdParams);
    }

    public int modifyUserPwd(PatchPwdReq patchPwdReq){
        String modifyUserPwdQuery = "update user set password = ? where id = ? ";
        Object[] modifyUserPwdParams = new Object[]{patchPwdReq.getNewPwd(), patchPwdReq.getId()};
        return this.jdbcTemplate.update(modifyUserPwdQuery,modifyUserPwdParams);
    }

    public List<GetUserCouponRes> getUserCoupon(int userId){
        String getUserCouponQuery = "SELECT cc.id, c.name, c.deadline_to_start_using, " +
                "c.deadline_for_use, c.discount_amount, c.discount_rate, " +
                "c.maximum_discount_amount, c.terms_of_use, c.excluded_accommodation " +
                "FROM (select * from coupon_connection where user_id = ?) as cc JOIN (select * from coupon) as c " +
                "on cc.coupon_id = c.id";
        int getUserCouponParams = userId;
        return this.jdbcTemplate.query(getUserCouponQuery,
                (rs, rowNum) -> new GetUserCouponRes(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("deadline_to_start_using"),
                        rs.getTimestamp("deadline_for_use"),
                        rs.getInt("discount_amount"),
                        rs.getInt("discount_rate"),
                        rs.getInt("maximum_discount_amount"),
                        rs.getString("terms_of_use"),
                        rs.getString("excluded_accommodation")),
                getUserCouponParams);
    }

    public int createWish(PostWishReq postWishReq){
        String createWishQuery = "insert into wish_list (user_id, accommodation_id, create_at) VALUES (?,?,?)";
        Object[] createWishParams = new Object[]{postWishReq.getUser_id(), postWishReq.getAccommodation_id(), postWishReq.getCreate_at()};
        return this.jdbcTemplate.update(createWishQuery, createWishParams);
    }

    public int checkWish(PostWishReq postWishReq){
        String checkWishQuery = "select exists(select id from wish_list where user_id = ? and accommodation_id = ?)";
        Object[] checkWishParams = new Object[]{postWishReq.getUser_id(), postWishReq.getAccommodation_id()};
        return this.jdbcTemplate.queryForObject(checkWishQuery,
                int.class,
                checkWishParams);
    }

    public List<GetWishRes> getWishList(int userId){
        String getWishListQuery = "select wish_id, acc_id, acc_name, short_notice, event_notice, short_way_to_come, standard_personnel, maximum_personnel,\n" +
                "       check_in_time, price, selling_price, discount_rate, review_count, review_avg, storage_name, path\n" +
                "from (select wish_id, acc_id, acc_name, a.short_notice, a.event_notice, a.short_way_to_come, arp.storage_name, arp.path\n" +
                "      from (select a.id acc_id, a.name acc_name, a.short_notice, a.event_notice, a.short_way_to_come, wl.create_at, wl.id wish_id\n" +
                "            from wish_list wl join accommodation a\n" +
                "                on a.id = wl.accommodation_id\n" +
                "            where user_id = ?) a join accommodation_representative_picture arp\n" +
                "                on acc_id = arp.accommodation_id) f join (select room_id, arl.accommodation_id, standard_personnel, maximum_personnel, arl.check_in_time,\n" +
                "                                                                 arl.price, arl.selling_price, arl.discount_rate, arr.review_count, arr.review_avg\n" +
                "                                                          from (select ar.id room_id, ar.accommodation_id, ar.standard_personnel, ar.maximum_personnel, l.* from accommodation_room ar join lodging l\n" +
                "                                                              on ar.id = l.accommodation_room_id) arl left join (select *, count(*) review_count, round((sum(review.kindness_rating)+sum(review.cleanliness_rating)\n" +
                "                                                                       +sum(review.conveniencerrating)+sum(review.equipment_satisfaction_rating))/(count(*)*4),1) review_avg\n" +
                "                                                                                                                 from review group by accommodation_id) as arr\n" +
                "                                                                  on arl.accommodation_id = arr.accommodation_id\n" +
                "                                                          group by arl.accommodation_id) s\n" +
                "                    on f.acc_id = s.accommodation_id";
        int getWishListParams = userId;
        return this.jdbcTemplate.query(getWishListQuery,
                (rs, rowNum) -> new GetWishRes(
                        rs.getInt("wish_id"),
                        rs.getInt("acc_id"),
                        rs.getString("acc_name"),
                        rs.getString("short_notice"),
                        rs.getString("event_notice"),
                        rs.getString("short_way_to_come"),
                        rs.getInt("standard_personnel"),
                        rs.getInt("maximum_personnel"),
                        rs.getTime("check_in_time"),
                        rs.getInt("price"),
                        rs.getInt("selling_price"),
                        rs.getInt("discount_rate"),
                        rs.getInt("review_count"),
                        rs.getFloat("review_avg"),
                        rs.getString("storage_name"),
                        rs.getString("path")
                ),
                getWishListParams);
    }

    public int deleteWish(int wishId){
        String deleteWishQuery = "delete from wish_list where id = ?";
        int deleteWishParams = wishId;
        return this.jdbcTemplate.update(deleteWishQuery,deleteWishParams);
    }

    public List<GetPointRes> getPoint(int userId){
        String getPointQuery = "SELECT up.id, up.name, up.point, up.deadline_for_use, up.create_at\n" +
                "FROM user u join user_point up\n" +
                "    on u.id = up.user_id\n" +
                "where up.user_id = ? and u.create_at >= date_add(now(), interval -1 year)";
        int getPointParams = userId;
        return this.jdbcTemplate.query(getPointQuery,
                (rs, rowNum) -> new GetPointRes(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("point"),
                        rs.getTimestamp("deadline_for_use"),
                        rs.getTimestamp("create_at")),
                getPointParams);
    }

    public int deleteReview(int reviewId){
        String deleteReivewQuery = "delete from review where id = ?";
        int deleteReivewParams = reviewId;
        return this.jdbcTemplate.update(deleteReivewQuery,deleteReivewParams);
    }

    public int checkAnswer(int reviewId){
        String checkAnswerQuery = "select IFNULL((select id from store_answer where review_id = ?), -1)";
        int checkAnswerParams = reviewId;
        return this.jdbcTemplate.queryForObject(checkAnswerQuery,
                int.class,
                checkAnswerParams);
    }

    public int deleteAnswer(int answerId){
        String deleteAnswerQuery = "delete from store_answer where id = ?";
        int deleteAnswerParams = answerId;
        return this.jdbcTemplate.update(deleteAnswerQuery,deleteAnswerParams);
    }

    public List<GetBasketRes> getBaskets(int userId){
        String getBasketsQuery = "SELECT id, adr.ac_id accommodation_id, name acc_name, sido, sigungu, ri, beonji, room_name, basic_info, min_per standard_personnel, max_per maximum_personnel, check_in, check_out, price, selling_price, discount_rate, storage storage_name, path\n" +
                "FROM (SELECT sbr.id, sbr.accommodation_id ac_id, sbr.name room_name, sbr.basic_info, sbr.standard_personnel min_per, sbr.maximum_personnel max_per,\n" +
                "             sbr.check_in_date check_in, sbr.check_out_date check_out, price, selling_price, discount_rate,\n" +
                "             arp.original_name origin, arp.storage_name storage, arp.path\n" +
                "FROM (SELECT sb.id, ar.accommodation_id, ar.name, ar.standard_personnel, ar.maximum_personnel, ar.basic_info,\n" +
                "             sb.check_in_date, sb.check_out_date, sb.accommodation_room_id, price, selling_price, discount_rate\n" +
                "      FROM shopping_basket sb\n" +
                "               JOIN (SELECT ro.id, ro.standard_personnel, ro.maximum_personnel, ro.basic_info, ro.accommodation_id,\n" +
                "                            ro.name, l.price, l.selling_price, l.discount_rate\n" +
                "                     FROM accommodation_room ro JOIN lodging l\n" +
                "    on ro.id = l.accommodation_room_id) ar\n" +
                "                    on ar.id = sb.accommodation_room_id\n" +
                "      WHERE user_id = ?) as sbr JOIN accommodation_room_picture arp\n" +
                "          on sbr.accommodation_room_id = arp.id) as room JOIN (SELECT aa.accommodation_id ac_id, a.name, aa.sido, aa.sigungu, aa.ri, aa.beonji\n" +
                "FROM accommodation a JOIN accommodation_addr aa\n" +
                "    on a.id = aa.accommodation_id) as adr\n" +
                "              on room.ac_id = adr.ac_id";
        int getBasketsParams = userId;
        return this.jdbcTemplate.query(getBasketsQuery,
                    (rs, rowNum) -> new GetBasketRes(
                            rs.getInt("id"),
                            rs.getInt("accommodation_id"),
                            rs.getString("acc_name"),
                            rs.getString("sido"),
                            rs.getString("sigungu"),
                            rs.getString("ri"),
                            rs.getString("beonji"),
                            rs.getString("room_name"),
                            rs.getString("basic_info"),
                            rs.getInt("standard_personnel"),
                            rs.getInt("maximum_personnel"),
                            rs.getTime("check_in"),
                            rs.getTime("check_out"),
                            rs.getInt("price"),
                            rs.getInt("selling_price"),
                            rs.getInt("discount_rate"),
                            rs.getString("storage_name"),
                            rs.getString("path")
                    ),
                getBasketsParams);
    }

    public int deleteBasket(int basketId) {
        String deleteBasketQuery = "delete from shopping_basket where id = ?";
        int deleteBasketId = basketId;
        return this.jdbcTemplate.update(deleteBasketQuery, deleteBasketId);
    }

    public List<GetReserveRes> getReserves(int userId, int term) {
        String getReservesQuery = "select id reservation_id, rr.*, create_at, check_in_date, check_out_date\n" +
                "from reservation_info ri LEFT JOIN (select ar.*, l.check_in_time, l.check_out_time\n" +
                "                               from (select ar.accommodation_id, ar.room_id, ar.acc_name, ar.room_name, arp.storage_name, arp.path\n" +
                "                                     from (select a.name acc_name, r.id room_id, r.name room_name, r.accommodation_id\n" +
                "                                           from accommodation a JOIN accommodation_room r\n" +
                "                                               on a.id = r.accommodation_id) ar LEFT JOIN accommodation_room_picture arp\n" +
                "                                                   on ar.room_id = arp.accommodation_room_id) ar LEFT JOIN lodging l\n" +
                "                                                       on ar.room_id = l.accommodation_room_id) as rr\n" +
                "    on ri.accommodation_room_id = rr.room_id\n" +
                "where user_id = ? and create_at >= date_add(now(), interval -? month)";
        Object[] getReservesParams = new Object[]{userId, term};
        return this.jdbcTemplate.query(getReservesQuery,
                (rs,rowNum) -> new GetReserveRes(
                        rs.getInt("reservation_id"),
                        rs.getInt("accommodation_id"),
                        rs.getInt("room_id"),
                        rs.getString("acc_name"),
                        rs.getString("room_name"),
                        rs.getString("storage_name"),
                        rs.getString("path"),
                        rs.getTime("check_in_time"),
                        rs.getTime("check_out_time"),
                        rs.getTimestamp("create_at"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date")
                ),
                getReservesParams);
    }

    public int createReserve(PostReserveReq postReserveReq) {
        String createReserveQuery = "insert into reservation_info(user_id, accommodation_room_id, check_in_date, check_out_date,\n" +
                "                             reservation_status, payment_type, amount_of_payment, create_at)\n" +
                "values (?, ?, ?, ?, 1, ?, ?, now())";
        Object[] createReserveParams = new Object[]{postReserveReq.getUser_id(), postReserveReq.getAccommodation_room_id(),
                postReserveReq.getCheck_in_date(), postReserveReq.getCheck_out_date(), postReserveReq.getPayment_type(),
                postReserveReq.getAmount_of_payment()};
        return this.jdbcTemplate.update(createReserveQuery, createReserveParams);
    }

    public int createBasket(PostBasketReq postBasketReq) {
        String createBasketQuery = "insert into shopping_basket(accommodation_room_id, user_id, check_in_date, check_out_date, create_at)\n" +
                "values (?, ?, ?, ?, now())";
        Object[] createBasketParams = new Object[]{postBasketReq.getAccommodation_room_id(), postBasketReq.getUser_id(),
                postBasketReq.getCheck_in_date(), postBasketReq.getCheck_out_date()};
        return this.jdbcTemplate.update(createBasketQuery, createBasketParams);
    }

    public List<GetSearchRes> getSearch(int userId) {
        String getSearchQuery = "SELECT *\n" +
                "FROM search_history\n" +
                "WHERE user_id = ?\n" +
                "ORDER BY create_at desc";
        int getSearchParams = userId;
        return this.jdbcTemplate.query(getSearchQuery,
                (rs,rowNum) -> new GetSearchRes(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("word"),
                        rs.getInt("adult"),
                        rs.getInt("child"),
                        rs.getTimestamp("start_date"),
                        rs.getTimestamp("end_date"),
                        rs.getTimestamp("create_at")
                ),
                getSearchParams);
    }

    public int deleteReserve(int reserveId){
        String deleteReserveQuery = "delete from reservation_info where id = ?";
        int deleteReserveParams = reserveId;
        return this.jdbcTemplate.update(deleteReserveQuery,deleteReserveParams);
    }

    public int checkReserve(int reserveId){
        String checkReserveQuery = "select IF((select reservation_status from reservation_info where id = ?) = 1,\n" +
                "    IF((select check_in_date from reservation_info where id = ?) > DATE_FORMAT(now(), '%Y-%m-%d'), 1, 0), 0);";
        Object[] checkReserveParams = new Object[]{reserveId, reserveId};
        return this.jdbcTemplate.queryForObject(checkReserveQuery,
                int.class,
                checkReserveParams);
    }

    public int checkNickname(String nickname) {
        String checkNickameQuery = "select exists(select id from user where nickname = ?)";
        String checkNickameParams = nickname;
        return this.jdbcTemplate.queryForObject(checkNickameQuery,
                int.class,
                checkNickameParams);
    }

}
