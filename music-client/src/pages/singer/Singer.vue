<template>
  <div class='singer'>
    <ul class='singer-header'>
      <li
        v-for='(item, index) in singerStyle'
        :key='index'
        :class='{active: item.name === activeName}'
        @click='handleChangeView(item)'>
        {{item.name}}
      </li>
    </ul>
    <play-list :playList='data' path='singer-detail'></play-list>
    <div class='pagination'>
      <el-pagination
        @current-change='handleCurrentChange'
        background
        layout='total, prev, pager, next'
        :current-page='currentPage'
        :page-size='pageSize'
        :total='allPlayList.length'>
      </el-pagination>
    </div>
  </div>
</template>

<script>
import PlayList from '../../components/PlayList'
import { singerStyle } from '../../enums'
import { HttpManager } from '../../api'

export default {
  name: 'Singer',
  components: {
    PlayList
  },
  data () {
    return {
      singerStyle: singerStyle, // 歌手导航栏类别
      activeName: '全部歌手',
      pageSize: 15, // 页数
      currentPage: 1, // 当前页
      allPlayList: []
    }
  },
  computed: {
    // 计算当前表格中的数据
    data () {
      return this.allPlayList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  created () {
    this.getAllSinger()
  },
  methods: {
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    handleChangeView (item) {
      this.activeName = item.name
      this.allPlayList = []
      if (item.name === '全部歌手') {
        this.getAllSinger()
      } else {
        this.getSingerSex(item.type)
      }
    },
    // 获取所有歌手
    getAllSinger () {
      HttpManager.getAllSinger()
        .then(res => {
          this.currentPage = 1
          this.allPlayList = res
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 通过性别对歌手分类
    getSingerSex (sex) {
      HttpManager.getSingerOfSex(sex)
        .then(res => {
          this.currentPage = 1
          this.allPlayList = res
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/singer.scss';
</style>
