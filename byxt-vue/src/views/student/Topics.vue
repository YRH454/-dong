<template>
<div>
  <div class="tip">每30秒自动刷新 &nbsp;<a @click="loadData">&#x21bb; 手动刷新</a> &nbsp;|&nbsp; 共 {{ topics.length }} 题</div>
  <van-search v-model="keyword" placeholder="搜索教师/题目..." @search="onSearch" @input="onSearch" shape="round" />
  <van-empty v-if="closed" description="现在不是选题时间" />
  <van-empty v-else-if="selected" description="你已选过题目" />
  <van-empty v-else-if="!topics.length" description="暂无可选题目" />
  <div v-else>
    <van-cell v-for="t in pageData" :key="t.id" :title="t.tm" :label="'教师：' + t.txm + (t.bz ? ' ｜ ' + t.bz : '')">
      <template #right-icon><van-button size="small" type="primary" round @click="doSelect(t)">选择</van-button></template>
    </van-cell>
    <van-pagination v-model="page" :total-items="filtered.length" :page-size="10" @change="onPage" style="margin-top:12px" />
  </div>
</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
const topics = ref([]), keyword = ref(''), page = ref(1), closed = ref(false), selected = ref(false)
const filtered = computed(() => topics.value.filter(t => !keyword.value || t.tm.includes(keyword.value) || t.txm.includes(keyword.value)))
const pageData = computed(() => filtered.value.slice((page.value-1)*10, page.value*10))

async function loadData() {
  try { const r = await api.get('/student/topics'); if (r.closed) closed.value = true; else if (r.selected) selected.value = true; else topics.value = r.list || [] } catch (e) {}
}
async function doSelect(t) {
  try { await showConfirmDialog({ title: '确认选题', message: '选择「' + t.tm + '」？' }) } catch { return }
  try { const r = await api.post('/student/select-topic', { tmid: t.id })
    if (r.code === 0) { showToast('选题成功'); loadData() } else showToast(r.msg) } catch (e) {}
}
function onPage(p) { page.value = p }
function onSearch() { page.value = 1 }
onMounted(loadData)
</script>
<style scoped>.tip{background:#FFF8E1;color:#F57F17;padding:10px 14px;font-size:13px;border-left:4px solid #FFC107;margin-bottom:10px;border-radius:0 8px 8px 0}.tip a{color:#E65100;font-weight:600}</style>
