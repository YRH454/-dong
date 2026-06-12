<template>
<div class="login-shell" :class="themeClass">
  <!-- Decorative Elements -->
  <div class="deco-top"></div>
  <div class="deco-dots"></div>

  <!-- Logo & Brand -->
  <div class="brand-area">
    <div class="brand-icon-wrapper">
      <span class="brand-icon">{{ role==='root' ? '🔐' : '📖' }}</span>
    </div>
    <h1 class="brand-name">毕设选题系统</h1>
    <p class="brand-sub">{{ role==='root' ? '超级管理员入口' : 'Graduation Project Selection' }}</p>
  </div>

  <!-- Role Switcher -->
  <div class="role-bar" v-if="role !== 'root'">
    <div v-for="r in roles" :key="r.val"
         class="role-chip" :class="{active:role===r.val}"
         @click="switchRole(r.val)">
      <span class="chip-icon">{{ r.icon }}</span>
      <span class="chip-label">{{ r.label }}</span>
    </div>
  </div>

  <!-- Login Card -->
  <div class="login-card">
    <div class="card-inner">
      <van-field v-model="userid" label="账号" placeholder="学号/工号/用户名" :left-icon="role==='student'?'friends-o':role==='teacher'?'manager-o':'user-o'" clearable />
      <van-field v-model="userpwd" label="密码" type="password" placeholder="输入密码" left-icon="lock" />
      <van-field v-if="role!=='root'" v-model="dpName" readonly is-link label="部门" placeholder="选择所属部门" left-icon="cluster-o" @click="showDp=true" />

      <van-button type="primary" block round :loading="loading" size="large"
                  @click="doLogin"
                  class="login-btn"
                  :class="'btn-'+role">
        进入系统
      </van-button>
      <p class="err-msg" v-if="errMsg">{{ errMsg }}</p>
    </div>
  </div>

  <!-- Footer Link -->
  <div class="footer-link">
    <a v-if="role==='root'" @click="switchRole('student')">← 返回普通登录</a>
    <a v-else @click="switchRole('root')">超级管理员入口</a>
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
const role = ref('student'), errMsg = ref(''), loading = ref(false)
const showDp = ref(false), dpList = ref([])

const roles = [
  { val: 'student', label: '学生', icon: '🎓' },
  { val: 'teacher', label: '教师', icon: '🎯' },
  { val: 'admin', label: '管理', icon: '⚙️' }
]
const themeClass = computed(() => 'theme-' + role.value)

onMounted(async () => {
  try { const res = await api.get('/departments'); dpList.value = res.map(d => ({ text: d.label, value: d.value })) } catch (e) {}
})

function switchRole(r) { role.value = r; dp.value = ''; dpName.value = ''; errMsg.value = '' }

async function doLogin() {
  if (!userid.value || !userpwd.value) { errMsg.value = '请输入账号和密码'; return }
  if (role.value !== 'root' && !dp.value) { errMsg.value = '请选择所属部门'; return }
  loading.value = true; errMsg.value = ''
  try {
    const res = await api.post('/login', { userid: userid.value, userpwd: userpwd.value, sf: role.value, dp: dp.value })
    if (res.code === 0) {
      localStorage.setItem('token', res.token)
      localStorage.setItem('role', res.role)
      localStorage.setItem('user', JSON.stringify(res))
      const to = { student: '/student', teacher: '/teacher', admin: '/admin', root: '/admin' }
      router.replace(to[res.role] || '/login')
    } else { errMsg.value = res.msg }
  } catch (e) { errMsg.value = '网络连接失败' }
  loading.value = false
}
function onDpConfirm({ selectedOptions }) { dp.value = selectedOptions[0].value; dpName.value = selectedOptions[0].text; showDp.value = false }
</script>

