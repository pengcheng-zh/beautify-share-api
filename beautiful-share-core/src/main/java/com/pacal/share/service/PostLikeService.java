package com.pacal.share.service;

import com.pacal.share.common.Constants;
import com.pacal.share.dao.UserPostDao;
import com.pacal.share.dao.UserPostLikeDao;
import com.pacal.share.entity.po.UserPostLikePO;
import com.pacal.share.entity.po.UserPostPO;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PostLikeService {
    @Resource
    UserPostLikeDao postLikeDao;

    @Resource
    UserPostDao userPostDao;

    public void toggleLike(Integer postId) {
        int userId = RequestUtil.getUserIdInt();

        UserPostPO postPO = userPostDao.getById(postId);

        UserPostLikePO likePO = postLikeDao.getByUserIdAndPostId(userId, postId);
        if (Objects.isNull(likePO)) {
            likePO = new UserPostLikePO();
            likePO.setUserId(userId);
            likePO.setPostId(postId);
            likePO.setStatus(Constants.ACTIVE_STATUS);
            postLikeDao.insert(likePO);

            UserPostPO postUpdatePO = new UserPostPO();
            postUpdatePO.setId(postId);
            postUpdatePO.setLikeCount(postPO.getLikeCount() + 1);
            userPostDao.update(postUpdatePO);
        } else {
            UserPostLikePO updatePO = new UserPostLikePO();
            updatePO.setId(likePO.getId());
            updatePO.setStatus(Constants.ACTIVE_STATUS.equals(likePO.getStatus()) ? Constants.INACTIVE_STATUS : Constants.ACTIVE_STATUS);
            postLikeDao.updateById(updatePO);

            int likeCount = Constants.ACTIVE_STATUS.equals(updatePO.getStatus()) ? postPO.getLikeCount() + 1 : postPO.getLikeCount() - 1;
            if (likeCount < 0) {
                likeCount = 0;
            }
            UserPostPO postUpdatePO = new UserPostPO();
            postUpdatePO.setId(postId);
            postUpdatePO.setLikeCount(likeCount);
            userPostDao.update(postUpdatePO);
        }
    }
}
