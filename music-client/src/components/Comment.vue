<template>
  <div>
    <div class='comment'>
      <h2>
        <span>评论</span>
        <span class='part__tit_desc'>共 {{commentList.length}} 条评论</span>
      </h2>
      <div class='comment-msg'>
        <el-input
          class='comment-input'
          type='textarea'
          placeholder='期待您的精彩评论...'
          :rows='2'
          v-model='textarea'>
        </el-input>
      </div>
      <el-button type='primary' class='sub-btn' @click='postComment()'>发表评论</el-button>
    </div>
    <ul class='popular' v-for='(item, index) in commentList' :key='index'>
      <li>
        <div class='popular-img'>
          <img :src='attachImageUrl(userPicList[index])' alt=''>
        </div>
        <div class='popular-msg'>
          <ul>
            <li class='name'>{{userNameList[index]}}</li>
            <li class='content'>{{item.content}}</li>
            <li class='time'>{{item.createTime}}</li>
          </ul>
        </div>
        <div class='up' ref='up' @click='postUp(item.id, item.up, index)'>
          <svg class='icon' aria-hidden='true'>
            <use :xlink:href='ZAN'></use>
          </svg>
          {{item.up}}
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mixin from '../mixins'
import { HttpManager } from '../api'
import { ICON } from '../enums'

export default {
  name: 'Comment',
  mixins: [mixin],
  props: {
    playId: Number, // 歌曲ID或歌单ID
    type: Number // 歌单（1）/歌曲（0）
  },
  data () {
    return {
      commentList: [], // 存放评论内容
      userPicList: [], // 保存评论用户头像
      userNameList: [], // 保存评论用户名字
      textarea: '', // 存放输入内容
      ZAN: ICON.ZAN
    }
  },
  computed: {
    ...mapGetters([
      'songId',
      'token' // 用户是否登录
    ])
  },
  watch: {
    songId () {
      this.getComment()
    }
  },
  mounted () {
    this.getComment()
  },
  methods: {
    // 获取所有评论
    getComment () {
      HttpManager.getAllComment(this.type, this.playId)
        .then(res => {
          this.commentList = res
          for (let item of res) {
            this.getUsers(item.userId)
          }
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 获取评论用户的昵称和头像
    getUsers (id) {
      HttpManager.getUserOfId(id)
        .then(res => {
          this.userPicList.push(res[0].avatar)
          this.userNameList.push(res[0].username)
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 提交评论
    postComment () {
      if (!this.token) {
        this.$notify({
          title: '请先登录喔',
          type: 'warning'
        })
        return
      }

      // 0 代表歌曲， 1 代表歌单
      let params = new URLSearchParams()
      if (this.type === 1) {
        params.append('songListId', this.playId)
      } else if (this.type === 0) {
        params.append('songId', this.playId)
      }
      params.append('userId', this.userId)
      params.append('type', this.type)
      params.append('content', this.textarea)
      HttpManager.setComment(params)
        .then(res => {
          if (res.code === 1) {
            this.textarea = ''
            this.getComment()
            this.$notify({
              title: '评论成功',
              type: 'success'
            })
          } else {
            this.$notify({
              title: '评论失败',
              type: 'error'
            })
          }
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 点赞
    postUp (id, up, index) {
      if (!this.token) {
        this.$notify({
          title: '请先登录喔',
          type: 'warning'
        })
        return
      }

      let params = new URLSearchParams()
      params.append('id', id)
      params.append('up', up + 1)
      HttpManager.setLike(params)
        .then(res => {
          if (res.code === 1) {
            this.$refs.up[index].children[0].style.color = '#2796dd'
            this.getComment()
          }
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/comment.scss';
</style>
