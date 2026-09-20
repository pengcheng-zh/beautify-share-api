package com.pacal.share.service;

import com.pacal.share.common.Constants;
import com.pacal.share.dao.UserPostFavoriteDao;
import com.pacal.share.entity.po.UserPostFavoritePO;
import com.pacal.share.entity.vo.PostInteractionVO;
import com.pacal.share.utils.RequestUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PostFavoriteService {
    @Resource
    UserPostFavoriteDao favoriteDao;
    public void toggleFavorite(Integer postId) {
        Integer userId = RequestUtil.getUserIdInt();
        UserPostFavoritePO favoritePO = favoriteDao.getByUserIdAndPostId( userId, postId );
        if (Objects.isNull(favoritePO)) {
            favoritePO = new UserPostFavoritePO();
            favoritePO.setUserId( userId );
            favoritePO.setPostId( postId );
            favoritePO.setStatus(Constants.ACTIVE_STATUS );
            favoriteDao.insert( favoritePO );
        } else {
            UserPostFavoritePO updatePO = new UserPostFavoritePO();
            updatePO.setId( favoritePO.getId() );
            updatePO.setStatus( favoritePO.getStatus().equals( Constants.ACTIVE_STATUS ) ? Constants.INACTIVE_STATUS : Constants.ACTIVE_STATUS );
            favoriteDao.updateById( updatePO );
        }
    }
}
