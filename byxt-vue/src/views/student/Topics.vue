<template>
<div style="position:relative">
  <div class="watermark">{{ user?.userId }}</div>
  <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
    <AnnounceBanner :adminId="adminId" />
    <!-- Quick Stats -->
    <div class="quick-stats" v-if="!closed && !selected">
      <div class="qs-item"><b>{{ topics.length }}</b><span>可选题目</span></div>
      <div class="qs-item warn" v-if="choiceCount>0"><b>{{ choiceCount }}</b><span>已提交</span></div>
      <div class="qs-item"><b>{{ 3-choiceCount }}</b><span>剩余志愿</span></div>
    </div>
    <van-search v-model="keyword" placeholder="搜索教师或题目..." shape="round" @search="onSearch" @clear="onSearch" @input="debounceSearch" />
    <van-empty v-if="closed" image="error" description="现在不是选题时间" />
    <van-empty v-else-if="selected" image="network" description="你已选过题目" />
    <van-empty v-else-if="!refreshing && !topics.length" image="search" description="暂无可选题目" />
    <div v-else class="topic-list">
      <div class="list-header">
        <span>共 {{ filtered.length }} 题</span>
        <van-tag type="success" size="small">{{ page }}/{{ totalPages }} 页</van-tag>
      </div>
      <!-- Topic Cards -->
      <div class="topic-card" :class="{selected:t.xh}" v-for="t in pageData" :key="t.id" @click="t.xh ? null : showDetail(t)">
        <div class="card-body">
          <div class="card-head">
            <h3 class="card-title">{{ t.tm }}
              <span v-if="t.xh" class="selected-badge">已被选</span>
            </h3>
            <span class="fav-star" :class="{on:isFav(t.id)}" @click.stop="toggleFav(t.id)">{{ isFav(t.id) ? '⭐' : '☆' }}</span>
          </div>
          <p class="card-desc" v-if="t.bz">{{ t.bz }}</p>
          <p class="card-desc" v-if="t.xh">已被 {{ t.sxm||t.xh }} 选择</p>
          <div class="card-meta">
            <span class="teacher-tag">🎯 {{ t.txm }}</span>
          </div>
        </div>
        <div class="card-action">
          <van-button v-if="t.xh" size="small" type="default" round disabled>不可选</van-button>
          <van-button v-else size="small" type="success" round>查看选题</van-button>
        </div>
      </div>
      <van-pagination v-if="totalPages>1" v-model="page" :total-items="filtered.length" :page-size="10" @change="onPage" style="margin-top:12px" />
    </div>

    <van-popup v-model:show="showDlg" round position="bottom" :style="{maxHeight:'80vh'}">
      <div style="padding:20px" v-if="detail">
        <h3 style="font-size:18px;color:#2E7D32;margin-bottom:12px">题目详情</h3>
        <van-cell-group inset>
          <van-cell title="题目" :value="detail.tm" />
          <van-cell title="指导教师" :value="detail.txm" is-link @click="showTeacher(detail.gh)" />
          <van-cell title="备注说明" :value="detail.bz||'无'" />
        </van-cell-group>
        <div style="margin:12px 0;display:flex;gap:8px;align-items:center;padding:0 12px">
          <span style="font-size:14px;color:#666">志愿：</span>
          <van-radio-group v-model="choiceNo" direction="horizontal">
            <van-radio :name="1" :disabled="choiceCount>=1">第一志愿</van-radio>
            <van-radio :name="2" :disabled="choiceCount>=2">第二志愿</van-radio>
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
import { ref, computed, onMounted, onActivated } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
import AnnounceBanner from '../../components/AnnounceBanner.vue'

const user = JSON.parse(localStorage.getItem('user') || '{}')
const adminId = user.adminId || ''

const topics = ref([]), keyword = ref(''), page = ref(1), refreshing = ref(false)
const closed = ref(false), selected = ref(false), timer = ref(0), choiceCount = ref(0)
const showDlg = ref(false), detail = ref(null), reason = ref(''), choiceNo = ref(1)
const showTeacherDlg = ref(false), teacherInfo = ref(null)

function showDetail(t) { detail.value = t; reason.value = ''; choiceNo.value = choiceCount.value >= 1 ? 2 : 1; showDlg.value = true }
async function showTeacher(gh) {
  try { const r = await api.get('/student/teacher-detail?gh='+gh); if(r.code===0) { teacherInfo.value = r.teacher; showTeacherDlg.value = true } } catch(e) {}
}

