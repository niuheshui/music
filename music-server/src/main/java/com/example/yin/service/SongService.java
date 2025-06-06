package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.domain.Song;

import java.util.List;

public interface SongService extends IService<Song> {

    boolean addSong (Song song);

    boolean updateSongMsg(Song song);

    boolean updateSongUrl(Song song);

    boolean updateSongPic(Song song);

    boolean deleteSong(Integer id);

    List<Song> allSong();

    List<Song> songOfSingerId(Integer singerId);

    List<Song> songOfId(Integer id);

    List<Song> songOfSingerName(String name);

    List<Song> songOfName(String name);
}
