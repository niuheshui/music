package com.example.yin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.domain.RankList;
import com.example.yin.mapper.RankListMapper;
import com.example.yin.service.RankListService;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class RankListServiceImpl extends ServiceImpl<RankListMapper, RankList> implements RankListService {

    @Override
    public int rankOfSongListId(Long songListId) {
        return getBaseMapper().songListScore(songListId);
    }
    
    @Override
    public int getScore(Long userId, Long songListId) {
        RankList entity = lambdaQuery()
            .eq(RankList::getUserId, userId)
            .eq(RankList::getSongListId, songListId)
            .one();
        
        return entity == null ?  0 : entity.getScore();
    }
    
    @Override
    public boolean addRank(RankList rankList) {
        
        RankList entity = lambdaQuery()
            .eq(RankList::getUserId, rankList.getUserId())
            .eq(RankList::getSongListId, rankList.getSongListId())
            .one();
        
        if (entity != null) {
            entity.setScore(rankList.getScore());
            updateById(entity) ;
        } else {
            save(rankList);
        }
        
        
        return true;
    }
}
