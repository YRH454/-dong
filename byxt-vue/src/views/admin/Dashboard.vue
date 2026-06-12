<template>
<van-pull-refresh v-model="refreshing" @refresh="load">
  <div class="stat-grid">
    <div class="stat" v-for="s in stats" :key="s.label">
      <b>{{ s.value }}</b><span>{{ s.label }}</span>
    </div>
  </div>
  <div class="zt-panel">
    <div class="zt-badge" :class="zt===1?'on':'off'">{{ zt===1 ? '🟢 开放选题中' : '🔴 选题已关闭' }}</div>
    <van-button size="small" :type="zt===1?'danger':'success'" round @click="toggleZt">{{ zt===1 ? '关闭选题' : '开放选题' }}</van-button>
  </div>
</van-pull-refresh>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { showToast } from 'vant'; import api from '../../api'
const refreshing=ref(false),zt=ref(0)
const stats=reactive([
  {label:'学生总数',value:0},{label:'教师总数',value:0},{label:'题目总数',value:0},{label:'已选题目',value:0}
])
async function load(){try{const r=await api.get('/admin/stats');stats[0].value=r.studentCount;stats[1].value=r.teacherCount;stats[2].value=r.topicCount;stats[3].value=r.selectedCount;zt.value=r.zt}catch(e){}refreshing.value=false}
async function toggleZt(){const n=zt.value===1?0:1;try{await api.post('/admin/toggle-zt',{zt:n});zt.value=n;showToast({message:'状态已更新',icon:'success'})}catch(e){}}
onMounted(load)
</script>
<style scoped>
.stat-grid{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin:12px 14px}
.stat{background:#fff;border-radius:12px;padding:18px;text-align:center;box-shadow:0 1px 4px rgba(0,0,0,.04);border-top:3px solid #FB8C00}
.stat b{display:block;font-size:28px;color:#E65100}.stat span{font-size:12px;color:#999;margin-top:4px}
.zt-panel{background:#fff;margin:0 14px;border-radius:12px;padding:20px;text-align:center;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.zt-badge{display:inline-block;padding:10px 24px;border-radius:24px;font-size:16px;font-weight:700;margin-bottom:14px}
.zt-badge.on{background:#E8F5E9;color:#2E7D32}.zt-badge.off{background:#FFEBEE;color:#C62828}
</style>
