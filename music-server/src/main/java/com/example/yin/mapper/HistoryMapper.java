package com.example.yin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.yin.domain.History;
import org.apache.ibatis.annotations.Select;

public interface HistoryMapper extends BaseMapper<History> {
    
    @Select("SELECT * FROM history WHERE id = #{id}")
    History byId(Integer id);
    
}
