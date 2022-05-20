package com.example.demo.src.user;

import com.example.demo.src.user.model.GetReserveRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexNickname;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 전체 유저 조회 API (+검색)
     * [GET] /app/users
     * 전체 회원 또는 이메일 검색 조회 API
     * [GET] /app/users?email=
     * @return BaseResponse<List<GetUserRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String email) {
        try{
            if(email == null){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }

            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(email);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 유저 조회 API
     * [GET] /app/users/:userId
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") int userIdx) {
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 닉네임 변경 API
     * [PATCH] /app/users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyUserName(@PathVariable("userId") int userIdx, @RequestBody Map<String,String> nickName){
        try {
            //jwt에서 id 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(!isRegexNickname(nickName.get("nickname"))) {
                return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
            }

            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userIdx, nickName.get("nickname"));
            userService.modifyUserName(patchUserReq);

            String result = nickName.get("nickname");
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 비밀번호 변경 API
     * [PATCH] /app/users/password/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/password/{userId}")
    public BaseResponse<String> modifyUserPwd(@PathVariable("userId") int userIdx, @RequestBody PatchPwdReq pwd){

        try {
            //jwt에서 id 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 비밀번호 형식 검증
            if(!isRegexPassword(pwd.getNewPwd())) {
                return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
            }

            // 비밀번호 변경
            PatchPwdReq patchPwdReq = new PatchPwdReq(userIdx, pwd.getOriginPwd(), pwd.getNewPwd());
            userService.modifyUserPwd(patchPwdReq);

            String result = "변경 완료";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내 쿠폰 조회 API
     * [GET] /app/users/coupons/:userId
     * @return BaseResponse<List<GetUserCouponRes>>
     */
    @ResponseBody
    @GetMapping("/coupons/{userId}")
    public BaseResponse<List<GetUserCouponRes>> getUserCoupon(@PathVariable("userId") int userId) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetUserCouponRes> getUserCouponRes = userProvider.getUserCoupon(userId);
            return new BaseResponse<>(getUserCouponRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜 추가 API
     * [POST] /app/users/wish/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/wish/{userId}")
    public BaseResponse<String> createWish(@PathVariable("userId") int userId, @RequestBody PostWishReq postWishReq) {
        String result = "";
        try{
            int userIdByJwt = jwtService.getUserIdx();

            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.createWish(postWishReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜 목록 확인 API
     * [GET] /app/users/wish/:userId
     * @return BaseResponse<List<GetWishRes>>
     */
    @ResponseBody
    @GetMapping("/wish/{userId}")
    public BaseResponse<List<GetWishRes>> getWishList(@PathVariable("userId") int userId) {
        try{
            int userIdByJwt = jwtService.getUserIdx();

            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetWishRes> getWishList = userProvider.getWishList(userId);
            return new BaseResponse<>(getWishList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜 삭제 API
     * [DELETE] /app/users/wish/:userId/:wishId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/wish/{userId}/{wishId}")
    public BaseResponse<String> deleteWish(@PathVariable("userId") int userId, @PathVariable("wishId") int wishId) {
        String result = "";
        try{
            int userIdByJwt = jwtService.getUserIdx();

            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.deleteWish(wishId);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 포인트 조회 API
     * [GET] /app/users/points/:userId
     * @return BaseResponse<List<GetPointRes>>
     */
    @ResponseBody
    @GetMapping("/points/{userId}")
    public BaseResponse<List<GetPointRes>> getPoint(@PathVariable("userId") int userId) {
        try{
            int userIdByJwt = jwtService.getUserIdx();

            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetPointRes> getPointRes = userProvider.getPoint(userId);
            return new BaseResponse<>(getPointRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내 후기 삭제 API
     * [DELETE] /app/users/reviews/:userId/:reviewId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/reviews/{userId}/{reviewId}")
    public BaseResponse<String> deleteReview(@PathVariable("userId") int userId, @PathVariable("reviewId") int reviewId) {
        String result = "";
        try{
            int userIdByJwt = jwtService.getUserIdx();

            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            int answerId = userProvider.checkAnswer(reviewId);

            // answerId가 -1이면 리뷰에 사장님 답변이 달려있지 않은 경우
            // -1이 아니면 리뷰에 달린 사장님 답변의 id 번호를 의미.
            if (answerId==-1) {
                userService.deleteReview(reviewId);
            } else {
                userService.deleteAnswer(answerId);
                userService.deleteReview(reviewId);
            }

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 장바구니 조회 API
     * [GET] /app/users/baskets/:userId
     * @return BaseResponse<List<GetBasketRes>>
     */
    @ResponseBody
    @GetMapping("/baskets/{userId}")
    public BaseResponse<List<GetBasketRes>> getBaskets(@PathVariable("userId") int userId) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetBasketRes> getBasketRes = userProvider.getBaskets(userId);
            return new BaseResponse<>(getBasketRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 장바구니 삭제 API
     * [DELETE] /app/users/baskets/:userId/:basketId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/baskets/{userId}/{basketId}")
    public BaseResponse<String> deleteBasket(@PathVariable("userId") int userId, @PathVariable("basketId") int basketId) {
        String result = "";
        try {
            int userIdByJwt = jwtService.getUserIdx();

            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.deleteBasket(basketId);
            return new BaseResponse<>(result);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 예약 목록 조회 API
     * [GET] /app/users/reservations/:userId
     * @return BaseResponse<List<GetReserveRes>>
     */
    @ResponseBody
    @GetMapping("/reservations/{userId}")
    public BaseResponse<List<GetReserveRes>> getReserves(@PathVariable("userId") int userId, @RequestParam(required = true) int term) {
        try {
            int userIdByJwt = jwtService.getUserIdx();

            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetReserveRes> getReserveRes = userProvider.getReserves(userId, term);
            return new BaseResponse<>(getReserveRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 숙박 예약 API
     * [POST] /app/users/reservations
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/reservations")
    public BaseResponse<String> postReserve(@RequestBody PostReserveReq postReserveReq) {
        String result = "";
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(postReserveReq.getUser_id() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.postReserve(postReserveReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 장바구니 추가 API
     * [POST] /app/users/baskets
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/baskets")
    public BaseResponse<String> postBasket(@RequestBody PostBasketReq postBasketReq) {
        String result = "";

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(postBasketReq.getUser_id() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.postBasket(postBasketReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 검색어 조회 API
     * [GET] /app/users/search/:userId
     * @return BaseResponse<List<GetSearchWordRes>>
     */
    @ResponseBody
    @GetMapping("/search/{userId}")
    public BaseResponse<List<GetSearchRes>> getSearch(@PathVariable("userId") int userId) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<GetSearchRes> getSearchRes = userProvider.getSearch(userId);
            return new BaseResponse<>(getSearchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 예약 취소 API
     * [DELETE] /app/users/reservations/:userId/:reserveId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/reservations/{userId}/{reserveId}")
    public BaseResponse<String> deleteReserve(@PathVariable("userId") int userId, @PathVariable("reserveId") int reserveId) {
        String result = "";
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.deleteReserve(reserveId);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
