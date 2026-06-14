<template>
<van-pull-refresh v-model="refreshing" @refresh="load">
  <!-- Stats + Filter -->
  <div class="header-bar">
    <div class="stat-row">
      <div class="stat" @click="filter='all'" :class="{active:filter==='all'}"><b>{{ list.length }}</b><span>总题数</span></div>
      <div class="stat warn" @click="filter='pending'" :class="{active:filter==='pending'}"><b>{{ pendingCount }}</b><span>待确认</span></div>
      <div class="stat" @click="filter='confirmed'" :class="{active:filter==='confirmed'}"><b>{{ confirmedCount }}</b><span>已确认</span></div>
    </div>
    <van-button size="small" plain type="primary" @click="exportMy" :loading="exp">导出</van-button>
  </div>

  <!-- Filtered Topic Cards -->
  <van-empty v-if="!refreshing && !filteredList.length" :description="filter==='pending'?'没有待确认的选题':filter==='confirmed'?'没有已确认的选题':'暂无出题记录'" />

  <div class="t-card" v-for="t in filteredList" :key="t.id">
    <div class="t-head">
      <span class="t-title">{{ t.tm }}</span>
      <van-tag v-if="t.status===1" type="warning" size="small">待确认</van-tag>
      <van-tag v-else-if="t.status===2&&t.xh" type="success" size="small">已确认</van-tag>
      <van-tag v-else-if="t.status===3" type="danger" size="small">已驳回</van-tag>
      <van-tag v-else size="small">待选</van-tag>
    </div>
    <div class="t-body">
      <div v-if="t.xh">学生：{{ t.sxm }}（{{ t.xh }}）{{ t.zy||'' }} {{ t.bj||'' }}</div>
      <div v-if="t.reason">理由：{{ t.reason }}</div>
      <div v-if="t.bz">备注：{{ t.bz }}</div>
    </div>
    <div class="t-actions">
      <van-button v-if="t.status===1" size="small" type="success" @click="confirmStu(t.id)">确认</van-button>
      <van-button v-if="t.status===1" size="small" type="danger" @click="rejectStu(t.id)">驳回</van-button>
      <van-button v-if="!t.xh" size="small" plain type="danger" @click="deleteTopic(t)">删除</van-button>
      <template v-if="t.status===2&&t.xh">
        <van-button size="small" plain type="primary" @click="openTaskbook(t)">{{ t.file_path ? '📝 查看/编辑任务书' : '📝 发布任务书' }}</van-button>
      </template>
    </div>
  </div>

  <!-- Taskbook Editor Popup -->
  <van-popup v-model:show="showTb" round position="bottom" :style="{height:'70vh'}">
    <div style="padding:16px;display:flex;flex-direction:column;height:100%">
      <h4 style="margin:0 0 12px">{{ tbFile ? '编辑任务书' : '发布任务书' }}</h4>
      <van-field v-model="tbTitle" label="标题" placeholder="任务书标题" />
      <van-field v-model="tbContent" type="textarea" rows="10" placeholder="输入任务书内容（要求、参考文献、时间安排等）" style="flex:1;margin:8px 0" />
      <div style="display:flex;gap:8px">
        <van-button block round @click="showTb=false">取消</van-button>
        <van-button block round type="primary" @click="saveTaskbook" :loading="tbSaving">保存发布</van-button>
      </div>
    </div>
  </van-popup>
</van-pull-refresh>
</template>

<script setup>
import { ref, computed, reactive, onMounted, onUnmounted, onActivated } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'

const list = ref([]), selCount = ref(0), refreshing = ref(false), exp = ref(false), filter = ref('all')
const showTb = ref(false), tbTitle = ref(''), tbContent = ref(''), tbSaving = ref(false), tbTopicId = ref(0), tbFile = ref(false)

const pendingCount = computed(() => list.value.filter(t => t.status === 1).length)
const confirmedCount = computed(() => list.value.filter(t => t.status === 2 && t.xh).length)
const filteredList = computed(() => {
  if (filter.value === 'pending') return list.value.filter(t => t.status === 1)
  if (filter.value === 'confirmed') return list.value.filter(t => t.status === 2 && t.xh)
  return list.value
})

