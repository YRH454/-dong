<template>
<div class="login-page" :class="bgClass">
  <div class="login-card">
    <div class="card-top">
      <div class="logo">{{ role === 'root' ? '&#128272;' : '&#128218;' }}</div>
      <h2>选题系统</h2>
      <p class="sub">{{ role === 'root' ? '超级管理员' : '毕业设计选题系统' }}</p>
    </div>
    <div class="role-tabs" v-if="role !== 'root'">
      <span v-for="r in roles" :key="r.val" class="tab" :class="{active:role===r.val}" @click="role=r.val">{{ r.label }}</span>
    </div>
    <div class="card-form">
      <van-field v-model="userid" label="用户名" placeholder="请输入用户名" />
      <van-field v-model="userpwd" label="密码" type="password" placeholder="请输入密码" />
      <van-field v-if="role !== 'root'" v-model="dp" readonly is-link label="部门" placeholder="请选择部门" @click="showDp=true" />
      <van-button type="primary" block round :loading="loading" @click="doLogin" style="margin-top:20px;height:48px">登 录</van-button>
      <p class="err">{{ errMsg }}</p>
    </div>
    <div class="card-foot">
      <a v-if="role==='root'" @click="role='student'">&#8592; 普通登录</a>
      <a v-else @click="role='root'">超级管理员 &#8594;</a>
    </div>
  </div>
  <van-popup v-model:show="showDp" round position="bottom">
    <van-picker :columns="dpList" @confirm="onDpConfirm" @cancel="showDp=false" />
  </van-popup>
</div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import api from '../api'

const router = useRouter()
const userid = ref(''), userpwd = ref(''), dp = ref(''), role = ref('student')
const errMsg = ref(''), loading = ref(false), showDp = ref(false), dpList = ref([])

const roles = [{val:'student',label:'&#127891; 学生'},{val:'teacher',label:'&#127919; 教师'},{val:'admin',label:'&#9881; 管理员'}]
const bgClass = computed(() => ({ student: 'bg-green', teacher: 'bg-blue', admin: 'bg-orange', root: 'bg-red' })[role.value])

onMounted(async () => {
  try { const res = await api.get('/departments'); dpList.value = res.map(d => ({ text: d.label, value: d.value })) } catch (e) {}
})

async function doLogin() {
  if (!userid.value || !userpwd.value) { errMsg.value = '用户名和密码不能为空'; return }
  if (role.value !== 'root' && !dp.value) { errMsg.value = '请选择部门'; return }
  loading.value = true; errMsg.value = ''
  try {
    const res = await api.post('/login', { userid: userid.value, userpwd: userpwd.value, sf: role.value, dp: dp.value })
    if (res.code === 0) {
      localStorage.setItem('token', res.token)
      localStorage.setItem('role', res.role)
      localStorage.setItem('user', JSON.stringify(res))
      showToast('登录成功')
      const to = { student: '/student', teacher: '/teacher', admin: '/admin', root: '/admin' }
      setTimeout(() => router.push(to[res.role] || '/login'), 500)
    } else { errMsg.value = res.msg }
  } catch (e) { errMsg.value = '登录失败，请检查网络' }
  loading.value = false
}
function onDpConfirm({ selectedOptions }) {
  dp.value = selectedOptions[0].value
  showDp.value = false
}
</script>

<style scoped>
.login-page{min-height:100vh;display:flex;align-items:center;justify-content:center;padding:20px}
.bg-green{background:linear-gradient(160deg,#E8F5E9,#C8E6C9,#A5D6A7)}
.bg-blue{background:linear-gradient(160deg,#E3F2FD,#BBDEFB,#90CAF9)}
.bg-orange{background:linear-gradient(160deg,#FFF3E0,#FFE0B2,#FFCC80)}
.bg-red{background:linear-gradient(160deg,#FFEBEE,#FFCDD2,#EF9A9A)}
.login-card{width:100%;max-width:400px;background:#fff;border-radius:16px;box-shadow:0 6px 30px rgba(0,0,0,.12);overflow:hidden}
.card-top{text-align:center;padding:28px 24px 16px}
.card-top .logo{font-size:36px;margin-bottom:4px}
.card-top h2{font-size:22px;color:#333}
.card-top .sub{font-size:13px;color:#999;margin-top:4px}
.role-tabs{display:flex;gap:4px;padding:0 16px 12px;justify-content:center}
.role-tabs .tab{padding:8px 16px;border-radius:20px;font-size:13px;cursor:pointer;border:1.5px solid #ddd;background:#fff}
.role-tabs .tab.active{border-color:#4CAF50;background:#E8F5E9;color:#2E7D32;font-weight:600}
.card-form{padding:0 16px 20px}
.err{color:#C62828;text-align:center;min-height:24px;font-size:14px;margin-top:8px}
.card-foot{text-align:center;padding:12px;border-top:1px solid #f0f0f0}
.card-foot a{color:#1565C0;font-size:13px;cursor:pointer}
</style>
