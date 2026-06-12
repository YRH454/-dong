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
  <van-popup v-model:show="showDlg" round position="bottom" :style="{maxHeight:'70vh'}">
    <div style="padding:20px" v-if="detail">
      <h3 style="font-size:18px;color:#2E7D32;margin-bottom:16px">📝 题目详情</h3>
      <van-cell-group inset>
        <van-cell title="题目" :value="detail.tm" />
        <van-cell title="指导教师" :value="detail.txm" />
        <van-cell title="备注说明" :value="detail.bz||'无'" />
      </van-cell-group>
      <div style="margin-top:20px;display:flex;gap:10px">
        <van-button block round @click="showDlg=false">返回</van-button>
        <van-button block round type="success" @click="doSelect(detail)">确认选择</van-button>
      </div>
    </div>
  </van-popup>
</van-pull-refresh>
</template>

<script setup>
import { ref, computed } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'

const topics = ref([]), keyword = ref(''), page = ref(1), refreshing = ref(false)
const closed = ref(false), selected = ref(false), timer = ref(0)
const showDlg = ref(false), detail = ref(null)

function showDetail(t) { detail.value = t; showDlg.value = true }

const filtered = computed(() => topics.value.filter(t => !keyword.value || t.tm.includes(keyword.value) || (t.txm||'').includes(keyword.value)))
const totalPages = computed(() => Math.ceil(filtered.value.length / 10))
const pageData = computed(() => filtered.value.slice((page.value-1)*10, page.value*10))

async function loadData() {
  try {
    const r = await api.get('/student/topics')
    if (r.closed) { closed.value = true; return }
    if (r.selected) { selected.value = true; return }
    closed.value = false; selected.value = false
    topics.value = r.list || []
  } catch (e) { /* server might be restarting */ }
}
async function onRefresh() { await loadData(); refreshing.value = false }
async function doSelect(t) {
  try { await showConfirmDialog({ title: '确认选题', message: `选择「${t.tm}」？选定后不可更改` }) } catch { return }
  try {
    const r = await api.post('/student/select-topic', { tmid: t.id })
    if (r.code === 0) { showToast({ message: '选题成功！请在选题结果中查看', icon: 'success' }); loadData() }
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
