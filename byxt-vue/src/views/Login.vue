<template>
<div class="login-shell" :class="themeClass">
  <div class="deco-top"></div>
  <div class="deco-dots"></div>
  <div class="glow g1"></div><div class="glow g2"></div><div class="glow g3"></div><div class="glow g4"></div><div class="glow g5"></div><div class="glow g6"></div>
  <div class="ring r1"></div><div class="ring r2"></div><div class="ring r3"></div>
  <div class="fline l1"></div><div class="fline l2"></div><div class="fline l3"></div><div class="fline l4"></div><div class="fline l5"></div>
  <canvas ref="pc" class="particles"></canvas>
  <div class="grain"></div>

  <div class="brand-area">
    <div class="brand-icon-wrapper">
      <span class="brand-icon">{{ role==='root' ? '🔐' : '📖' }}</span>
    </div>
    <h1 class="brand-name">山东农业大学选课系统</h1>
    <p class="brand-sub">{{ role==='root' ? '超级管理员入口' : 'GRADUATION PROJECT SELECTION' }}</p>
  </div>

  <div class="role-bar" v-if="role !== 'root'">
    <div v-for="r in roles" :key="r.val" class="role-chip" :class="{active:role===r.val}" @click="switchRole(r.val)">
      <span class="chip-icon">{{ r.icon }}</span><span class="chip-label">{{ r.label }}</span>
    </div>
  </div>

  <div class="login-card">
    <div class="card-inner">
      <van-field v-model="userid" label="账号" placeholder="学号/工号/用户名" :left-icon="role==='student'?'friends-o':role==='teacher'?'manager-o':'user-o'" clearable />
      <van-field v-model="userpwd" label="密码" type="password" placeholder="输入密码" left-icon="lock" />
      <van-field v-if="role!=='root'" v-model="dpName" readonly is-link label="部门" placeholder="选择所属部门" left-icon="cluster-o" @click="showDp=true" />
      <button class="login-btn" :class="'btn-'+role" @click="doLogin" :disabled="loading">{{ loading ? '登录中...' : '进入系统' }}</button>
      <p class="err-msg" v-if="errMsg">{{ errMsg }}</p>
    </div>
  </div>

  <div class="footer-link">
    <a v-if="role==='root'" @click="switchRole('student')">← 返回普通登录</a>
    <a v-else @click="switchRole('root')">超级管理员入口</a>
  </div>

  <van-popup v-model:show="showDp" round position="bottom">
    <van-picker :columns="dpList" @confirm="onDpConfirm" @cancel="showDp=false" title="选择部门" />
  </van-popup>
</div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import api from '../api'

const router = useRouter()
const userid = ref(''), userpwd = ref(''), dp = ref(''), dpName = ref('')
const role = ref('student'), errMsg = ref(''), loading = ref(false)
const showDp = ref(false), dpList = ref([]), pc = ref(null)
let animId = 0
onMounted(() => {
  const c = pc.value; if (!c) return
  const ctx = c.getContext('2d'); let w, h
  const rs = () => { w = c.width = innerWidth; h = c.height = innerHeight }
  rs(); addEventListener('resize', rs)
  const ps = Array.from({length:60}, () => ({x:Math.random()*w,y:Math.random()*h,r:Math.random()*2+.5,vx:(Math.random()-.5)*.3,vy:(Math.random()-.5)*.3,a:Math.random()*.12+.03}))
  ;(function draw(){ctx.clearRect(0,0,w,h);ps.forEach(p=>{p.x+=p.vx;p.y+=p.vy;if(p.x<0)p.x=w;if(p.x>w)p.x=0;if(p.y<0)p.y=h;if(p.y>h)p.y=0;ctx.beginPath();ctx.arc(p.x,p.y,p.r,0,Math.PI*2);ctx.fillStyle='rgba(0,0,0,'+p.a+')';ctx.fill()});animId=requestAnimationFrame(draw)})()
})
onUnmounted(() => cancelAnimationFrame(animId))

const roles = [
  { val: 'student', label: '学生', icon: '🎓' },
  { val: 'teacher', label: '教师', icon: '🎯' },
  { val: 'admin', label: '管理', icon: '⚙️' }
]
const themeClass = computed(() => 'theme-' + role.value)

