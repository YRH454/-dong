<template>
<div class="page">
  <div class="page-header">
    <h3>教师管理</h3>
    <van-button size="small" type="primary" round @click="showForm=true">+ 添加</van-button>
  </div>

  <van-empty v-if="!list.length" description="无教师数据" />
  <div class="t-card" v-for="t in list" :key="t.id">
    <div class="t-head">
      <span class="t-name">{{ t.xm }}</span>
      <van-tag size="small">{{ t.gh }}</van-tag>
    </div>
    <div class="t-info">职称: {{ t.zhicheng||'无' }} | 上限: {{ t.shangxian }} | 电话: {{ t.phone||'无' }}</div>
    <div class="t-actions">
      <van-button size="mini" type="warning" @click="resetPwd(t.gh)">重置密码</van-button>
      <van-button size="mini" type="danger" @click="delT(t.id)">删除</van-button>
    </div>
  </div>

  <!-- Add Form Popup -->
  <van-popup v-model:show="showForm" round position="bottom" :style="{maxHeight:'80vh'}">
    <div style="padding:16px">
      <h3 style="margin-bottom:12px">添加教师</h3>
      <van-field v-model="form.gh" label="工号" required />
      <van-field v-model="form.xm" label="姓名" required />
      <van-field v-model="form.zhicheng" label="职称" />
      <van-field v-model="form.shangxian" label="出题上限" type="number" />
      <van-field v-model="form.email" label="邮箱" />
      <van-field v-model="form.phone" label="电话" />
      <van-field v-model="form.qq" label="QQ" />
      <van-field v-model="form.bgdd" label="办公地点" />
      <div style="display:flex;gap:10px;margin-top:14px">
        <van-button block round @click="showForm=false">取消</van-button>
        <van-button block round type="primary" @click="addT" :loading="saving">保存</van-button>
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
const form=reactive({gh:'',xm:'',zhicheng:'',shangxian:'10',email:'',phone:'',qq:'',bgdd:''})
onMounted(async()=>{try{const r=await api.get('/admin/teacher-list');list.value=r.list||[]}catch(e){}})
async function addT(){
  if(!form.gh||!form.xm){showToast('工号和姓名必填');return}
  saving.value=true
  try{await api.post('/admin/add-teacher',{...form});showToast({message:'添加成功',icon:'success'});showForm.value=false;const r=await api.get('/admin/teacher-list');list.value=r.list||[]}catch(e){}
  saving.value=false
}
async function resetPwd(gh){try{await showConfirmDialog({title:'确认',message:'重置'+gh+'密码为工号？'})}catch{return};try{await api.post('/admin/reset-teacher-pwd',{gh});showToast('已重置')}catch(e){}}
async function delT(id){try{await showConfirmDialog({title:'确认删除',message:'删除该教师？'})}catch{return};try{await api.post('/admin/delete-teacher',{id});list.value=list.value.filter(t=>t.id!==id);showToast('已删除')}catch(e){}}
</script>

<style scoped>
.page{padding:12px 14px}
.page-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px}
.page-header h3{font-size:18px;color:#E65100}
.t-card{background:#fff;border-radius:12px;padding:14px;margin-bottom:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.t-head{display:flex;align-items:center;gap:8px;margin-bottom:6px}
.t-name{font-size:16px;font-weight:600}
.t-info{font-size:13px;color:#888;margin-bottom:8px}
.t-actions{display:flex;gap:8px;justify-content:flex-end}
</style>
