<template>
<div>
  <h3 style="color:#2E7D32;margin-bottom:14px">&#128273; 修改密码</h3>
  <div class="card">
    <van-field v-model="p1" type="password" label="新密码" placeholder="请输入新密码" />
    <van-field v-model="p2" type="password" label="确认密码" placeholder="请再次输入" />
    <van-button type="primary" block round @click="save" :loading="saving" style="margin-top:16px">确认修改</van-button>
  </div>
</div>
</template>

<script setup>
import { ref } from 'vue'
import { showToast } from 'vant'
import api from '../../api'
const p1 = ref(''), p2 = ref(''), saving = ref(false)
async function save() {
  if (!p1.value || !p2.value) { showToast('密码不能为空'); return }
  if (p1.value !== p2.value) { showToast('两次密码不一致'); return }
  saving.value = true
  try { await api.post('/student/change-password', { newPwd: p1.value }); showToast('修改成功'); p1.value = p2.value = '' } catch (e) {}
  saving.value = false
}
</script>
<style scoped>.card{background:#fff;border-radius:12px;padding:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}</style>
