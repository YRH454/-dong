<template>
<div class="app-shell">
  <div class="topbar">
    <span class="menu-btn" @click="showSide=!showSide">&#9776;</span>
    <span class="brand">&#127891; 选题系统-学生端</span>
    <span class="user-info">{{ user?.userId }} {{ user?.name }}</span>
  </div>
  <div class="body-wrap">
    <div class="sidebar" :class="{open:showSide}">
      <div class="side-item" v-for="m in menus" :key="m.path" @click="nav(m.path)" :class="{active:$route.path===m.path}">
        <span class="icon">{{ m.icon }}</span>{{ m.label }}
      </div>
      <div class="side-item danger" @click="logout">&#128682; 退出系统</div>
    </div>
    <div class="overlay" v-if="showSide" @click="showSide=false" />
    <div class="content"><router-view /></div>
  </div>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const router = useRouter(); const route = useRoute()
const showSide = ref(false), user = ref(null)
const menus = [
  { path: '/student/topics', icon: '&#128221;', label: '选择题目' },
  { path: '/student/my-result', icon: '&#128203;', label: '选题结果' },
  { path: '/student/profile', icon: '&#128100;', label: '个人信息' },
  { path: '/student/password', icon: '&#128273;', label: '修改密码' }
]
onMounted(() => { user.value = JSON.parse(localStorage.getItem('user') || '{}') })
function nav(path) { router.push(path); showSide.value = false }
function logout() { localStorage.clear(); router.push('/login') }
</script>

<style scoped>
.app-shell{display:flex;flex-direction:column;height:100vh;overflow:hidden}
.topbar{display:flex;align-items:center;height:52px;padding:0 14px;background:linear-gradient(135deg,#1B5E20,#2E7D32);color:#fff;gap:10px;flex-shrink:0}
.topbar .brand{font-size:16px;font-weight:700;flex:1}
.topbar .user-info{font-size:12px;opacity:.85}
.menu-btn{font-size:22px;cursor:pointer;width:36px;height:36px;display:flex;align-items:center;justify-content:center;border-radius:8px}
.body-wrap{display:flex;flex:1;overflow:hidden}
.sidebar{width:220px;background:#fff;border-right:1px solid #eee;overflow-y:auto;flex-shrink:0;padding:8px 0}
.side-item{display:flex;align-items:center;gap:10px;height:46px;padding:0 18px;font-size:15px;cursor:pointer;border-left:3px solid transparent;color:#555}
.side-item:hover,.side-item.active{background:#E8F5E9;border-left-color:#4CAF50;color:#2E7D32}
.side-item .icon{font-size:18px;width:24px;text-align:center}
.side-item.danger{color:#C62828;margin-top:auto;border-top:1px solid #eee}
.content{flex:1;overflow-y:auto;padding:12px 14px}
.overlay{display:none}
@media(max-width:768px){
  .sidebar{position:fixed;top:52px;left:0;bottom:0;transform:translateX(-100%);transition:transform .25s;z-index:100;box-shadow:4px 0 20px rgba(0,0,0,.15)}
  .sidebar.open{transform:translateX(0)}
  .overlay{display:block;position:fixed;inset:0;background:rgba(0,0,0,.35);z-index:99}
}
</style>
