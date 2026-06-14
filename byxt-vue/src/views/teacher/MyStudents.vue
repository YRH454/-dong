<template>
<van-pull-refresh v-model="refreshing" @refresh="load">
  <div class="header-bar">
    <div class="stat-row">
      <div class="stat"><b>{{ list.length }}</b><span>指导学生</span></div>
      <div class="stat warn"><b>{{ pendingCount }}</b><span>待确认</span></div>
      <div class="stat"><b>{{ confirmedCount }}</b><span>已确认</span></div>
    </div>
  </div>

  <!-- Taskbook Editor Popup -->
  <van-popup v-model:show="showTb" round position="bottom" :style="{height:'65vh'}">
    <div style="padding:16px;display:flex;flex-direction:column;height:100%">
      <h4 style="margin:0 0 12px">📝 发布任务书</h4>
      <van-field v-model="tbContent" type="textarea" rows="10" placeholder="输入任务书内容（要求、参考文献、时间安排等）" style="flex:1;margin-bottom:8px" />
      <div style="display:flex;gap:8px">
        <van-button block round @click="showTb=false">取消</van-button>
        <van-button block round type="primary" @click="saveTb" :loading="tbSaving">保存发布</van-button>
      </div>
    </div>
  </van-popup>

  <van-empty v-if="!refreshing && !list.length" image="friends-o" description="暂无学生选择你的题目">
    <p style="color:#999;font-size:13px;text-align:center">等待学生选题并确认后，将在此显示</p>
  </van-empty>

  <div class="student-card" v-for="s in list" :key="s.topicId">
    <!-- Student Info -->
    <div class="stu-head">
      <div class="stu-avatar">🎓</div>
      <div class="stu-info">
        <div class="stu-name">{{ s.sxm }} <span class="stu-xh">{{ s.xh }}</span></div>
        <div class="stu-meta">{{ s.zy||'未知专业' }} · {{ s.bj||'未知班级' }}</div>
      </div>
      <van-tag v-if="s.status===2" type="success" size="small">已确认</van-tag>
      <van-tag v-else-if="s.status===1" type="warning" size="small">待确认</van-tag>
      <van-tag v-else-if="s.status===3" type="danger" size="small">已驳回</van-tag>
    </div>

    <!-- Topic -->
    <div class="stu-topic">📝 题目：{{ s.tm }}</div>
    <div class="stu-reason" v-if="s.reason">💬 申请理由：{{ s.reason }}</div>

    <!-- Contact -->
    <div class="stu-contact">
      <span v-if="s.phone">📞 {{ s.phone }}</span>
      <span v-if="s.email">📧 {{ s.email }}</span>
      <span v-if="s.qq">💬 {{ s.qq }}</span>
    </div>

    <!-- Report Status & Review -->
    <div class="report-status" v-if="s.status===2">
      <div class="rs-line">
        <span class="rs-label">开题报告：</span>
        <van-tag v-if="s.reportStatus===-1" type="default" size="small">未提交</van-tag>
        <van-tag v-else-if="s.reportStatus===0" type="warning" size="small">待审核</van-tag>
        <van-tag v-else-if="s.reportStatus===1" type="success" size="small">已通过</van-tag>
        <van-tag v-else-if="s.reportStatus===2" type="danger" size="small">已驳回</van-tag>
      </div>
      <!-- Review actions for pending reports -->
      <div class="review-actions" v-if="s.reportStatus===0 || s.reportStatus===2">
        <van-button size="mini" type="success" @click="reviewReport(s, 1)">通过</van-button>
        <van-button size="mini" type="danger" @click="reviewReport(s, 2)">驳回</van-button>
      </div>
      <div class="report-comment" v-if="s.reportComment">
        <span class="rc-label">评语：</span>{{ s.reportComment }}
      </div>
    </div>

    <!-- Taskbook -->
    <div class="stu-actions" v-if="s.status===2">
      <van-button size="small" plain type="primary" @click="openTb(s)">{{ s.taskbook ? '📝 编辑任务书' : '📝 发布任务书' }}</van-button>
    </div>
  </div>
