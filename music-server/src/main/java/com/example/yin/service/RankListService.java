package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.domain.RankList;

public interface RankListService extends IService<RankList> {
    int rankOfSongListId(Long songListId);
    int getScore(Long userId, Long songListId);
    boolean addRank(RankList rankList);
}