async function load() { try { const r = await api.get('/teacher/my-topics'); list.value = r.list || []; selCount.value = r.selectedCount } catch (e) {}; refreshing.value = false }

async function confirmStu(id) {
  try { await showConfirmDialog({ title: '确认选题', message: '确认该学生的选题申请？' }) } catch { return }
  try { await api.post('/teacher/confirm-student', { id }); showToast({ message: '已确认', icon: 'success' }); load() } catch (e) {}
}
async function rejectStu(id) {
  try { await showConfirmDialog({ title: '驳回申请', message: '驳回后学生可重新选题' }) } catch { return }
  try { await api.post('/teacher/reject-student', { id }); showToast({ message: '已驳回', icon: 'success' }); load() } catch (e) {}
}
async function deleteTopic(t) {
  try { await showConfirmDialog({ title: '删除题目', message: '确定删除题目「' + t.tm + '」？' }) } catch { return }
  try { await api.post('/teacher/delete-topic', { id: t.id }); showToast({ message: '已删除', icon: 'success' }); load() } catch (e) { showToast('删除失败') }
}
async function openTaskbook(t) {
  tbTopicId.value = t.id; tbFile.value = !!t.file_path; tbTitle.value = ''; tbContent.value = ''
  if (t.file_path) {
    try { const r = await api.get('/student/get-taskbook-content?topicId='+t.id); if (r.code===0) tbContent.value = r.content } catch(e) {}
  }
  showTb.value = true
}
async function saveTaskbook() {
  if (!tbContent.value.trim()) { showToast('请输入任务书内容'); return }
  tbSaving.value = true
  try { const r = await api.post('/teacher/save-taskbook-text', { topicId: tbTopicId.value, content: tbContent.value }); showToast({ message: r.msg, icon: r.code===0?'success':'fail' }); if (r.code===0) { showTb.value = false; load() } } catch(e) { showToast('保存失败') }
  tbSaving.value = false
}
async function exportMy() {
  exp.value = true
  try { let csv = '工号,教师姓名,题目,备注,学号,学生姓名,专业,班级\n'; list.value.forEach(t => { csv += `${t.gh},${t.txm},${t.tm},${t.bz||''},${t.xh||''},${t.sxm||''},${t.zy||''},${t.bj||''}\n` }); const blob = new Blob([csv], { type: 'text/csv;charset=utf-8' }); const a = document.createElement('a'); a.href = URL.createObjectURL(blob); a.download = '选题结果.csv'; a.click(); showToast({ message: '导出成功', icon: 'success' }) } catch (e) {}
  exp.value = false
}
let timer = 0
onMounted(() => { load(); timer = setInterval(load, 30000) })
onActivated(() => { load() })
onUnmounted(() => clearInterval(timer))
</script>

<style scoped>
.header-bar { padding: 10px 14px; display: flex; justify-content: space-between; align-items: center; gap: 10px; background: #fff; border-bottom: 1px solid #f0f0f0; position: sticky; top: 0; z-index: 10 }
.stat-row { display: flex; gap: 16px }
.stat { text-align: center; cursor: pointer; padding: 4px 8px; border-radius: 8px; transition: all .15s }
.stat b { display: block; font-size: 20px; color: #1E88E5; font-weight: 700 }
.stat span { font-size: 11px; color: #999 }
.stat.warn b { color: #FB8C00 }
.stat.active { background: #E3F2FD }
.stat.active span { color: #1565C0; font-weight: 600 }
.t-card { background: #fff; border-radius: 12px; padding: 14px; margin: 6px 10px; box-shadow: 0 1px 4px rgba(0,0,0,.04) }
.t-head { display: flex; align-items: center; gap: 8px; margin-bottom: 8px }
.t-title { font-size: 15px; font-weight: 600; color: #333; flex: 1 }
.t-body { font-size: 13px; color: #666; line-height: 1.8; margin-bottom: 8px }
.t-actions { display: flex; gap: 8px; flex-wrap: wrap; align-items: center }
.file-hint { font-size: 10px; color: #bbb; margin: 2px 0 0 }
</style>
