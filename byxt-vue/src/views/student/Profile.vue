<template>
<div>
  <h3 style="color:#2E7D32;margin-bottom:14px">&#128100; 个人信息</h3>
  <div class="card">
    <van-field v-model="form.xh" label="学号" readonly />
    <van-field v-model="form.xm" label="姓名" required />
    <van-field v-model="form.zy" label="专业" />
    <van-field v-model="form.bj" label="班级" />
    <van-field v-model="form.email" label="邮箱" />
    <van-field v-model="form.phone" label="电话" required />
    <van-field v-model="form.qq" label="QQ" required />
    <van-button type="primary" block round @click="save" :loading="saving" style="margin-top:16px">保存</van-button>
  </div>
</div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { showToast } from 'vant'
import api from '../../api'
const form = reactive({ xh:'',xm:'',zy:'',bj:'',email:'',phone:'',qq:'' }), saving = ref(false)
async function load() {
  try { const r = await api.get('/student/profile'); Object.assign(form, r.profile) } catch (e) {}
}
async function save() {
  if (!form.xm || !form.phone || !form.qq) { showToast('姓名、电话、QQ不能为空'); return }
  saving.value = true
  try { await api.post('/student/update-profile', {...form}); showToast('保存成功') } catch (e) {}
  saving.value = false
}
onMounted(load)
</script>
<style scoped>.card{background:#fff;border-radius:12px;padding:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}</style>