</van-pull-refresh>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'

const list = ref([]), refreshing = ref(false)
const showTb = ref(false), tbContent = ref(''), tbSaving = ref(false), tbTopicId = ref(0)

const pendingCount = computed(() => list.value.filter(s => s.status === 1).length)
const confirmedCount = computed(() => list.value.filter(s => s.status === 2).length)

async function load() {
  try { const r = await api.get('/teacher/my-students'); if (r.code === 0) list.value = r.list } catch(e) {}
  refreshing.value = false
}

async function reviewReport(s, status) {
  const label = status === 1 ? '通过' : '驳回'
  try { await showConfirmDialog({ title: label + '开题报告', message: '确定' + label + '该学生的开题报告？' }) } catch { return }
  try {
    const comment = status === 1 ? '报告审核通过' : '报告需修改后重新提交'
    const r = await api.post('/teacher/review-report', { id: s.reportId, status, comment })
    showToast({ message: r.msg, icon: r.code===0?'success':'fail' })
    if (r.code === 0) load()
  } catch(e) { showToast('审核失败') }
}

async function openTb(s) {
  tbTopicId.value = s.topicId; tbContent.value = ''
  if (s.taskbook) {
    try { const r = await api.get('/student/get-taskbook-content?topicId='+s.topicId); if (r.code===0) tbContent.value = r.content } catch(e) {}
  }
  showTb.value = true
}
async function saveTb() {
  if (!tbContent.value.trim()) { showToast('请输入任务书内容'); return }
  tbSaving.value = true
  try { const r = await api.post('/teacher/save-taskbook-text', { topicId: tbTopicId.value, content: tbContent.value }); showToast({ message: r.msg, icon: r.code===0?'success':'fail' }); if (r.code===0) { showTb.value = false; load() } } catch(e) { showToast('保存失败') }
  tbSaving.value = false
}

onMounted(load)
</script>

<style scoped>
.header-bar { padding: 10px 14px; background: #fff; border-bottom: 1px solid #f0f0f0; position: sticky; top: 0; z-index: 10 }
.stat-row { display: flex; gap: 16px }
.stat { text-align: center; padding: 4px 8px; border-radius: 8px }
.stat b { display: block; font-size: 20px; color: #1E88E5; font-weight: 700 }
.stat span { font-size: 11px; color: #999 }
.stat.warn b { color: #FB8C00 }
.student-card { background: #fff; border-radius: 14px; padding: 16px; margin: 8px 10px; box-shadow: 0 2px 12px rgba(0,0,0,.04) }
.stu-head { display: flex; align-items: center; gap: 10px; margin-bottom: 10px }
.stu-avatar { width: 36px; height: 36px; border-radius: 50%; background: #E3F2FD; display: flex; align-items: center; justify-content: center; font-size: 18px }
.stu-info { flex: 1 }
.stu-name { font-size: 15px; font-weight: 600; color: #333 }
.stu-xh { font-size: 12px; color: #999; font-weight: 400; margin-left: 6px }
.stu-meta { font-size: 12px; color: #888; margin-top: 2px }
.stu-topic { font-size: 14px; color: #1565C0; font-weight: 500; margin-bottom: 4px }
.stu-reason { font-size: 13px; color: #888; margin-bottom: 6px }
.stu-contact { display: flex; flex-wrap: wrap; gap: 10px; font-size: 12px; color: #888; margin-bottom: 8px }
.report-status { padding: 8px 0; border-top: 1px solid #f5f5f5; margin-bottom: 4px }
.rs-line { display: flex; align-items: center; gap: 6px; font-size: 13px }
.rs-label { color: #666 }
.review-actions { display: flex; gap: 8px; margin-top: 8px }
.report-comment { margin-top: 6px; padding: 8px; background: #f8f9fb; border-radius: 6px; font-size: 13px; color: #555 }
.rc-label { font-weight: 500; color: #333 }
.stu-actions { display: flex; gap: 8px; padding-top: 8px; border-top: 1px solid #f5f5f5 }
.file-hint { font-size: 10px; color: #bbb; margin: 2px 0 0 }
</style>
