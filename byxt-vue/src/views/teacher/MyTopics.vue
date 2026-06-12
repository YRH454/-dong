<template>
<van-pull-refresh v-model="refreshing" @refresh="load">
  <div style="padding:8px 14px;display:flex;justify-content:space-between;align-items:center">
    <span style="font-size:13px;color:#888">已出 {{ list.length }} 题，已选 {{ selCount }} 人</span>
    <van-button size="small" plain type="primary" @click="exportMy" :loading="exp">📥 导出</van-button>
  </div>
  <van-empty v-if="!refreshing && !list.length" image="records" description="暂无出题记录" />
  <van-collapse v-model="expand" accordion>
    <van-collapse-item v-for="t in list" :key="t.id" :title="t.tm" :name="t.id">
      <van-cell title="状态" :value="t.xh ? `已被 ${t.sxm} 选择` : '待选'" />
      <van-cell title="备注" :value="t.bz||'无'" />
      <van-field v-model="edit.xh" label="学生学号" placeholder="分配学生" />
      <van-button size="small" plain type="primary" @click="checkStu" style="margin:4px 8px">检测学号</van-button>
      <van-field v-model="edit.sxm" label="姓名" />
      <van-field v-model="edit.zy" label="专业" />
      <van-field v-model="edit.bj" label="班级" />
      <div style="display:flex;gap:8px;padding:8px">
        <van-button size="small" type="primary" @click="updateTopic(t.id)">更新</van-button>
        <van-button size="small" type="danger" @click="delTopic(t.id)">删除</van-button>
      </div>
    </van-collapse-item>
  </van-collapse>
</van-pull-refresh>
</template>

<script setup>
import { ref, onMounted } from 'vue'; import { showToast, showConfirmDialog } from 'vant'; import api from '../../api'
const list=ref([]),selCount=ref(0),expand=ref(0),refreshing=ref(false),exp=ref(false)
async function exportMy(){
  exp.value=true
  try{
    let csv='﻿工号,教师姓名,题目,备注,学号,学生姓名,专业,班级\n'
    list.value.forEach(t=>{csv+=`${t.gh},${t.txm},${t.tm},${t.bz||''},${t.xh||''},${t.sxm||''},${t.zy||''},${t.bj||''}\n`})
    const blob=new Blob([csv],{type:'text/csv;charset=utf-8'})
    const a=document.createElement('a');a.href=URL.createObjectURL(blob);a.download='选题结果.csv';a.click()
    showToast({message:'导出成功',icon:'success'})
  }catch(e){}
  exp.value=false
}
const edit=ref({xh:'',sxm:'',zy:'',bj:''})
async function load(){try{const r=await api.get('/teacher/my-topics');list.value=r.list||[];selCount.value=r.selectedCount}catch(e){}refreshing.value=false}
async function checkStu(){try{const r=await api.post('/teacher/check-student',{xh:edit.value.xh});if(r.code===0){edit.value.sxm=r.xm;edit.value.zy=r.zy;edit.value.bj=r.bj}else showToast(r.msg)}catch(e){}}
async function updateTopic(id){try{await api.post('/teacher/update-topic',{id,...edit.value});showToast({message:'更新成功',icon:'success'});load()}catch(e){}}
async function delTopic(id){try{await showConfirmDialog({title:'确认删除',message:'删除该题目？'})}catch{return};try{await api.post('/teacher/delete-topic',{id});showToast('已删除');load()}catch(e){}}
onMounted(load)
</script>
