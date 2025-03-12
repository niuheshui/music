package com.example.yin.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    /* 歌曲图片，歌手图片，歌曲文件，歌单图片等文件的存放路径 */
    public static Path PROJECT_PATH = Paths.get(".");
    public static Path IMG_PATH = PROJECT_PATH.resolve("img/");
    public static Path SONG_PATH = PROJECT_PATH.resolve("song/");
    public static Path AVATAR_IMAGES_PATH = IMG_PATH.resolve("avatarImages/");
    public static Path SONG_LIST_PIC_PATH = IMG_PATH.resolve("songListPic/");
    public static Path SONG_PIC_PATH = IMG_PATH.resolve("songPic/");
    public static Path SINGER_PIC_PATH = IMG_PATH.resolve("singerPic/");
}
