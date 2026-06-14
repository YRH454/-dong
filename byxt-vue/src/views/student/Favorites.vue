<template>
<div style="position:relative;min-height:60vh">
  <div class="watermark">{{ userId }}</div>
  <van-pull-refresh v-model="refreshing" @refresh="load">
    <div class="quick-stats">
      <div class="qs-item"><b>{{ favTopics.length }}</b><span>已收藏题目</span></div>
      <div class="qs-item" style="text-align:right">
        <van-button size="small" type="success" round @click="$router.push('/student/topics')">去选题</van-button>
      </div>
    </div>

    <!-- Empty State -->
    <div v-if="!refreshing && favTopics.length===0" class="empty-hero">
      <div class="empty-icon-wrap">
        <span class="empty-icon-big">⭐</span>
      </div>
      <h2 class="empty-title">还没有收藏题目</h2>
      <p class="empty-desc">在选题列表中点击星标<br>感兴趣的题目就会出现在这里</p>
      <van-button type="success" round size="large" @click="$router.push('/student/topics')" style="margin-top:12px">去看看题目</van-button>
    </div>

    <!-- Topic List -->
    <div v-if="favTopics.length>0" class="topic-list">
      <div class="topic-card" v-for="t in favTopics" :key="t.id" @click="showDetail(t)">
        <div class="card-body">
          <div class="card-head">
            <h3 class="card-title">{{ t.tm }}</h3>
            <span class="fav-star on" @click.stop="toggleFav(t.id)">⭐</span>
          </div>
          <p class="card-desc" v-if="t.bz">{{ t.bz }}</p>
          <div class="card-meta">
            <span class="teacher-tag">🎯 {{ t.txm }}</span>
            <span class="teacher-tag" v-if="t.tzhicheng">{{ t.tzhicheng }}</span>
          </div>
        </div>
        <div class="card-action">
          <van-button size="small" type="success" round>查看选题</van-button>
        </div>
      </div>
    </div>

    <van-popup v-model:show="showDlg" round position="bottom" :style="{maxHeight:'80vh'}">
      <div style="padding:20px" v-if="detail">
        <h3 style="font-size:18px;color:#2E7D32;margin-bottom:12px">题目详情</h3>
        <van-cell-group inset>
          <van-cell title="题目" :value="detail.tm" />
          <van-cell title="指导教师" :value="detail.txm" is-link @click="showTeacherDetail(detail.gh)" />
          <van-cell title="备注说明" :value="detail.bz||'无'" />
        </van-cell-group>
        <div style="margin:12px 0;display:flex;gap:8px;align-items:center;padding:0 12px">
          <span style="font-size:14px;color:#666">志愿：</span>
          <van-radio-group v-model="choiceNo" direction="horizontal">
            <van-radio :name="1">第一志愿</van-radio>
            <van-radio :name="2">第二志愿</van-radio>
          </van-radio-group>
        </div>
        <van-field v-model="reason" type="textarea" rows="2" placeholder="申请理由（选填）" maxlength="60" />
        <div style="margin-top:16px;display:flex;gap:10px">
          <van-button block round @click="showDlg=false">返回</van-button>
          <van-button block round type="success" @click="doSelect(detail)">提交选择</van-button>
        </div>
      </div>
    </van-popup>

    <van-popup v-model:show="showTeacherDlg" round position="bottom">
      <div style="padding:20px" v-if="teacherInfo">
        <h3 style="font-size:18px;color:#1565C0;margin-bottom:12px">教师详情</h3>
        <van-cell-group inset>
          <van-cell title="姓名" :value="teacherInfo.xm"/>
          <van-cell title="工号" :value="teacherInfo.gh"/>
          <van-cell title="职称" :value="teacherInfo.zhicheng||'无'"/>
          <van-cell title="邮箱" :value="teacherInfo.email||'无'"/>
          <van-cell title="电话" :value="teacherInfo.phone||'无'"/>
          <van-cell title="办公地点" :value="teacherInfo.bgdd||'无'"/>
        </van-cell-group>
        <van-button block round @click="showTeacherDlg=false" style="margin-top:12px">关闭</van-button>
      </div>
    </van-popup>
  </van-pull-refresh>
