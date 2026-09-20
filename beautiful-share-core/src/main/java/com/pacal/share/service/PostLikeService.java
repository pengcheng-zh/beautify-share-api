package com.pacal.share.service;

import com.pacal.share.common.Constants;
import com.pacal.share.dao.UserPostLikeDao;
import com.pacal.share.entity.po.UserPostLikePO;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PostLikeService {
    @Resource
    UserPostLikeDao postLikeDao;

    public void toggleLike(Integer postId) {
        int userId = RequestUtil.getUserIdInt();

        UserPostLikePO likePO = postLikeDao.getByUserIdAndPostId(userId, postId);
        if (Objects.isNull(likePO)) {
            likePO = new UserPostLikePO();
            likePO.setUserId(userId);
            likePO.setPostId(postId);
            likePO.setStatus(Constants.ACTIVE_STATUS);
            postLikeDao.insert(likePO);
        } else {
            UserPostLikePO updatePO = new UserPostLikePO();
            updatePO.setId(likePO.getId());
            updatePO.setStatus(Constants.ACTIVE_STATUS.equals(likePO.getStatus()) ? Constants.INACTIVE_STATUS : Constants.ACTIVE_STATUS);
            postLikeDao.updateById(updatePO);
        }
    }
}
