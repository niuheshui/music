package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.domain.SongList;

import java.util.List;

public interface SongListService extends IService<SongList> {

    boolean addSongList (SongList songList);

    boolean updateSongListMsg(SongList songList);

    boolean updateSongListImg(SongList songList);

    boolean deleteSongList(Integer id);

    List<SongList> allSongList();

    List<SongList> likeTitle(String title);

    List<SongList> likeStyle(String style);

    List<SongList> songListOfTitle(String title);

}
