<template>
<div style="position:relative">
  <div class="watermark">{{ userId }}</div>
  <van-pull-refresh v-model="refreshing" @refresh="load">
    <!-- Empty State -->
    <div v-if="!refreshing && !list.length" class="empty-wrap">
      <div class="empty-card">
        <div class="empty-icon">📋</div>
        <h3>还没有选题记录</h3>
        <p>选一个感兴趣的题目开始你的毕业设计之旅吧</p>
        <van-button type="success" round @click="$router.push('/student/topics')">去选题</van-button>
      </div>
    </div>

    <!-- Result Content -->
    <div v-else class="result-wrap">
      <div class="result-card" v-for="item in list" :key="item.id">
        <!-- Progress Timeline -->
        <div class="timeline">
          <div class="tl-step done">
            <div class="tl-dot">1</div>
            <div class="tl-label">提交申请</div>
          </div>
          <div class="tl-line" :class="{active: item.status===2||item.status===3}"></div>
          <div class="tl-step" :class="{done:item.status===2, fail:item.status===3}">
            <div class="tl-dot">{{ item.status===2?'✓':item.status===3?'✕':'2' }}</div>
            <div class="tl-label">{{ item.status===2?'已通过':item.status===3?'已驳回':'待审核' }}</div>
          </div>
          <div class="tl-line" :class="{active: item.status===2}"></div>
          <div class="tl-step" :class="{done:item.status===2}">
            <div class="tl-dot">3</div>
            <div class="tl-label">选题完成</div>
          </div>
        </div>

        <!-- Topic Title -->
        <h3 class="topic-name">{{ item.tm }}</h3>

        <!-- Teacher Card -->
        <div class="teacher-card">
          <div class="teacher-avatar">👨‍🏫</div>
          <div class="teacher-info">
            <div class="teacher-name">{{ item.txm }} <span class="teacher-title">{{ item.tzhicheng||'' }}</span></div>
            <div class="teacher-contact">
              <span v-if="item.tphone">📞 {{ item.tphone }}</span>
              <span v-if="item.tqq">💬 {{ item.tqq }}</span>
              <span v-if="item.tbgdd">📍 {{ item.tbgdd }}</span>
            </div>
          </div>
        </div>

        <!-- Details -->
        <div class="detail-row" v-if="item.bz">
          <span class="detail-label">备注</span>
          <span class="detail-value">{{ item.bz }}</span>
        </div>
        <!-- Taskbook view -->
        <div class="taskbook-card" v-if="item.file_path" @click="viewTaskbook(item.id)">
          <div class="tb-left">
            <span class="tb-icon">📥</span>
            <div>
              <div class="tb-title">任务书已下发</div>
              <div class="tb-hint">点击查看老师发布的任务书</div>
            </div>
          </div>
          <van-tag type="success" size="small">查看</van-tag>
        </div>

        <!-- Report Section (for confirmed topics) -->
        <div class="report-section" v-if="item.status===2">
          <div class="report-header">📄 开题报告</div>
          <div v-if="!item.reportStatus || item.reportStatus===-1" class="report-empty">
            <span>尚未提交开题报告</span>
            <van-button size="small" type="success" round @click="openReport(item)">写报告</van-button>
          </div>
          <div v-else class="report-info">
            <div class="report-status-row">
              <van-tag v-if="item.reportStatus===0" type="warning">待审核</van-tag>
              <van-tag v-else-if="item.reportStatus===1" type="success">已通过</van-tag>
              <van-tag v-else-if="item.reportStatus===2" type="danger">已驳回</van-tag>
              <van-button size="mini" plain type="primary" @click="openReport(item)">重新提交</van-button>
            </div>
            <div class="report-comment" v-if="item.reportComment">
              <span class="rc-label">教师评语：</span>{{ item.reportComment }}
            </div>
          </div>
        </div>

        <!-- Actions -->
        <div class="action-row" v-if="canDelete && item.status !== 2">
          <van-button type="danger" size="small" round @click="doDel">删除记录</van-button>
        </div>
      </div>
    </div>
    <!-- Taskbook View Popup -->
    <van-popup v-model:show="showTb" round position="bottom" :style="{height:'70vh'}">
      <div style="padding:16px;overflow-y:auto;height:100%">
        <h4 style="margin:0 0 12px">📥 任务书</h4>
        <div style="white-space:pre-wrap;font-size:14px;line-height:1.8;color:#333">{{ tbContent }}</div>
        <van-button block round style="margin-top:16px" @click="showTb=false">关闭</van-button>
      </div>
    </van-popup>

    <!-- Report Editor Popup -->
    <van-popup v-model:show="showRp" round position="bottom" :style="{height:'70vh'}">
      <div style="padding:16px;display:flex;flex-direction:column;height:100%">
        <h4 style="margin:0 0 12px">📄 开题报告</h4>
        <van-field v-model="rpContent" type="textarea" rows="10" placeholder="输入开题报告内容（研究背景、方法、预期成果等）" style="flex:1;margin-bottom:8px" />
        <div style="display:flex;gap:8px">
          <van-button block round @click="showRp=false">取消</van-button>
          <van-button block round type="success" @click="submitReport" :loading="rpSaving">提交报告</van-button>
        </div>
      </div>
    </van-popup>
  </van-pull-refresh>
