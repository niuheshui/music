package com.example.yin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.domain.History;
import com.example.yin.mapper.HistoryMapper;
import com.example.yin.service.HistoryService;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {
    @Override
    public void record(Integer userId, Integer songId) {
        History history = new History();
        history.setUserId(userId);
        history.setSongId(songId);
        save(history);
    }
}
