package com.example.yin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class History {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer songId;
    @TableField("created_time")
    private Date createdTime;
}
