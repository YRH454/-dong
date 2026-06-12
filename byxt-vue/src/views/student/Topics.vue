<template>
<van-pull-refresh v-model="refreshing" @refresh="onRefresh">
  <van-search v-model="keyword" placeholder="搜索教师或题目..." shape="round" @search="onSearch" @clear="onSearch" @input="debounceSearch" />
  <van-notice-bar v-if="!closed && !selected && topics.length" left-icon="info-o" scrollable text="列表自动刷新，点击题目右侧按钮即可选题" background="#E8F5E9" color="#2E7D32" />
  <van-empty v-if="closed" image="error" description="现在不是选题时间，请等待管理员开放" />
  <van-empty v-else-if="selected" image="network" description="你已选过题目，不能重复选择" />
  <van-empty v-else-if="!refreshing && !topics.length" image="search" description="暂无可选题目，请等待教师出题" />
  <div v-else>
    <div class="count-bar">共 {{ filtered.length }} 个题目 <van-tag type="success" size="small">{{ page }}/{{ totalPages }}</van-tag></div>
    <van-cell v-for="t in pageData" :key="t.id" :title="t.tm" :label="`教师：${t.txm}${t.bz ? '  |  ' + t.bz : ''}`" size="large" clickable @click="showDetail(t)">
      <template #right-icon><van-button size="small" type="success" round>选题</van-button></template>
    </van-cell>
    <van-pagination v-if="totalPages>1" v-model="page" :total-items="filtered.length" :page-size="10" @change="onPage" style="margin-top:12px" />
  </div>

  <!-- Topic Detail Popup -->
  <van-popup v-model:show="showDlg" round position="bottom" :style="{maxHeight:'80vh'}">
    <div style="padding:20px" v-if="detail">
      <h3 style="font-size:18px;color:#2E7D32;margin-bottom:12px">📝 题目详情</h3>
      <van-cell-group inset>
        <van-cell title="题目" :value="detail.tm" />
        <van-cell title="指导教师" :value="detail.txm" is-link @click="showTeacher(detail.gh)" />
        <van-cell title="备注说明" :value="detail.bz||'无'" />
      </van-cell-group>
      <!-- Choice selector -->
      <div style="margin:12px 0;display:flex;gap:8px;align-items:center;padding:0 12px">
        <span style="font-size:14px;color:#666">志愿：</span>
        <van-radio-group v-model="choiceNo" direction="horizontal">
          <van-radio :name="1" :disabled="choiceCount>=1&&!detail.choice">第一志愿</van-radio>
          <van-radio :name="2" :disabled="choiceCount>=2">第二志愿</van-radio>
        </van-radio-group>
      </div>
      <!-- Reason -->
      <van-field v-model="reason" type="textarea" rows="2" placeholder="申请理由（选填，30字以内）" maxlength="60" />
      <div style="margin-top:16px;display:flex;gap:10px">
        <van-button block round @click="showDlg=false">返回</van-button>
        <van-button block round type="success" @click="doSelect(detail)">提交选择</van-button>
      </div>
    </div>
  </van-popup>

  <!-- Teacher Detail Popup -->
  <van-popup v-model:show="showTeacherDlg" round position="bottom">
    <div style="padding:20px" v-if="teacherInfo">
      <h3 style="font-size:18px;color:#1565C0;margin-bottom:12px">👨‍🏫 教师详情</h3>
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
</template>

<script setup>
import { ref, computed } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'

const topics = ref([]), keyword = ref(''), page = ref(1), refreshing = ref(false)
const closed = ref(false), selected = ref(false), timer = ref(0), choiceCount = ref(0)
const showDlg = ref(false), detail = ref(null), reason = ref(''), choiceNo = ref(1)
const showTeacherDlg = ref(false), teacherInfo = ref(null)

function showDetail(t) { detail.value = t; reason.value = ''; choiceNo.value = choiceCount.value >= 1 ? 2 : 1; showDlg.value = true }

async function showTeacher(gh) {
  try { const r = await api.get('/student/teacher-detail?gh='+gh); if(r.code===0) { teacherInfo.value = r.teacher; showTeacherDlg.value = true } } catch(e) {}
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
  } catch (e) { /* server might be restarting */ }
}
async function onRefresh() { await loadData(); refreshing.value = false }
async function doSelect(t) {
  try { await showConfirmDialog({ title: '确认选题', message: `选择「${t.tm}」？选定后不可更改` }) } catch { return }
  try {
    const r = await api.post('/student/select-topic', { tmid: t.id, choice: choiceNo.value, reason: reason.value })
    if (r.code === 0) { showDlg.value = false; showToast({ message: '已提交志愿'+choiceNo.value+'，等待教师确认', icon: 'success' }); loadData() }
    else showToast({ message: r.msg, icon: 'fail' })
  } catch (e) { showToast('网络错误，请重试') }
}
function onPage(p) { page.value = p }
function onSearch() { page.value = 1 }
let debounceTimer = 0
function debounceSearch() { clearTimeout(debounceTimer); debounceTimer = setTimeout(onSearch, 300) }

import { onMounted, onUnmounted } from 'vue'
onMounted(() => { loadData(); timer.value = setInterval(loadData, 30000) })
onUnmounted(() => clearInterval(timer.value))
</script>

<style scoped>
.count-bar { padding: 6px 16px; font-size: 13px; color: #888; display: flex; align-items: center; gap: 8px }
</style>
