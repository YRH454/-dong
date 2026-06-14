<template>
<div style="padding-top:8px">
  <van-cell-group inset>
    <van-field v-model="p1" type="password" label="新密码" placeholder="请输入新密码" />
    <van-field v-model="p2" type="password" label="确认密码" placeholder="请再次输入" />
  </van-cell-group>
  <div style="margin:16px">
    <van-button round block type="primary" :loading="saving" @click="save">确认修改</van-button>
  </div>
</div>
</template>
<script setup>
import { ref } from 'vue'; import { showToast } from 'vant'; import api from '../../api'
const p1=ref(''),p2=ref(''),saving=ref(false)
async function save(){
  if(!p1.value||!p2.value){showToast('密码不能为空');return}
  if(p1.value!==p2.value){showToast('两次密码不一致');return}
  saving.value=true
  try{await api.post('/student/change-password',{newPwd:p1.value});showToast({message:'修改成功',icon:'success'});p1.value=p2.value=''}catch(e){}
  saving.value=false
}
</script>
