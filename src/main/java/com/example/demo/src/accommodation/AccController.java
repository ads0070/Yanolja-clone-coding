package com.example.demo.src.accommodation;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.accommodation.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/accommodations")
public class AccController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AccService accService;
    @Autowired
    private final AccProvider accProvider;
    @Autowired
    private final JwtService jwtService;

    public AccController(AccService accService, AccProvider accProvider, JwtService jwtService) {
        this.accService = accService;
        this.accProvider = accProvider;
        this.jwtService = jwtService;
    }

    /**
     * 국내 숙소 목록 조회 API (+검색)
     * [GET] /app/accommodations?category=&sigungu=
     * @return BaseResponse<List<GetAccRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetAccRes>> getAccsByCategory(@RequestParam(required = true) String category, @RequestParam(required = true) String sigungu) {
        try{
            List<GetAccRes> getAccRes = accProvider.getAccsByCategory(category, sigungu);
            return new BaseResponse<>(getAccRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 숙소 후기 조회 API
     * [GET] /app/accommodations/reviews/:accId
     * @return BaseResponse<List<GetAccReviewRes>>
     */
    @ResponseBody
    @GetMapping("/reviews/{accId}")
    public BaseResponse<List<GetAccReviewRes>> getAccReviews(@PathVariable int accId) {
        try{
            List<GetAccReviewRes> getAccReviewRes = accProvider.getAccReviews(accId);
            return new BaseResponse<>(getAccReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 객실 목록 조회 API
     * [GET] /app/accommodations/rooms/:accId
     * @return BaseResponse<List<GetRoomRes>>
     */
    @ResponseBody
    @GetMapping("/rooms/{accId}")
    public BaseResponse<List<GetRoomRes>> getAccRooms(@PathVariable("accId") int accId,
                                                      @RequestParam(required = true) Date startDate,
                                                      @RequestParam(required = true) Date endDate) {
        try {
            List<GetRoomRes> getRoomRes = accProvider.getAccRooms(accId, startDate, endDate);
            return new BaseResponse<>(getRoomRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 숙소 추가 API
     * [POST] /app/accommodations/:ownerId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{ownerId}")
    public BaseResponse<String> createAcc(@PathVariable("ownerId") int ownerId, @RequestBody PostAccReq postAccReq) {
        String result = "";
        try {
            int ownerIdByJwt = jwtService.getUserIdx();

            if(ownerId != ownerIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            accService.postAcc(postAccReq, ownerId);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 숙소 정보 변경 API
     * [PATCH] /app/accommodations/accInfo/:ownerId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/accInfo/{ownerId}")
    public BaseResponse<String> modifyAccInfo(@PathVariable("ownerId") int ownerId, @RequestBody PatchAccInfoReq patchAccInfoReq) {
        String result = "";
        try {
            int ownerIdByJwt = jwtService.getUserIdx();

            if(ownerId != ownerIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            accService.modifyAccInfo(patchAccInfoReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 숙소 주소 변경 API
     * [PATCH] /app/accommodations/accAddr/:ownerId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/accAddr/{ownerId}")
    public BaseResponse<String> modifyAccAddr(@PathVariable("ownerId") int ownerId, @RequestBody PatchAccAddrReq patchAccAddrReq) {
        String result = "";
        try {
            int ownerIdByJwt = jwtService.getUserIdx();

            if(ownerId != ownerIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            accService.modifyAccAddr(patchAccAddrReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 객실 추가 API
     * [POST] /app/accommodations/:ownerId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/room/{ownerId}")
    public BaseResponse<String> createAccAddr(@PathVariable("ownerId") int ownerId, @RequestBody PostRoomReq postRoomReq) {
        String result = "";
        try {
            int ownerIdByJwt = jwtService.getUserIdx();

            if(ownerId != ownerIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            accService.createAccAddr(postRoomReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 객실 정보 변경 API
     * [PATCH] /app/accommodations/room/:ownerId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/room/{ownerId}")
    public BaseResponse<String> modifyRoom(@PathVariable("ownerId") int ownerId, @RequestBody PatchRoomReq patchRoomReq) {
        String result = "";
        try {
            int ownerIdByJwt = jwtService.getUserIdx();

            if(ownerId != ownerIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            accService.modifyRoom(patchRoomReq);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
