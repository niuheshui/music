package com.example.yin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.example.yin.domain.History;
import com.example.yin.domain.Song;
import com.example.yin.domain.SongList;
import com.example.yin.service.HistoryService;
import com.example.yin.service.SongListService;
import com.example.yin.service.SongService;
import com.example.yin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RestController
@Slf4j
public class RecommendController {
    
    @Autowired
    private SongListService songListService;
    
    @Autowired
    private SongService songService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private HistoryService historyService;
    
    private static final String MODEL_FILE = "data.csv";
    
    @GetMapping("/recommend/{userId}")
    public JSONObject recommend(@PathVariable Long userId) {
        JSONObject jsonObject = new JSONObject();
        
        List<Song> recommendSongList = getRecommendedSongs(userId);
        List<SongList> recommendSongLists = songListService.allSongList().stream().limit(5).collect(Collectors.toList());
        
        jsonObject.put("recommendSongLists", recommendSongLists);
        jsonObject.put("recommendSongList", recommendSongList);
        
        return jsonObject;
    }
    
    private List<Song> getRecommendedSongs(Long userId) {
        Path dataModelPath = Paths.get(MODEL_FILE);
        int n = 3;
        
        try {
            generateDataModelFile(dataModelPath);
            List<RecommendedItem> recommendedItem = getRecommendedItem(userId, 5, n, dataModelPath);
            if (CollUtil.isEmpty(recommendedItem)) {
                throw new RuntimeException("推荐数为0");
            }
            return songService.listByIds(recommendedItem
                .stream()
                .map(RecommendedItem::getItemID)
                .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("推荐过程中发生错误", e);
            return RandomUtil.randomEles(songService.list(), 5);
        }
    }
    
    
    private List<RecommendedItem> getRecommendedItem(long userId, int howMany, int n, Path dataModelPath) throws TasteException, IOException {
        // 1. 加载用户评分数据
        DataModel model = new FileDataModel(dataModelPath.toFile());
        // 2. 计算用户相似度
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        // 3. 创建用户邻居
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(n, similarity, model);
        // 4. 创建推荐器
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        // 5. 获取推荐
        List<RecommendedItem> recommendations = recommender.recommend(userId, howMany);
        
        return recommendations;
        // 6. 输出推荐结果
        // for (RecommendedItem item : recommendations) {
        //     System.out.format("推荐歌曲ID: %s, 评分: %s%n", item.getItemID(), item.getValue());
        // }
    }
    
    
    private void generateDataModelFile(Path modelFilePath) throws IOException {
        List<History> list = historyService.list();
        
        // 使用 Stream API 简化代码
        Map<Integer, Map<Integer, Integer>> m1 = list.stream()
            .collect(groupingBy(
                History::getUserId,
                groupingBy(
                    History::getSongId,
                    Collectors.summingInt(h -> 1)
                )
            ));
        
        
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(modelFilePath))) {
            m1.forEach((userId, songCountMap) -> {
                songCountMap.forEach((songId, count) -> {
                    pw.println(String.format("%d,%d,%d", userId, songId, count));
                });
            });
        }
    }
    
    
    
    
    
    
    
    
}
