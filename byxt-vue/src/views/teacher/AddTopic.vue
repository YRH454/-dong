<template>
<van-pull-refresh v-model="refreshing" @refresh="loadInfo">
  <AnnounceBanner :adminId="adminId" />
  <div class="stat-row">
    <div class="stat"><b>{{ info.topicCount }}</b><span>已出</span></div>
    <div class="stat"><b>{{ info.remaining }}</b><span>剩余</span></div>
    <div class="stat"><b>{{ info.teacher?.shangxian||0 }}</b><span>上限</span></div>
  </div>
  <van-notice-bar v-if="info.remaining<=0" left-icon="warning-o" text="题目数量已达上限，无法继续出题" background="#FFF3E0" color="#E65100" />
  <van-form v-else @submit="submit">
    <van-cell-group inset>
      <van-field v-model="tm" label="题目名称" placeholder="请输入题目名称" required :rules="[{required:true,message:'请输入题目'}]" />
      <van-field v-model="bz" label="备注" placeholder="可选填写备注" type="textarea" rows="3" />
    </van-cell-group>
    <div style="margin:16px">
      <van-button round block type="primary" native-type="submit" :loading="saving">提交出题</van-button>
    </div>
  </van-form>
</van-pull-refresh>
</template>

<script setup>
import { ref, reactive, onMounted, onActivated } from 'vue'; import { showToast } from 'vant'; import api from '../../api'
import AnnounceBanner from '../../components/AnnounceBanner.vue'
const user = JSON.parse(localStorage.getItem('user') || '{}')
const adminId = user.adminId || ''
const tm=ref(''),bz=ref(''),saving=ref(false),refreshing=ref(false)
const info=reactive({topicCount:0,remaining:0,teacher:null})
async function loadInfo(){try{Object.assign(info,await api.get('/teacher/info'))}catch(e){}refreshing.value=false}
async function submit(){
  saving.value=true
  try{const r=await api.post('/teacher/add-topic',{tm:tm.value,bz:bz.value});showToast({message:r.msg,icon:r.code===0?'success':'fail'});if(r.code===0){tm.value=bz.value='';loadInfo()}}catch(e){}
  saving.value=false
}
onMounted(loadInfo)
onActivated(loadInfo)
</script>
<style scoped>
.stat-row{display:flex;gap:10px;margin:12px 14px}
.stat{flex:1;background:#fff;border-radius:12px;padding:14px;text-align:center;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.stat b{display:block;font-size:24px;color:#1E88E5}.stat span{font-size:12px;color:#999;margin-top:2px}
</style>
