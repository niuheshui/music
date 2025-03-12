<template>
  <div class='my-music'>
    <div class='album-slide'>
      <div class='album-img'>
        <img :src='attachImageUrl(userPic)' alt=''>
      </div>
    </div>
    <div class='song-list'>
      <div class='songs-body'>
        <song-list :songList='recommendSongList'>
          <template slot='title'><span>每日歌曲推荐</span></template>
        </song-list>
      </div>
    </div>
    <play-list :playList='recommendSongLists' title='每日歌单推荐' path='song-sheet-detail'></play-list>
  </div>
</template>

<script>
import mixin from '../mixins'
import SongList from '../components/SongList'
import PlayList from '../components/PlayList.vue'
import { HttpManager } from '../api'

export default {
  name: 'MyMusic',
  mixins: [mixin],
  components: {
    SongList,
    PlayList
  },
  data () {
    return {
      userPic: '',
      recommendSongLists: [],
      recommendSongList: []
    }
  },
  mounted () {
    this.getUserInfo(this.userId)
    this.getRecommend()
  },
  methods: {
    getRecommend () {
      this.$axios.get(`/recommend/${this.userId}`).then(res => {
        console.log(res.data)
        this.recommendSongList = res.data.recommendSongList
        this.recommendSongLists = res.data.recommendSongLists
      })
    },
    getUserInfo (id) {
      HttpManager.getUserOfId(id)
        .then(res => {
          this.userPic = res[0].avatar
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/my-music.scss';
</style>
