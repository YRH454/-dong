<template>
<div class="banner-wrap" v-if="announcements.length">
  <div class="banner-label">
    <span class="bl-icon">📢</span>
    <span class="bl-text">公告</span>
    <span class="bl-badge" v-if="hasNew">NEW</span>
  </div>
  <van-swipe :autoplay="4000" indicator-color="#4CAF50" class="swiper">
    <van-swipe-item v-for="item in announcements" :key="item.id">
      <div class="banner-card" :class="{new: item.id > lastSeenId}" @click="showDetail(item)">
        <h4 class="banner-title">{{ item.title }}</h4>
        <p class="banner-content">{{ item.content }}</p>
        <span class="banner-time">{{ item.createTime }}</span>
      </div>
    </van-swipe-item>
  </van-swipe>
  <van-popup v-model:show="showDlg" round position="bottom" :style="{maxHeight:'60vh'}">
    <div style="padding:20px" v-if="detailItem">
      <span class="badge-tag">📢 公告详情</span>
      <h3 style="font-size:18px;color:#333;margin:12px 0 8px">{{ detailItem.title }}</h3>
      <p style="font-size:14px;color:#888;margin-bottom:12px">{{ detailItem.createTime }}</p>
      <p style="font-size:15px;color:#555;line-height:1.8;white-space:pre-wrap">{{ detailItem.content }}</p>
      <van-button block round style="margin-top:16px" @click="showDlg=false">关闭</van-button>
    </div>
  </van-popup>
</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'; import api from '../api'
const props = defineProps({ adminId: String })
const announcements = ref([]), showDlg = ref(false), detailItem = ref(null)
const lastSeenId = ref(parseInt(localStorage.getItem('lastAnnId')||'0'))
const hasNew = computed(() => announcements.value.length > 0 && announcements.value[0].id > lastSeenId.value)
async function load() {
  try { const r = await api.get('/announcements/' + props.adminId); announcements.value = r || [] } catch (e) {}
}
function showDetail(item) {
  detailItem.value = item; showDlg.value = true
  if (item.id > lastSeenId.value) { lastSeenId.value = item.id; localStorage.setItem('lastAnnId', String(item.id)) }
}
onMounted(load)
</script>

<style scoped>
.banner-wrap { margin: 8px 10px }
.banner-label { display: flex; align-items: center; gap: 4px; padding: 4px 2px; font-size: 12px; color: #999 }
.bl-icon { font-size: 14px }
.bl-text { font-weight: 500 }
.bl-badge { display: inline-block; background: #C62828; color: #fff; font-size: 10px; padding: 1px 6px; border-radius: 8px; font-weight: 700; animation: pulse 2s ease-in-out infinite }
@keyframes pulse { 0%,100% { opacity: 1 } 50% { opacity: .6 } }
.swiper { border-radius: 14px; overflow: hidden }
.banner-card { background: linear-gradient(135deg, #2E7D32, #4CAF50); color: #fff; padding: 16px 18px; min-height: 80px; cursor: pointer; transition: transform .15s }
.banner-card.new { background: linear-gradient(135deg, #1565C0, #1E88E5) }
.banner-card:active { transform: scale(.98) }
.banner-title { font-size: 16px; font-weight: 700; margin: 0 0 6px; line-height: 1.3 }
.banner-content { font-size: 13px; opacity: .9; margin: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap }
.banner-time { font-size: 10px; opacity: .6; margin-top: 4px; display: block }
.badge-tag { display: inline-block; background: #E8F5E9; color: #2E7D32; padding: 4px 12px; border-radius: 12px; font-size: 12px; font-weight: 600 }
</style>
