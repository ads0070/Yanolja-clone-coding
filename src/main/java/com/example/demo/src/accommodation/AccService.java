package com.example.demo.src.accommodation;

import com.example.demo.config.BaseException;
import com.example.demo.src.accommodation.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AccService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccDao accDao;
    private final AccProvider accProvider;
    private final JwtService jwtService;

    @Autowired
    public AccService(AccDao accDao, AccProvider accProvider, JwtService jwtService) {
        this.accDao = accDao;
        this.accProvider = accProvider;
        this.jwtService = jwtService;
    }

    @Transactional(rollbackFor = BaseException.class)
    public void postAcc(PostAccReq postAccReq, int ownerId) throws BaseException {
        LocalDateTime localDateTime = LocalDateTime.now();

        try {
            PostAccInfoReq postAccInfoReq = new PostAccInfoReq(postAccReq.getName(),
                    postAccReq.getStore_phone_number(), postAccReq.getMain_category(),
                    postAccReq.getSub_category(), localDateTime);

            int result = accDao.createAcc(postAccInfoReq);
            if(result == 0) {
                throw new BaseException(INSERT_FAIL_ACC);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

        int ac_id = accProvider.getLastInsertId();

        try {
            int result = accDao.insertConnectAcc(ac_id, ownerId);
            if(result == 0) {
                throw new BaseException(INSERT_FAIL_ACC_MATCH);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

        try {
            PostAccAddrReq postAccAddrReq = new PostAccAddrReq(ac_id, postAccReq.getSido(),
                    postAccReq.getSigungu(), postAccReq.getEubmyeondong(), postAccReq.getRi(),
                    postAccReq.getBeonji(),postAccReq.getDetail(), localDateTime);

            int result = accDao.insertAddr(postAccAddrReq);
            if(result == 0) {
                throw new BaseException(INSERT_FAIL_ADDR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAccInfo(PatchAccInfoReq patchAccInfoReq) throws BaseException {
        LocalDateTime localDateTime = LocalDateTime.now();
        patchAccInfoReq.setUpdate_at(localDateTime);

        try {
            int result = accDao.modifyAccInfo(patchAccInfoReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_ACC_INFO);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAccAddr(PatchAccAddrReq patchAccAddrReq) throws BaseException {
        LocalDateTime localDateTime = LocalDateTime.now();
        patchAccAddrReq.setUpdate_at(localDateTime);

        try {
            int result = accDao.modifyAccAddr(patchAccAddrReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_ACC_ADDR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createAccAddr(PostRoomReq postRoomReq) throws BaseException {
        try {
            int result = accDao.insertRoom(postRoomReq);
            if (result == 0) {
                throw new BaseException(INSERT_FAIL_ROOM);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyRoom(PatchRoomReq patchRoomReq) throws BaseException {
        try {
            int result = accDao.modifyRoom(patchRoomReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_ROOM);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