// Favorites
const favIds = ref(JSON.parse(localStorage.getItem('favs')||'[]'))
function isFav(id) { return favIds.value.includes(id) }
function toggleFav(id) {
  const idx = favIds.value.indexOf(id)
  if (idx >= 0) { favIds.value.splice(idx, 1); showToast('已取消收藏') }
  else { favIds.value.push(id); showToast({ message: '已收藏', icon: 'success' }) }
  localStorage.setItem('favs', JSON.stringify(favIds.value))
}

const filtered = computed(() => topics.value.filter(t => !keyword.value || t.tm.includes(keyword.value) || (t.txm||'').includes(keyword.value)))
const totalPages = computed(() => Math.ceil(filtered.value.length / 10))
const pageData = computed(() => filtered.value.slice((page.value-1)*10, page.value*10))

async function loadData() {
  try {
    const r = await api.get('/student/topics')
    if (r.closed) { closed.value = true; return }
    if (r.selected) { selected.value = true; return }
    closed.value = false; selected.value = false
    topics.value = r.list || []; choiceCount.value = r.choiceCount || 0
  } catch (e) {}
}
async function onRefresh() { await loadData(); refreshing.value = false }
async function doSelect(t) {
  try { await showConfirmDialog({ title: '确认选题', message: '选择「'+t.tm+'」？' }) } catch { return }
  try {
    const r = await api.post('/student/select-topic', { tmid: t.id, choice: choiceNo.value, reason: reason.value })
    if (r.code === 0) { showDlg.value = false; showToast({ message: '已提交，等待教师确认', icon: 'success' }); loadData() }
    else showToast({ message: r.msg, icon: 'fail' })
  } catch (e) { showToast('网络错误') }
}
function onPage(p) { page.value = p }
function onSearch() { page.value = 1 }
let db = 0
function debounceSearch() { clearTimeout(db); db = setTimeout(onSearch, 300) }

import { onUnmounted } from 'vue'
onMounted(() => { loadData(); timer.value = setInterval(loadData, 30000) })
onActivated(() => { loadData() })
onUnmounted(() => clearInterval(timer.value))
</script>

<style scoped>
.quick-stats { display: flex; gap: 10px; padding: 8px 14px; background: #fff; border-bottom: 1px solid #f0f0f0; margin-bottom: 4px }
.qs-item { flex: 1; text-align: center }
.qs-item b { display: block; font-size: 20px; color: #4CAF50; font-weight: 700 }
.qs-item span { font-size: 11px; color: #999 }
.qs-item.warn b { color: #FB8C00 }
.topic-list { padding: 0 10px }
.list-header { display: flex; justify-content: space-between; align-items: center; padding: 6px 6px 10px; font-size: 14px; color: #666; font-weight: 500 }
.topic-card { display: flex; align-items: center; background: #fff; border-radius: 14px; padding: 14px; margin-bottom: 8px; box-shadow: 0 2px 8px rgba(0,0,0,.04); cursor: pointer; transition: all .15s; border-left: 4px solid #4CAF50 }
.topic-card.selected { border-left-color: #ccc; opacity: .75; cursor: default }
.topic-card:active { transform: scale(.98); box-shadow: 0 1px 4px rgba(0,0,0,.06) }
.topic-card.selected:active { transform: none }
.card-head { display: flex; align-items: flex-start; justify-content: space-between; gap: 8px }
.fav-star { font-size: 24px; cursor: pointer; user-select: none; transition: transform .15s; flex-shrink: 0; line-height: 1 }
.fav-star:active { transform: scale(1.3) }
.fav-star.on { filter: none }
.selected-badge { display: inline-block; background: #C62828; color: #fff; font-size: 11px; padding: 2px 8px; border-radius: 10px; font-weight: 600; margin-left: 8px; vertical-align: middle }
.card-body { flex: 1; min-width: 0 }
.card-title { font-size: 15px; font-weight: 600; color: #333; margin: 0 0 4px; line-height: 1.4; overflow: hidden; text-overflow: ellipsis; white-space: nowrap }
.card-desc { font-size: 12px; color: #999; margin: 0 0 6px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap }
.card-meta { display: flex; align-items: center; gap: 8px }
.teacher-tag { font-size: 12px; color: #2E7D32; background: #E8F5E9; padding: 2px 8px; border-radius: 10px }
.card-action { flex-shrink: 0; margin-left: 10px }
:deep(.van-search) { padding: 8px 10px }
:deep(.van-pull-refresh) { min-height: 100vh; background: linear-gradient(180deg, #f5f7f5, #fafcfa) }
.watermark { position: fixed; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none; z-index: 9999; opacity: .04; font-size: 18px; font-weight: 700; color: #000; user-select: none; padding: 40px; line-height: 3 }
</style>
