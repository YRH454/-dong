<template>
<van-pull-refresh v-model="refreshing" @refresh="load">
  <!-- Stat Cards -->
  <div class="stat-grid">
    <div class="stat" v-for="s in cards" :key="s.label" :style="{borderTopColor:s.color}">
      <b :style="{color:s.color}">{{ s.value }}</b><span>{{ s.label }}</span>
    </div>
  </div>

  <!-- ZT Control -->
  <div class="zt-panel">
    <div :class="['zt-badge',zt?'on':'off']">{{ zt ? '🟢 选题开放中' : '🔴 选题已关闭' }}</div>
    <van-button size="small" :type="zt?'danger':'success'" round @click="toggleZt">
      {{ zt ? '关闭选题' : '开放选题' }}
    </van-button>
  </div>

  <!-- Teacher Stats Table -->
  <div class="card" style="margin:12px 14px;padding:16px">
    <h4 style="margin-bottom:12px;font-size:15px">📊 各教师出题/选题统计</h4>
    <div class="table-wrap">
    <table>
      <thead><tr><th>教师</th><th>上限</th><th>已出</th><th>已选</th><th>完成率</th></tr></thead>
      <tbody>
        <tr v-for="t in teacherStats" :key="t.name">
          <td>{{ t.name }}</td><td>{{ t.shangxian }}</td><td>{{ t.total }}</td><td>{{ t.selected }}</td>
          <td><div class="prog"><div class="prog-bar" :style="{width:(t.selected/t.shangxian*100)+'%',background:(t.selected>=t.shangxian?'#4CAF50':'#FB8C00')}"></div></div></td>
        </tr>
      </tbody>
    </table>
    </div>
  </div>

  <!-- Quick Export -->
  <div style="margin:12px 14px">
    <van-button block round type="primary" @click="exportTopics" :loading="exporting">📥 导出选题汇总表</van-button>
  </div>
</van-pull-refresh>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { showToast } from 'vant'; import api from '../../api'
const refreshing=ref(false),zt=ref(0),teacherStats=ref([]),exporting=ref(false)
const cards=reactive([
  {label:'学生总数',value:0,color:'#4CAF50'},{label:'教师总数',value:0,color:'#1E88E5'},
  {label:'题目总数',value:0,color:'#FB8C00'},{label:'已选题目',value:0,color:'#C62828'}
])
async function load(){try{const r=await api.get('/admin/stats');cards[0].value=r.studentCount;cards[1].value=r.teacherCount;cards[2].value=r.topicCount;cards[3].value=r.selectedCount;zt.value=r.zt;teacherStats.value=r.teacherStats||[]}catch(e){}refreshing.value=false}
async function toggleZt(){const n=zt.value?0:1;try{await api.post('/admin/toggle-zt',{zt:n});zt.value=n;showToast({message:'已更新',icon:'success'})}catch(e){}}
async function exportTopics(){
  exporting.value=true
  try{
    const r=await api.get('/admin/export-topics')
    if(r.code===0&&r.list.length){
      let csv='﻿工号,教师姓名,题目,备注,学生学号,学生姓名,专业,班级\n'
      r.list.forEach(t=>{csv+=`${t.gh},${t.txm},${t.tm},${t.bz||''},${t.xh||''},${t.sxm||''},${t.zy||''},${t.bj||''}\n`})
      const blob=new Blob([csv],{type:'text/csv;charset=utf-8'})
      const url=URL.createObjectURL(blob)
      const a=document.createElement('a');a.href=url;a.download='选题汇总.csv';a.click()
      showToast({message:'导出成功',icon:'success'})
    }else{showToast('暂无数据')}
  }catch(e){showToast('导出失败')}
  exporting.value=false
}
onMounted(load)
</script>

<style scoped>
.stat-grid{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin:12px 14px}
.stat{background:#fff;border-radius:12px;padding:18px;text-align:center;box-shadow:0 1px 4px rgba(0,0,0,.04);border-top:3px solid #ccc}
.stat b{display:block;font-size:28px}.stat span{font-size:12px;color:#999;margin-top:4px}
.zt-panel{background:#fff;margin:0 14px;border-radius:12px;padding:20px;text-align:center;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.zt-badge{display:inline-block;padding:10px 24px;border-radius:24px;font-size:16px;font-weight:700;margin-bottom:14px}
.zt-badge.on{background:#E8F5E9;color:#2E7D32}.zt-badge.off{background:#FFEBEE;color:#C62828}
.card{background:#fff;border-radius:12px;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.table-wrap{-webkit-overflow-scrolling:touch;overflow-x:auto}
table{width:100%;border-collapse:collapse;min-width:400px;font-size:13px}
thead th{background:#f5f5f5;padding:8px 6px;font-size:12px;border-bottom:2px solid #e0e0e0}
tbody td{padding:8px 6px;text-align:center;border-bottom:1px solid #f0f0f0}
.prog{height:8px;background:#eee;border-radius:4px;overflow:hidden;min-width:60px}
.prog-bar{height:100%;border-radius:4px;transition:width .3s}
</style>
