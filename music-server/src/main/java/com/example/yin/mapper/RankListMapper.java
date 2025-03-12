package com.example.yin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.yin.domain.RankList;
import org.apache.ibatis.annotations.Select;

public interface RankListMapper extends BaseMapper<RankList> {
    
    @Select("select avg(score) from rank_list where song_list_id = #{songListId}")
    int songListScore(Long songListId);

    int insert(RankList rankList);

    int insertSelective(RankList record);

    /**
     * 查总分
     * @param songListId
     * @return
     */
    int selectScoreSum(Long songListId);

    /**
     * 查总评分人数
     * @param songListId
     * @return
     */
    int selectRankNum(Long songListId);
}
