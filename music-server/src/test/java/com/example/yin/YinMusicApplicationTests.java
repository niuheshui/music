package com.example.yin;

import cn.hutool.core.util.RandomUtil;
import com.example.yin.domain.*;
import com.example.yin.service.*;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

// @SpringBootTest
@Slf4j
public class YinMusicApplicationTests {
    
    @Autowired
    private SongListService songListService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SongService songService;
    
    @Autowired
    private HistoryService historyService;
    
    @Autowired
    private RankListService rankListService;
    
    String ratingsFilePath = "songlist_data.csv";
    
    @Test
    public void addUser() {
        
        List<Song> songs = songService.allSong();
        
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername(RandomUtil.randomString(6));
            user.setPassword("123456");
            user.setAvatar("/img/avatarImages/user.jpg");
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            userService.addUser(user);
        
            Integer userId = user.getId();
            
            RandomUtil.randomEles(songs,
                RandomUtil.randomInt(10, 20))
                .stream().map(Song::getId)
                .forEach(songId -> historyService.record(userId, songId));
        }
    }
    
    @Test
    public void addHistory() {
        
        List<Song> songs = songService.allSong();
        List<User> users = userService.allUser();
        
        for (int i = 0; i < 10; i++) {
            users.forEach(user -> {
                RandomUtil.randomEles(songs, RandomUtil.randomInt(10, 20))
                    .stream().map(Song::getId)
                    .forEach(songId -> historyService.record(user.getId(), songId));
            });
        }
    }
    
    @Test
    public void test() {
        long userId = 1; // 目标用户ID
        int numberOfRecommendations = 10; // 推荐数量
        int n = 5;
        
        try {
            // 1. 加载用户评分数据
            DataModel model = new FileDataModel(Paths.get(ratingsFilePath).toFile());
            // 2. 计算用户相似度
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            // 3. 创建用户邻居
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(n, similarity, model);
            // 4. 创建推荐器
            UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            // 5. 获取推荐
            List<RecommendedItem> recommendations = recommender.recommend(userId, numberOfRecommendations);
            // 6. 输出推荐结果
            log.info("为用户 {} 推荐的歌曲：", userId);
            for (RecommendedItem item : recommendations) {
                log.info("推荐歌曲ID: {}, 评分: {}", item.getItemID(), item.getValue());
            }
        } catch (Exception e) {
            log.error("推荐过程中发生错误", e);
        }
    }

    @Test
    public void genData() throws TasteException, IOException {
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
        
        
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(ratingsFilePath)))) {
            m1.forEach((userId, songCountMap) -> {
                songCountMap.forEach((songId, count) -> {
                    pw.println(String.format("%d,%d,%d", userId, songId, count));
                });
            });
        }
        
    }
    
    
    @Test
    public void randomData() throws IOException {
        int userCount = 100;
        int musicCount = 1000;
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < userCount; i++) {
            for (int j = 0; j < musicCount; j++) {
                
                if (RandomUtil.randomInt(1, 100) < 70) {
                    continue;
                }
                
                //                      userId, songId, count
                sb.append(String.format("%d,%d,%d\n", i, j, RandomUtil.randomInt(1, 100)));
            }
        }
        
        
        Files.write(Paths.get(ratingsFilePath), sb.toString().getBytes(StandardCharsets.UTF_8));
    }
    
    
    @Test
    public void addRank() {
        List<User> users = userService.allUser();
        List<SongList> list = songListService.list();

        users.forEach(user -> {
            list.forEach(songList -> {
                RankList rankList = new RankList();
                rankList.setUserId((long) user.getId());
                rankList.setSongListId((long) songList.getId());
                rankList.setScore(RandomUtil.randomInt(1, 10));
                rankListService.addRank(rankList);
            });
        });
    }
    
}

