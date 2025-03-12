package com.example.yin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.domain.Song;
import com.example.yin.mapper.SongMapper;
import com.example.yin.service.SongService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements SongService {


    @Override
    public List<Song> allSong()
    {
        return getBaseMapper().allSong();
    }

    @Override
    public boolean addSong(Song song)
    {

        return getBaseMapper().insertSelective(song) > 0?true:false;
    }

    @Override
    public boolean updateSongMsg(Song song) {
        return getBaseMapper().updateSongMsg(song) >0 ?true:false;
    }

    @Override
    public boolean updateSongUrl(Song song) {

        return getBaseMapper().updateSongUrl(song) >0 ?true:false;
    }

    @Override
    public boolean updateSongPic(Song song) {

        return getBaseMapper().updateSongPic(song) >0 ?true:false;
    }

    @Override
    public boolean deleteSong(Integer id) {
        return getBaseMapper().deleteSong(id) >0 ?true:false;
    }

    @Override
    public List<Song> songOfSingerId(Integer singerId)

    {
        return getBaseMapper().songOfSingerId(singerId);
    }

    @Override
    public List<Song> songOfId(Integer id)

    {
        return getBaseMapper().songOfId(id);
    }

    @Override
    public List<Song> songOfSingerName(String name)

    {
        return getBaseMapper().songOfSingerName(name);
    }

    @Override
    public List<Song> songOfName(String name)

    {
        return getBaseMapper().songOfName(name);
    }
}
