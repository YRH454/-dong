<template>
<div class="page">
  <van-nav-bar title="通知公告" left-arrow @click-left="$router.back()" fixed placeholder />

  <!-- Publish Form -->
  <div class="pub-card">
    <h3 class="pub-title">{{ editing ? '编辑公告' : '发布新公告' }}</h3>
    <van-field v-model="form.title" placeholder="公告标题" maxlength="50" />
    <van-field v-model="form.content" type="textarea" rows="4" placeholder="公告内容..." maxlength="500" show-word-limit />
    <div class="pub-actions">
      <van-checkbox v-model="form.isTop">置顶</van-checkbox>
      <div style="display:flex;gap:8px">
        <van-button v-if="editing" size="small" plain @click="cancelEdit">取消</van-button>
        <van-button size="small" type="primary" round @click="submit" :loading="submitting">{{ editing?'更新':'发布' }}</van-button>
      </div>
    </div>
  </div>

  <!-- Announcement List -->
  <div class="list-section">
    <h3 class="section-label">已发布公告</h3>
    <van-empty v-if="!list.length" description="暂无公告" />
    <div class="announce-card" v-for="item in list" :key="item.id" :class="{top:item.isTop}">
      <div class="card-head">
        <span class="card-badge" v-if="item.isTop">📌 置顶</span>
        <span class="card-time">{{ item.createTime }}</span>
      </div>
      <h4 class="card-title">{{ item.title }}</h4>
      <p class="card-body">{{ item.content }}</p>
      <div class="card-foot">
        <van-button size="mini" plain type="primary" @click="editItem(item)">编辑</van-button>
        <van-button size="mini" plain type="danger" @click="delItem(item.id)">删除</van-button>
      </div>
    </div>
  </div>
  <div style="height:20px"></div>
</div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'; import { showToast, showConfirmDialog } from 'vant'; import api from '../../api'
const list=ref([]),submitting=ref(false),editing=ref(false),editId=ref(0)
const form=reactive({title:'',content:'',isTop:false})
async function load(){try{const r=await api.get('/admin/announcements');if(r.code===0)list.value=r.list}catch(e){}}
async function submit(){
  if(!form.title||!form.content){showToast('标题和内容不能为空');return}
  submitting.value=true
  try{
    if(editing.value){
      // For simplicity: delete old + create new
      await api.delete('/admin/announcement/'+editId.value)
    }
    await api.post('/admin/announcement',{title:form.title,content:form.content,isTop:form.isTop?'1':'0'})
    showToast({message:editing.value?'已更新':'发布成功',icon:'success'})
    form.title='';form.content='';form.isTop=false;editing.value=false;load()
  }catch(e){showToast('操作失败')}
  submitting.value=false
}
function editItem(item){form.title=item.title;form.content=item.content;form.isTop=item.isTop===1;editing.value=true;editId.value=item.id;window.scrollTo(0,0)}
function cancelEdit(){form.title='';form.content='';form.isTop=false;editing.value=false}
async function delItem(id){try{await showConfirmDialog({title:'确认删除',message:'删除该公告？'})}catch{return};try{await api.delete('/admin/announcement/'+id);showToast('已删除');load()}catch(e){}}
onMounted(load)
</script>

<style scoped>
.page{min-height:100vh;background:linear-gradient(180deg,#fdf7f3,#fef9f5);padding:0 14px}
.pub-card{background:#fff;border-radius:16px;padding:16px;margin:12px 0;box-shadow:0 2px 12px rgba(0,0,0,.04)}
.pub-title{font-size:16px;font-weight:700;color:#333;margin-bottom:12px}
.pub-actions{display:flex;justify-content:space-between;align-items:center;margin-top:12px}
.section-label{font-size:15px;font-weight:700;color:#666;margin:16px 0 10px}
.announce-card{background:#fff;border-radius:14px;padding:16px;margin-bottom:10px;box-shadow:0 1px 6px rgba(0,0,0,.03);border-left:3px solid transparent}
.announce-card.top{border-left-color:#FB8C00;background:linear-gradient(90deg,#FFF8E1 0%,#fff 20%)}
.card-head{display:flex;justify-content:space-between;align-items:center;margin-bottom:8px}
.card-badge{font-size:11px;color:#E65100;font-weight:600}
.card-time{font-size:11px;color:#bbb}
.card-title{font-size:16px;font-weight:700;color:#333;margin-bottom:6px}
.card-body{font-size:14px;color:#666;line-height:1.7;white-space:pre-wrap}
.card-foot{display:flex;gap:8px;margin-top:12px;justify-content:flex-end}
</style>
