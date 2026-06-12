<template>
<div class="app-shell">
  <div class="topbar t-orange">
    <span class="menu-btn" @click="showSide=!showSide">&#9776;</span>
    <span class="brand">&#9881; 选题系统-管理员端</span>
    <span class="user-info">{{ user?.dp }}</span>
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
  { path: '/admin', icon: '&#128200;', label: '数据面板' },
  { path: '/admin/teachers', icon: '&#128100;', label: '教师管理' },
  { path: '/admin/students', icon: '&#128101;', label: '学生管理' },
  { path: '/admin/topics', icon: '&#128221;', label: '题目管理' },
  { path: '/admin/settings', icon: '&#9881;', label: '系统设置' },
  { path: '/admin/password', icon: '&#128273;', label: '修改密码' }
]
onMounted(() => { user.value = JSON.parse(localStorage.getItem('user') || '{}') })
function nav(path) { router.push(path); showSide.value = false }
function logout() { localStorage.clear(); router.push('/login') }
</script>

<style scoped>
.app-shell{display:flex;flex-direction:column;height:100vh;overflow:hidden}
.topbar{display:flex;align-items:center;height:52px;padding:0 14px;color:#fff;gap:10px;flex-shrink:0}
.t-orange{background:linear-gradient(135deg,#BF360C,#E64A19)}
.topbar .brand{font-size:16px;font-weight:700;flex:1}
.topbar .user-info{font-size:12px;opacity:.85}
.menu-btn{font-size:22px;cursor:pointer;width:36px;height:36px;display:flex;align-items:center;justify-content:center;border-radius:8px}
.body-wrap{display:flex;flex:1;overflow:hidden}
.sidebar{width:220px;background:#fff;border-right:1px solid #eee;overflow-y:auto;flex-shrink:0;padding:8px 0}
.side-item{display:flex;align-items:center;gap:10px;height:46px;padding:0 18px;font-size:15px;cursor:pointer;border-left:3px solid transparent;color:#555}
.side-item:hover,.side-item.active{background:#FFF3E0;border-left-color:#FB8C00;color:#E65100}
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