</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'

const userId = JSON.parse(localStorage.getItem('user')||'{}').userId || ''
const topics = ref([]), refreshing = ref(false)
const showDlg = ref(false), detail = ref(null), reason = ref(''), choiceNo = ref(1)
const showTeacherDlg = ref(false), teacherInfo = ref(null)

const favIds = ref(JSON.parse(localStorage.getItem('favs')||'[]'))
const favTopics = computed(() => topics.value.filter(t => favIds.value.includes(t.id)))

function toggleFav(id) {
  const idx = favIds.value.indexOf(id)
  if (idx >= 0) { favIds.value.splice(idx, 1); showToast('已取消收藏') }
  localStorage.setItem('favs', JSON.stringify(favIds.value))
}

function showDetail(t) { detail.value = t; reason.value = ''; choiceNo.value = 1; showDlg.value = true }
async function showTeacherDetail(gh) {
  try { const r = await api.get('/student/teacher-detail?gh='+gh); if(r.code===0) { teacherInfo.value = r.teacher; showTeacherDlg.value = true } } catch(e) {}
}
async function doSelect(t) {
  try { await showConfirmDialog({ title: '确认选择', message: '确定选择「'+t.tm+'」作为毕业设计题目？' }) } catch { return }
  try { const r = await api.post('/student/select-topic', { tmid: t.id, choice: choiceNo.value, reason: reason.value }); showToast({ message: r.msg, icon: r.code===0?'success':'fail' }); if (r.code===0) { load(); showDlg.value = false } } catch(e) { showToast('提交失败') }
}
async function load() {
  try { const r = await api.get('/student/topics'); if (!r.closed && !r.selected) topics.value = r.list || [] } catch(e) {}
  refreshing.value = false
}
onMounted(load)
</script>

<style scoped>
.quick-stats { display: flex; align-items: center; gap: 10px; padding: 8px 14px; background: #fff; border-bottom: 1px solid #f0f0f0; margin-bottom: 4px }
.qs-item { flex: 1 }
.qs-item b { display: block; font-size: 20px; color: #FF9800; font-weight: 700 }
.qs-item span { font-size: 11px; color: #999 }
.empty-hero { text-align: center; padding: 60px 30px 40px }
.empty-icon-wrap { margin-bottom: 20px }
.empty-icon-big { font-size: 72px; display: inline-block; animation: float 3s ease-in-out infinite }
@keyframes float { 0%,100% { transform: translateY(0) } 50% { transform: translateY(-12px) } }
.empty-title { font-size: 20px; font-weight: 700; color: #333; margin: 0 0 8px }
.empty-desc { font-size: 14px; color: #999; line-height: 1.8; margin: 0 }
.topic-list { padding: 0 10px }
.topic-card { display: flex; align-items: center; background: #fff; border-radius: 14px; padding: 14px; margin-bottom: 8px; box-shadow: 0 2px 8px rgba(0,0,0,.04); cursor: pointer; border-left: 4px solid #FF9800 }
.topic-card:active { transform: scale(.98) }
.card-head { display: flex; align-items: flex-start; justify-content: space-between; gap: 8px }
.card-title { font-size: 16px; font-weight: 700; color: #333; margin: 0; flex: 1; line-height: 1.4 }
.card-desc { font-size: 13px; color: #666; margin: 6px 0 }
.card-meta { display: flex; gap: 8px; flex-wrap: wrap; margin-top: 4px }
.teacher-tag { font-size: 12px; color: #1E88E5; background: #E3F2FD; padding: 2px 8px; border-radius: 6px }
.card-action { margin-left: 10px; flex-shrink: 0 }
.fav-star { font-size: 24px; cursor: pointer; user-select: none; transition: transform .15s; flex-shrink: 0; line-height: 1 }
.fav-star:active { transform: scale(1.3) }
.watermark { position: fixed; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none; z-index: 9999; opacity: .04; font-size: 18px; font-weight: 700; color: #000; user-select: none; padding: 40px; line-height: 3 }
</style>
