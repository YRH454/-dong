<template>
<van-pull-refresh v-model="refreshing" @refresh="load" class="dash-page">
  <!-- Hero Stats -->
  <div class="stat-hero">
    <div class="stat-hero-card" v-for="s in cards" :key="s.label" :style="{borderLeftColor:s.color}">
      <b :style="{color:s.color}">{{ s.value }}</b>
      <span>{{ s.label }}</span>
    </div>
  </div>

  <!-- ZT Control -->
  <div class="zt-panel">
    <div class="zt-status" :class="zt?'on':'off'">
      <span class="zt-dot"></span>
      <span>{{ zt ? '选题系统开放中' : '选题系统已关闭' }}</span>
    </div>
    <van-button size="small" :type="zt?'danger':'success'" round @click="toggleZt" class="zt-btn">
      {{ zt ? '关闭系统' : '开放系统' }}
    </van-button>
  </div>

  <!-- Teacher Progress -->
  <div class="section-card">
    <h4 class="section-title">各教师出题进度</h4>
    <div class="progress-list">
      <div class="progress-item" v-for="t in teacherStats" :key="t.name">
        <div class="progress-head">
          <span class="prog-name">{{ t.name }}</span>
          <span class="prog-num">{{ t.selected }}/{{ t.shangxian }}</span>
        </div>
        <div class="prog-track">
          <div class="prog-fill" :style="{width:(t.selected/t.shangxian*100)+'%',background:t.selected>=t.shangxian?'#4CAF50':t.selected>0?'#FB8C00':'#e0e0e0'}"></div>
        </div>
      </div>
      <div v-if="!teacherStats.length" class="empty-inline">暂无教师数据</div>
    </div>
  </div>

  <!-- Quick Export -->
  <van-button block round :type="'primary'" @click="exportTopics" :loading="exporting" class="export-btn">
    📥 导出选题汇总表
  </van-button>
  <div style="height:20px"></div>
</van-pull-refresh>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { showToast } from 'vant'; import api from '../../api'
const refreshing=ref(false),zt=ref(0),teacherStats=ref([]),exporting=ref(false)
const cards=reactive([
  {label:'学生总数',value:0,color:'#4CAF50'},{label:'指导教师',value:0,color:'#1E88E5'},
  {label:'题目总数',value:0,color:'#FB8C00'},{label:'已选题目',value:0,color:'#C62828'}
])
async function load(){try{const r=await api.get('/admin/stats');cards[0].value=r.studentCount;cards[1].value=r.teacherCount;cards[2].value=r.topicCount;cards[3].value=r.selectedCount;zt.value=r.zt;teacherStats.value=r.teacherStats||[]}catch(e){}refreshing.value=false}
async function toggleZt(){const n=zt.value?0:1;try{await api.post('/admin/toggle-zt',{zt:n});zt.value=n;showToast({message:'已更新',icon:'success'})}catch(e){}}
async function exportTopics(){
  exporting.value=true
  try{const r=await api.get('/admin/export-topics');if(r.code===0&&r.list.length){let csv='﻿工号,教师姓名,题目,备注,学生学号,学生姓名,专业,班级\n';r.list.forEach(t=>{csv+=`${t.gh},${t.txm},${t.tm},${t.bz||''},${t.xh||''},${t.sxm||''},${t.zy||''},${t.bj||''}\n`});const blob=new Blob([csv],{type:'text/csv;charset=utf-8'});const a=document.createElement('a');a.href=URL.createObjectURL(blob);a.download='选题汇总.csv';a.click();showToast({message:'导出成功',icon:'success'})}else showToast('暂无数据')}catch(e){showToast('导出失败')}
  exporting.value=false
}
onMounted(load)
</script>

<style scoped>
.dash-page { min-height: 100vh; background: linear-gradient(180deg, #fdf7f3 0%, #fef9f5 50%, #fdf7f3 100%); padding: 12px 14px }
.stat-hero { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 14px }
.stat-hero-card { background: #fff; border-radius: 14px; padding: 18px 14px; box-shadow: 0 2px 12px rgba(0,0,0,.04); border-left: 4px solid #ccc; transition: transform .15s }
.stat-hero-card:active { transform: scale(.98) }
.stat-hero-card b { display: block; font-size: 30px; font-weight: 800; line-height: 1.1 }
.stat-hero-card span { font-size: 12px; color: #999; margin-top: 4px; display: block }
.zt-panel { background: #fff; border-radius: 14px; padding: 16px 20px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; box-shadow: 0 2px 12px rgba(0,0,0,.04) }
.zt-status { display: flex; align-items: center; gap: 8px; font-weight: 600; font-size: 15px }
.zt-dot { width: 10px; height: 10px; border-radius: 50%; display: inline-block }
.zt-status.on .zt-dot { background: #4CAF50; box-shadow: 0 0 8px rgba(76,175,80,.4) }
.zt-status.off .zt-dot { background: #C62828; box-shadow: 0 0 8px rgba(198,40,40,.4) }
.zt-status.on { color: #2E7D32 } .zt-status.off { color: #C62828 }
.zt-btn { font-weight: 600 !important }
.section-card { background: #fff; border-radius: 14px; padding: 16px; margin-bottom: 14px; box-shadow: 0 2px 12px rgba(0,0,0,.04) }
.section-title { font-size: 15px; font-weight: 700; color: #333; margin-bottom: 12px; display: flex; align-items: center; gap: 6px }
.section-title::before { content: ''; display: inline-block; width: 4px; height: 16px; border-radius: 2px; background: #FB8C00 }
.progress-item { margin-bottom: 14px }
.progress-item:last-child { margin-bottom: 0 }
.progress-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; font-size: 14px }
.prog-name { font-weight: 500 } .prog-num { color: #999; font-size: 13px }
.prog-track { height: 8px; background: #f0f0f0; border-radius: 4px; overflow: hidden }
.prog-fill { height: 100%; border-radius: 4px; transition: width .6s cubic-bezier(.4,0,.2,1) }
.empty-inline { text-align: center; padding: 20px; color: #ccc; font-size: 14px }
.export-btn { margin-top: 4px; height: 46px; font-weight: 600; --van-button-primary-background: linear-gradient(135deg, #FB8C00, #FF9800); border: none; box-shadow: 0 4px 14px rgba(251,140,0,.2) }
</style>
