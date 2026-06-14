<template>
<div class="page">
  <div class="page-header">
    <h3>学生管理</h3>
    <van-button size="small" type="primary" round @click="showForm=true">+ 添加</van-button>
  </div>

  <van-empty v-if="!list.length" description="无学生数据" />
  <div class="s-card" v-for="s in list" :key="s.id">
    <div class="s-head">
      <span class="s-name">{{ s.xm }}</span>
      <van-tag size="small">{{ s.xh }}</van-tag>
    </div>
    <div class="s-info">{{ s.zy||'' }} {{ s.bj||'' }} | {{ s.phone||'' }} | {{ s.email||'' }}</div>
    <div class="s-actions">
      <van-button size="mini" plain type="primary" @click="resetPwd(s)">重置密码</van-button>
      <van-button size="mini" type="danger" @click="delS(s.id)">删除</van-button>
    </div>
  </div>

  <!-- Add Form -->
  <van-popup v-model:show="showForm" round position="bottom" :style="{maxHeight:'80vh'}">
    <div style="padding:16px">
      <h3 style="margin-bottom:12px">添加学生</h3>
      <van-field v-model="form.xh" label="学号" required />
      <van-field v-model="form.xm" label="姓名" required />
      <van-field v-model="form.zy" label="专业" />
      <van-field v-model="form.bj" label="班级" />
      <van-field v-model="form.email" label="邮箱" />
      <van-field v-model="form.phone" label="电话" />
      <van-field v-model="form.qq" label="QQ" />
      <div style="display:flex;gap:10px;margin-top:14px">
        <van-button block round @click="showForm=false">取消</van-button>
        <van-button block round type="primary" @click="addS" :loading="saving">保存</van-button>
      </div>
    </div>
  </van-popup>
</div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
const list=ref([]),showForm=ref(false),saving=ref(false)
const form=reactive({xh:'',xm:'',zy:'',bj:'',email:'',phone:'',qq:''})
onMounted(async()=>{try{const r=await api.get('/admin/student-list');list.value=r.list||[]}catch(e){}})
async function addS(){
  if(!form.xh||!form.xm){showToast('学号和姓名必填');return}
  saving.value=true
  try{await api.post('/admin/add-student',{...form});showToast({message:'添加成功',icon:'success'});showForm.value=false;const r=await api.get('/admin/student-list');list.value=r.list||[]}catch(e){}
  saving.value=false
}
async function resetPwd(s){try{await showConfirmDialog({title:'重置密码',message:'重置学生 '+s.xm+' 的密码为 123456？'})}catch{return};try{const r=await api.post('/admin/reset-student-pwd',{xh:s.xh});showToast(r.msg)}catch(e){}}
async function delS(id){try{await showConfirmDialog({title:'确认删除',message:'删除该学生？'})}catch{return};try{await api.post('/admin/delete-student',{id});list.value=list.value.filter(s=>s.id!==id);showToast('已删除')}catch(e){}}
</script>

<style scoped>
.page{padding:12px 14px}
.page-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px}
.page-header h3{font-size:18px;color:#E65100}
.s-card{background:#fff;border-radius:12px;padding:14px;margin-bottom:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.s-head{display:flex;align-items:center;gap:8px;margin-bottom:6px}
.s-name{font-size:16px;font-weight:600}
.s-info{font-size:13px;color:#888;margin-bottom:8px}
.s-actions{display:flex;gap:8px;justify-content:flex-end}
</style>