</div>
</template>

<script setup>
import { ref, reactive, onMounted, onActivated } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
const userId = JSON.parse(localStorage.getItem('user')||'{}').userId || ''
const list = ref([]), canDelete = ref(false), refreshing = ref(false)
const showTb = ref(false), tbContent = ref(''), showRp = ref(false), rpContent = ref(''), rpSaving = ref(false), rpTopicId = ref(0)
async function viewTaskbook(topicId) {
  try { const r = await api.get('/student/get-taskbook-content?topicId='+topicId); if (r.code===0) { tbContent.value = r.content; showTb.value = true } else showToast('暂无任务书内容') } catch(e) { showToast('加载失败') }
}
function openReport(item) { rpTopicId.value = item.id; rpContent.value = ''; showRp.value = true }
async function submitReport() {
  if (!rpContent.value.trim()) { showToast('请输入报告内容'); return }
  rpSaving.value = true
  try { const r = await api.post('/student/submit-report-text', { topicId: rpTopicId.value, content: rpContent.value }); showToast({ message: r.msg, icon: r.code===0?'success':'fail' }); if (r.code===0) { showRp.value = false; load() } } catch(e) { showToast('提交失败') }
  rpSaving.value = false
}
async function load() {
  try { const r = await api.get('/student/my-result'); list.value = r.list || []; canDelete.value = r.canDelete
    for (const item of list.value) {
      if (item.status === 2) {
        try { const rr = await api.get('/student/my-report?topicId='+item.id); if (rr.code===0) { item.reportStatus = rr.status; item.reportComment = rr.comment } else { item.reportStatus = -1 } } catch(e) { item.reportStatus = -1 }
      }
    }
  } catch (e) {}
  refreshing.value = false
}
async function doDel() {
  try { await showConfirmDialog({ title: '确认删除', message: '删除后不可恢复' }) } catch { return }
  try { await api.get('/student/my-result?action=delete'); showToast({ message: '已删除', icon: 'success' }); load() } catch (e) {}
}
onMounted(load)
onActivated(load)
</script>