<style scoped>
/* === BASE === */
.login-shell {
  min-height: 100vh; min-height: 100dvh;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  padding: 24px 20px; position: relative; overflow: hidden;
  transition: background .4s ease;
}
.theme-student { background: linear-gradient(160deg, #e8f5e9 0%, #c8e6c9 30%, #f1f8e9 60%, #e8f5e9 100%) }
.theme-teacher { background: linear-gradient(160deg, #e3f2fd 0%, #bbdefb 30%, #e8eaf6 60%, #e3f2fd 100%) }
.theme-admin  { background: linear-gradient(160deg, #fff3e0 0%, #ffe0b2 30%, #fef7e0 60%, #fff3e0 100%) }
.theme-root   { background: linear-gradient(160deg, #ffebee 0%, #ffcdd2 30%, #fce4ec 60%, #ffebee 100%) }

/* Decorative */
.deco-top {
  position: absolute; top: -60px; right: -60px;
  width: 200px; height: 200px; border-radius: 50%;
  opacity: .06; pointer-events: none;
}
.theme-student .deco-top { background: #2E7D32 }
.theme-teacher .deco-top { background: #1565C0 }
.theme-admin .deco-top  { background: #E65100 }
.theme-root .deco-top   { background: #C62828 }

.deco-dots {
  position: absolute; bottom: 40px; left: 20px;
  width:60px; height:60px;
  background-image: radial-gradient(circle, rgba(0,0,0,.04) 1px, transparent 1px);
  background-size: 8px 8px; pointer-events: none;
}

/* Brand */
.brand-area { text-align: center; margin-bottom: 20px; z-index: 1 }
.brand-icon-wrapper { margin-bottom: 8px }
.brand-icon { font-size: 44px; display: inline-block; animation: float 3s ease-in-out infinite }
@keyframes float { 0%,100% { transform: translateY(0) } 50% { transform: translateY(-6px) } }
.brand-name { font-size: 28px; font-weight: 800; letter-spacing: 3px; margin: 0;
  background: linear-gradient(135deg, #1B5E20, #2E7D32); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text }
.theme-teacher .brand-name { background: linear-gradient(135deg, #0D47A1, #1565C0); -webkit-background-clip: text; background-clip: text }
.theme-admin .brand-name  { background: linear-gradient(135deg, #BF360C, #E65100); -webkit-background-clip: text; background-clip: text }
.theme-root .brand-name   { background: linear-gradient(135deg, #B71C1C, #C62828); -webkit-background-clip: text; background-clip: text }
.brand-sub { font-size: 11px; color: #999; letter-spacing: 1px; text-transform: uppercase; margin-top: 4px }

/* Role Bar */
.role-bar { display: flex; gap: 6px; margin-bottom: 16px; z-index: 1; background: rgba(255,255,255,.5); backdrop-filter: blur(10px); border-radius: 28px; padding: 4px }
.role-chip { display: flex; align-items: center; gap: 4px; padding: 9px 16px; border-radius: 24px; font-size: 14px; cursor: pointer; transition: all .25s; user-select: none; font-weight: 500; color: #666 }
.role-chip .chip-icon { font-size: 16px }
.role-chip.active { background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,.08); font-weight: 600 }
.theme-student .role-chip.active { color: #2E7D32 }
.theme-teacher .role-chip.active { color: #1565C0 }
.theme-admin .role-chip.active  { color: #E65100 }

/* Card */
.login-card { width: 100%; max-width: 400px; z-index: 1 }
.card-inner { background: rgba(255,255,255,.85); backdrop-filter: blur(20px); border-radius: 20px; padding: 16px 12px 20px; box-shadow: 0 8px 32px rgba(0,0,0,.06), 0 1px 2px rgba(0,0,0,.04) }
:deep(.van-field) { border-radius: 12px; margin-bottom: 2px; --van-cell-background: rgba(255,255,255,.6) }

/* Button */
.login-btn { margin-top: 18px; height: 50px; font-size: 16px; font-weight: 700; letter-spacing: 4px; border: none !important; transition: all .2s }
.btn-student { background: linear-gradient(135deg, #2E7D32, #4CAF50) !important; box-shadow: 0 4px 16px rgba(46,125,50,.25) }
.btn-teacher { background: linear-gradient(135deg, #1565C0, #1E88E5) !important; box-shadow: 0 4px 16px rgba(21,101,192,.25) }
.btn-admin  { background: linear-gradient(135deg, #E65100, #FB8C00) !important; box-shadow: 0 4px 16px rgba(230,81,0,.25) }
.btn-root   { background: linear-gradient(135deg, #C62828, #EF5350) !important; box-shadow: 0 4px 16px rgba(198,40,40,.25) }

.err-msg { color: #C62828; text-align: center; font-size: 13px; margin-top: 10px; min-height: 20px }
.footer-link { text-align: center; margin-top: 20px; z-index: 1 }
.footer-link a { color: #666; font-size: 13px; cursor: pointer; opacity: .7 }
.footer-link a:active { opacity: 1 }
</style>
