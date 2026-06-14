<template>
<div>
  <h3 style="color:#1565C0;margin-bottom:14px">&#128100; 个人信息</h3>
  <div class="card">
    <van-field v-model="form.gh" label="工号" readonly />
    <van-field v-model="form.xm" label="姓名" required />
    <van-field v-model="form.zhicheng" label="职称" />
    <van-field v-model="form.email" label="邮箱" />
    <van-field v-model="form.phone" label="电话" required />
    <van-field v-model="form.qq" label="QQ" />
    <van-field v-model="form.bgdd" label="办公地点" />
    <van-button type="primary" block round @click="save" :loading="saving" style="margin-top:16px">保存</van-button>
  </div>
</div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { showToast } from 'vant'
import api from '../../api'
const form = reactive({ gh:'',xm:'',zhicheng:'',email:'',phone:'',qq:'',bgdd:'' }), saving = ref(false)
onMounted(async () => { try { const r = await api.get('/teacher/profile'); Object.assign(form, r.profile) } catch (e) {} })
async function save() {
  if (!form.xm || !form.phone) { showToast('姓名和电话不能为空'); return }
  saving.value = true
  try { await api.post('/teacher/update-profile', {...form}); showToast('保存成功') } catch (e) {}
  saving.value = false
}
</script>
<style scoped>.card{background:#fff;border-radius:12px;padding:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}</style>
