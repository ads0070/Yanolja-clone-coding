package com.example.demo.src.accommodation;

import com.example.demo.config.BaseException;
import com.example.demo.src.accommodation.model.GetAccRes;
import com.example.demo.src.accommodation.model.GetAccReviewRes;
import com.example.demo.src.accommodation.model.GetRoomRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class AccProvider {

    private final AccDao accDao;
    private final JwtService jwtService;

    @Autowired
    public AccProvider(AccDao accDao, JwtService jwtService) {
        this.accDao = accDao;
        this.jwtService = jwtService;
    }

    public List<GetAccRes> getAccsByCategory(String category, String sigungu) throws BaseException {
        try {
            List<GetAccRes> getAccRes = accDao.getAccsByCategory(category, sigungu);
            return getAccRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetAccReviewRes> getAccReviews(int accId) throws BaseException {
        try {
            List<GetAccReviewRes> getAccReviewRes = accDao.getAccReviews(accId);
            return getAccReviewRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetRoomRes> getAccRooms(int accId, Date startDate, Date endDate) throws BaseException {
        try {
            List<GetRoomRes> getRoomRes = accDao.getAccRooms(accId, startDate, endDate);
            return getRoomRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getLastInsertId() throws BaseException {
        try {
            return accDao.getLastInsertId();
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