onMounted(async () => {
  try { const res = await api.get('/departments'); dpList.value = res.map(d => ({ text: d.label, value: d.value })); if (dpList.value.length > 0) { dp.value = dpList.value[0].value; dpName.value = dpList.value[0].text } } catch (e) {}
})

function switchRole(r) { role.value = r; errMsg.value = ''; if (dpList.value.length > 0 && r !== 'root') { dp.value = dpList.value[0].value; dpName.value = dpList.value[0].text } }

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
      showToast({ message: '登录成功', duration: 500 })
      const to = { student: '/student', teacher: '/teacher', admin: '/admin', root: '/admin' }
      setTimeout(() => { router.replace(to[res.role] || '/login') }, 300)
    } else { errMsg.value = res.msg }
  } catch (e) { errMsg.value = '网络连接失败' }
  loading.value = false
}
function onDpConfirm({ selectedOptions }) { dp.value = selectedOptions[0].value; dpName.value = selectedOptions[0].text; showDp.value = false }
</script>

<style scoped>
.login-shell { min-height: 100vh; min-height: 100dvh; display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 24px 20px; position: relative; overflow: hidden; transition: background .4s ease }
.theme-student { background: linear-gradient(160deg, #e8f5e9, #c8e6c9 30%, #f1f8e9 60%, #e8f5e9) }
.theme-teacher { background: linear-gradient(160deg, #e3f2fd, #bbdefb 30%, #e8eaf6 60%, #e3f2fd) }
.theme-admin  { background: linear-gradient(160deg, #fff3e0, #ffe0b2 30%, #fef7e0 60%, #fff3e0) }
.theme-root   { background: linear-gradient(160deg, #ffebee, #ffcdd2 30%, #fce4ec 60%, #ffebee) }
.deco-top { position: absolute; top: -60px; right: -60px; width: 200px; height: 200px; border-radius: 50%; opacity: .06; pointer-events: none }
.theme-student .deco-top { background: #2E7D32 } .theme-teacher .deco-top { background: #1565C0 }
.theme-admin .deco-top { background: #E65100 } .theme-root .deco-top { background: #C62828 }
.deco-dots { position: absolute; bottom: 40px; left: 20px; width:60px; height:60px; background-image: radial-gradient(circle, rgba(0,0,0,.04) 1px, transparent 1px); background-size: 8px 8px; pointer-events: none }
.particles { position: absolute; inset: 0; width: 100%; height: 100%; pointer-events: none; z-index: 0 }
.glow { position: absolute; border-radius: 50%; filter: blur(60px); opacity: .25; animation: gFloat 17s ease-in-out infinite; pointer-events: none }
.g1 { width: 350px; height: 350px; top: -130px; left: -90px; background: #66BB6A }
.g2 { width: 280px; height: 280px; top: 10%; right: -110px; background: #42A5F5; animation-delay: -5s }
.g3 { width: 230px; height: 230px; bottom: -80px; left: 30%; background: #FFA726; animation-delay: -10s }
.g4 { width: 190px; height: 190px; top: 60%; left: -60px; background: #AB47BC; animation-delay: -14s }
.g5 { width: 160px; height: 160px; top: 35%; left: 55%; background: #26C6DA; animation-delay: -8s }
.g6 { width: 120px; height: 120px; top: 70%; right: 25%; background: #EF5350; animation-delay: -12s }
@keyframes gFloat { 0%,100% { transform: translate(0,0) scale(1) } 25% { transform: translate(45px,-35px) scale(1.15) } 50% { transform: translate(-30px,25px) scale(.92) } 75% { transform: translate(20px,35px) scale(1.08) } }
.ring { position: absolute; border-radius: 50%; border: 2.5px solid; opacity: .1; animation: rPulse 9s ease-in-out infinite; pointer-events: none }
.r1 { width: 180px; height: 180px; top: 8%; right: 10%; border-color: #66BB6A }
.r2 { width: 140px; height: 140px; bottom: 15%; left: 6%; border-color: #42A5F5; animation-delay: -3s }
.r3 { width: 120px; height: 120px; top: 50%; left: 65%; border-color: #FFA726; animation-delay: -6s }
@keyframes rPulse { 0%,100% { transform: scale(1); opacity: .06 } 50% { transform: scale(1.35); opacity: .18 } }
.fline { position: absolute; height: 1.5px; opacity: .05; animation: lScan 13s linear infinite; pointer-events: none; border-radius: 1px }
.l1 { width: 60%; top: 20%; left: 18%; transform: rotate(-6deg); background: #66BB6A }
.l2 { width: 45%; top: 38%; right: 10%; transform: rotate(7deg); background: #42A5F5; animation-delay: -3s }
.l3 { width: 52%; top: 55%; left: 25%; transform: rotate(-4deg); background: #FFA726; animation-delay: -7s }
.l4 { width: 38%; top: 72%; right: 18%; transform: rotate(5deg); background: #AB47BC; animation-delay: -10s }
.l5 { width: 46%; top: 10%; left: 38%; transform: rotate(-8deg); background: #26C6DA; animation-delay: -12s }
@keyframes lScan { 0% { opacity: .03; transform: translateX(-4%) } 50% { opacity: .1; transform: translateX(4%) } 100% { opacity: .03; transform: translateX(-4%) } }
.grain { position: absolute; inset: 0; opacity: .02; pointer-events: none; background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='.9' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)'/%3E%3C/svg%3E"); background-size: 200px 200px }
.brand-area { text-align: center; margin-bottom: 20px; z-index: 1 }
.brand-icon-wrapper { margin-bottom: 8px }
.brand-icon { font-size: 44px; display: inline-block; animation: float 3s ease-in-out infinite }
@keyframes float { 0%,100% { transform: translateY(0) } 50% { transform: translateY(-6px) } }
.brand-name { font-size: 28px; font-weight: 800; letter-spacing: 3px; margin: 0; background: linear-gradient(135deg, #1B5E20, #2E7D32); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text }
.theme-teacher .brand-name { background: linear-gradient(135deg, #0D47A1, #1565C0); -webkit-background-clip: text; background-clip: text }
.theme-admin .brand-name { background: linear-gradient(135deg, #BF360C, #E65100); -webkit-background-clip: text; background-clip: text }
.theme-root .brand-name { background: linear-gradient(135deg, #B71C1C, #C62828); -webkit-background-clip: text; background-clip: text }
.brand-sub { font-size: 11px; color: #999; letter-spacing: 1px; text-transform: uppercase; margin-top: 4px }
.role-bar { display: flex; gap: 6px; margin-bottom: 16px; z-index: 1; background: rgba(255,255,255,.5); backdrop-filter: blur(10px); border-radius: 28px; padding: 4px }
.role-chip { display: flex; align-items: center; gap: 4px; padding: 9px 16px; border-radius: 24px; font-size: 14px; cursor: pointer; transition: all .25s; user-select: none; font-weight: 500; color: #666 }
.role-chip .chip-icon { font-size: 16px }
.role-chip.active { background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,.08); font-weight: 600 }
.login-card { width: 100%; max-width: 400px; z-index: 1 }
.card-inner { background: rgba(255,255,255,.85); backdrop-filter: blur(20px); border-radius: 20px; padding: 16px 12px 20px; box-shadow: 0 8px 32px rgba(0,0,0,.06), 0 1px 2px rgba(0,0,0,.04) }
:deep(.van-field) { border-radius: 12px; margin-bottom: 2px }
.login-btn { width: 100%; margin-top: 18px; height: 50px; font-size: 16px; font-weight: 700; letter-spacing: 4px; border: none; border-radius: 25px; color: #fff; cursor: pointer; transition: all .2s }
.login-btn:active { transform: scale(.97) }
.login-btn:disabled { opacity: .6; cursor: not-allowed }
.btn-student { background: linear-gradient(135deg, #2E7D32, #4CAF50) !important; box-shadow: 0 4px 16px rgba(46,125,50,.25) }
.btn-teacher { background: linear-gradient(135deg, #1565C0, #1E88E5) !important; box-shadow: 0 4px 16px rgba(21,101,192,.25) }
.btn-admin  { background: linear-gradient(135deg, #E65100, #FB8C00) !important; box-shadow: 0 4px 16px rgba(230,81,0,.25) }
.btn-root   { background: linear-gradient(135deg, #C62828, #EF5350) !important; box-shadow: 0 4px 16px rgba(198,40,40,.25) }
.err-msg { color: #C62828; text-align: center; font-size: 13px; margin-top: 10px; min-height: 20px }
.footer-link { text-align: center; margin-top: 20px; z-index: 1 }
.footer-link a { color: #666; font-size: 13px; cursor: pointer; opacity: .7 }
.footer-link a:active { opacity: 1 }
</style>
