package com.example.yin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class RankList {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long songListId;
    private Long userId;
    private Integer score;
}