<style scoped>
.empty-wrap { display: flex; justify-content: center; padding-top: 60px }
.empty-card { text-align: center; background: #fff; border-radius: 20px; padding: 40px 30px; margin: 0 16px; box-shadow: 0 4px 20px rgba(0,0,0,.04); max-width: 340px; width: 100% }
.empty-icon { font-size: 56px; margin-bottom: 16px }
.empty-card h3 { font-size: 18px; color: #333; margin: 0 0 8px }
.empty-card p { font-size: 14px; color: #999; margin: 0 0 20px }

.result-wrap { padding: 10px 14px }
.result-card { background: #fff; border-radius: 16px; padding: 16px; margin-bottom: 14px; box-shadow: 0 2px 12px rgba(0,0,0,.05); overflow: hidden }

.timeline { display: flex; align-items: center; justify-content: center; padding: 12px 8px; margin: -16px -16px 16px; background: linear-gradient(180deg,#f8f9fb,#fff); gap: 0 }
.tl-step { display: flex; flex-direction: column; align-items: center; gap: 4px; z-index: 1 }
.tl-dot { width: 28px; height: 28px; border-radius: 50%; background: #e0e0e0; color: #999; display: flex; align-items: center; justify-content: center; font-size: 12px; font-weight: 700 }
.tl-label { font-size: 11px; color: #999 }
.tl-step.done .tl-dot { background: #4CAF50; color: #fff }
.tl-step.done .tl-label { color: #2E7D32; font-weight: 600 }
.tl-step.fail .tl-dot { background: #C62828; color: #fff }
.tl-step.fail .tl-label { color: #C62828; font-weight: 600 }
.tl-line { flex: 1; height: 2px; background: #e0e0e0; margin: 0 4px; margin-bottom: 16px }
.tl-line.active { background: #4CAF50 }

.topic-name { font-size: 18px; font-weight: 700; color: #333; margin: 0 0 14px; line-height: 1.4 }

.teacher-card { display: flex; gap: 12px; padding: 12px; background: #f8f9fb; border-radius: 12px; margin-bottom: 12px; align-items: center }
.teacher-avatar { width: 40px; height: 40px; border-radius: 50%; background: #E3F2FD; display: flex; align-items: center; justify-content: center; font-size: 20px; flex-shrink: 0 }
.teacher-name { font-size: 15px; font-weight: 600; color: #333 }
.teacher-title { font-size: 12px; color: #1E88E5; font-weight: 400; margin-left: 6px }
.teacher-contact { display: flex; flex-wrap: wrap; gap: 8px; margin-top: 4px; font-size: 12px; color: #888 }

.detail-row { display: flex; padding: 10px 0; border-bottom: 1px solid #f5f5f5 }
.detail-label { width: 60px; font-size: 13px; color: #999; flex-shrink: 0 }
.detail-value { font-size: 14px; color: #555; flex: 1 }
.detail-link { font-size: 14px; color: #1E88E5; cursor: pointer; font-weight: 500 }

.report-section { margin-top: 12px; padding-top: 12px; border-top: 1px solid #f0f0f0 }
.report-header { font-size: 14px; font-weight: 600; color: #333; margin-bottom: 8px }
.report-empty { display: flex; align-items: center; justify-content: space-between; padding: 8px 0 }
.report-empty span { font-size: 13px; color: #999 }
.file-hint { font-size: 11px; color: #bbb; margin: 2px 0 0 }
.report-info { padding: 4px 0 }
.report-status-row { display: flex; align-items: center; gap: 8px }
.report-comment { margin-top: 8px; padding: 10px; background: #f8f9fb; border-radius: 8px; font-size: 13px; color: #555; line-height: 1.5 }
.rc-label { font-weight: 500; color: #333 }
.taskbook-card { display: flex; align-items: center; justify-content: space-between; padding: 12px 14px; background: linear-gradient(135deg, #E8F5E9, #F1F8E9); border-radius: 12px; margin-top: 10px; cursor: pointer; transition: transform .15s; border: 1px solid #C8E6C9 }
.taskbook-card:active { transform: scale(.98) }
.tb-left { display: flex; align-items: center; gap: 10px }
.tb-icon { font-size: 28px }
.tb-title { font-size: 14px; font-weight: 600; color: #2E7D32 }
.tb-hint { font-size: 11px; color: #66BB6A; margin-top: 2px }
.action-row { text-align: center; margin-top: 12px; padding-top: 8px; border-top: 1px solid #f5f5f5 }

.watermark { position: fixed; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none; z-index: 9999; opacity: .04; font-size: 18px; font-weight: 700; color: #000; user-select: none; padding: 40px; line-height: 3 }
</style>
