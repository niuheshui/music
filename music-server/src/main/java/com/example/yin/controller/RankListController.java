package com.example.yin.controller;

import cn.hutool.json.JSONObject;
import com.example.yin.domain.RankList;
import com.example.yin.service.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Controller
public class RankListController {

    @Autowired
    private RankListService rankListService;
    
    @GetMapping("/rankList/{userId}/{songListId}")
    public int getScore(@PathVariable Long userId, @PathVariable Long songListId) {
        return rankListService.getScore(userId, songListId);
    }
    

//    提交评分
    @ResponseBody
    @PostMapping("/rankList/add")
    public Object addRank(RankList rankList){
        JSONObject jsonObject = new JSONObject();
        boolean res = rankListService.addRank(rankList);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "评价成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "评价失败");
            return jsonObject;
        }
    }

//    获取指定歌单的评分
    @RequestMapping(value = "/rankList", method = RequestMethod.GET)
    public Object rankOfSongListId(HttpServletRequest req){
        String songListId = req.getParameter("songListId");
        return rankListService.rankOfSongListId(Long.parseLong(songListId));
    }
}
