package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.domain.History;

public interface HistoryService extends IService<History> {
    void record(Integer userId, Integer songId);
}
