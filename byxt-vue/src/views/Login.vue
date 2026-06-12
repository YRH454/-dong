<template>
<van-loading v-if="initLoading" type="spinner" size="40" class="full-center" />
<div v-else class="login-page" :class="bgClass">
  <div class="login-card">
    <div class="card-top">
      <div class="logo-icon">{{ role === 'root' ? '🔐' : '📚' }}</div>
      <h2>选题系统</h2>
      <p class="sub">{{ role === 'root' ? '超级管理员入口' : '毕业设计选题系统' }}</p>
    </div>

    <!-- Role Tabs -->
    <div class="role-tabs" v-if="role !== 'root'">
      <div v-for="r in roles" :key="r.val" class="tab" :class="{active: role===r.val}" @click="switchRole(r.val)">
        <span class="tab-icon">{{ r.icon }}</span>
        <span>{{ r.label }}</span>
      </div>
    </div>

    <!-- Login Form -->
    <div class="card-form">
      <van-field v-model="userid" label="用户名" placeholder="请输入用户名" left-icon="user-o" clearable />
      <van-field v-model="userpwd" label="密码" type="password" placeholder="请输入密码" left-icon="lock" />
      <van-field v-if="role !== 'root'" v-model="dpName" readonly is-link label="部门" placeholder="请选择部门" left-icon="cluster-o" @click="showDp=true" />
      <van-button type="primary" block round :loading="loading" size="large" @click="doLogin" style="margin-top:20px">登 录</van-button>
      <p class="err" v-if="errMsg">{{ errMsg }}</p>
    </div>

    <!-- Footer -->
    <div class="card-foot">
      <a v-if="role==='root'" @click="switchRole('student')">← 返回普通登录</a>
      <a v-else @click="switchRole('root')">超级管理员入口 →</a>
    </div>
  </div>

  <!-- Department Picker -->
  <van-popup v-model:show="showDp" round position="bottom">
    <van-picker :columns="dpList" @confirm="onDpConfirm" @cancel="showDp=false" title="选择部门" />
  </van-popup>
</div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import api from '../api'

const router = useRouter()
const userid = ref(''), userpwd = ref(''), dp = ref(''), dpName = ref('')
const role = ref('student'), errMsg = ref(''), loading = ref(false), initLoading = ref(false)
const showDp = ref(false), dpList = ref([])

const roles = [
  { val: 'student', label: '学生', icon: '🎓' },
  { val: 'teacher', label: '教师', icon: '🎯' },
  { val: 'admin', label: '管理员', icon: '⚙️' }
]
const bgClass = computed(() =>
  ({ student: 'bg-green', teacher: 'bg-blue', admin: 'bg-orange', root: 'bg-red' })[role.value]
)

onMounted(async () => {
  initLoading.value = true
  try {
    const res = await api.get('/departments')
    dpList.value = res.map(d => ({ text: d.label, value: d.value }))
  } catch (e) { /* server may be starting up */ }
  initLoading.value = false
})

function switchRole(r) { role.value = r; dp.value = ''; dpName.value = ''; errMsg.value = '' }

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
      showToast({ message: '登录成功', icon: 'success', duration: 1000 })
      const to = { student: '/student', teacher: '/teacher', admin: '/admin', root: '/admin' }
      setTimeout(() => router.replace(to[res.role] || '/login'), 500)
    } else { errMsg.value = res.msg }
  } catch (e) { errMsg.value = '登录失败，请检查网络连接' }
  loading.value = false
}

function onDpConfirm({ selectedOptions }) {
  dp.value = selectedOptions[0].value
  dpName.value = selectedOptions[0].text
  showDp.value = false
}
</script>

<style scoped>
.full-center { display: flex; justify-content: center; align-items: center; min-height: 100vh }
.login-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 16px }
.bg-green { background: linear-gradient(160deg, #E8F5E9, #C8E6C9 40%, #A5D6A7) }
.bg-blue { background: linear-gradient(160deg, #E3F2FD, #BBDEFB 40%, #90CAF9) }
.bg-orange { background: linear-gradient(160deg, #FFF3E0, #FFE0B2 40%, #FFCC80) }
.bg-red { background: linear-gradient(160deg, #FFEBEE, #FFCDD2 40%, #EF9A9A) }
.login-card { width: 100%; max-width: 400px; background: #fff; border-radius: 18px; box-shadow: 0 8px 36px rgba(0,0,0,.12); overflow: hidden }
.card-top { text-align: center; padding: 32px 24px 12px }
.logo-icon { font-size: 42px; margin-bottom: 6px }
.card-top h2 { font-size: 24px; font-weight: 700; color: #333; letter-spacing: 2px }
.card-top .sub { font-size: 13px; color: #999; margin-top: 4px }
.role-tabs { display: flex; gap: 6px; padding: 0 16px 16px; justify-content: center }
.tab { display: flex; align-items: center; gap: 4px; padding: 9px 16px; border-radius: 22px; font-size: 14px; cursor: pointer; border: 1.5px solid #e0e0e0; background: #fff; transition: all .15s; user-select: none }
.tab .tab-icon { font-size: 16px }
.tab.active { border-color: #4CAF50; background: #E8F5E9; color: #2E7D32; font-weight: 600 }
.card-form { padding: 0 16px 20px }
:deep(.van-field) { border-radius: 10px; margin-bottom: 4px }
.err { color: #C62828; text-align: center; min-height: 22px; font-size: 14px; margin-top: 10px }
.card-foot { text-align: center; padding: 14px; border-top: 1px solid #f0f0f0; background: #fafafa }
.card-foot a { color: #1565C0; font-size: 13px; cursor: pointer; -webkit-tap-highlight-color: transparent }
</style>
