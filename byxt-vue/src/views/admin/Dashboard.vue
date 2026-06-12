<template>
<div>
  <h3 style="color:#E65100;margin-bottom:12px">&#128200; 数据面板</h3>
  <div class="stat-row">
    <div class="stat"><b>{{ stats.studentCount }}</b><small>学生</small></div>
    <div class="stat"><b>{{ stats.teacherCount }}</b><small>教师</small></div>
    <div class="stat"><b>{{ stats.topicCount }}</b><small>题目</small></div>
    <div class="stat"><b>{{ stats.selectedCount }}</b><small>已选</small></div>
  </div>
  <div class="zt-card">
    <div :class="['zt-badge', stats.zt===1?'on':'off']">{{ stats.zt===1 ? '&#128994; 开放选题中' : '&#128308; 选题已关闭' }}</div>
    <van-button size="small" :type="stats.zt===1?'danger':'success'" round @click="toggleZt">
      {{ stats.zt===1 ? '关闭选题' : '开放选题' }}
    </van-button>
  </div>
</div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { showToast } from 'vant'
import api from '../../api'
const stats = reactive({ studentCount:0,teacherCount:0,topicCount:0,selectedCount:0,zt:0 })
async function load() { try { const r = await api.get('/admin/stats'); Object.assign(stats, r) } catch (e) {} }
async function toggleZt() {
  const nzt = stats.zt === 1 ? 0 : 1
  try { await api.post('/admin/toggle-zt', { zt: nzt }); stats.zt = nzt; showToast('状态已更新') } catch (e) {}
}
onMounted(load)
</script>
<style scoped>
.stat-row{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin-bottom:14px}
.stat{background:#fff;border-radius:10px;padding:16px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,.04);border-top:3px solid #FB8C00}
.stat b{display:block;font-size:26px;color:#E65100}
.stat small{color:#888;font-size:12px;margin-top:2px}
.zt-card{background:#fff;border-radius:10px;padding:16px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,.04)}
.zt-badge{display:inline-block;padding:8px 20px;border-radius:20px;font-size:16px;font-weight:700;margin-bottom:12px}
.zt-badge.on{background:#E8F5E9;color:#2E7D32}
.zt-badge.off{background:#FFEBEE;color:#C62828}
</style>
