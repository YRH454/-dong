<template>
<div class="app-shell">
  <van-nav-bar :title="isRoot?'选题系统-超级管理员':'选题系统-管理端'" left-text="菜单" @click-left="showSide=!showSide" fixed placeholder>
    <template #right><span style="font-size:13px;color:#888">{{ isRoot ? '全局管理' : user?.dp }}</span></template>
  </van-nav-bar>
  <div class="body-wrap">
    <div class="sidebar" :class="{open:showSide}">
      <div class="side-item" v-for="m in menus" :key="m.path" @click="nav(m.path)" :class="{active: currentPath===m.path}">
        <span class="si-icon">{{ m.icon }}</span><span>{{ m.label }}</span>
      </div>
      <div class="side-item logout" @click="doLogout"><span class="si-icon">🚪</span><span>退出系统</span></div>
    </div>
    <div class="overlay" v-if="showSide" @click="showSide=false" />
    <div class="content">
      <router-view v-slot="{ Component }">
        <keep-alive><component :is="Component" /></keep-alive>
      </router-view>
    </div>
  </div>
</div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const router = useRouter(); const route = useRoute()
const showSide = ref(false), user = JSON.parse(localStorage.getItem('user') || '{}')
const isRoot = computed(() => localStorage.getItem('role') === 'root')
const currentPath = computed(() => route.path)
const adminMenus = [
  { path: '/admin', icon: '📊', label: '数据面板' },
  { path: '/admin/announcements', icon: '📢', label: '通知公告' },
  { path: '/admin/teachers', icon: '👨‍🏫', label: '教师管理' },
  { path: '/admin/students', icon: '👥', label: '学生管理' },
  { path: '/admin/topics', icon: '📝', label: '题目管理' },
  { path: '/admin/settings', icon: '⚙️', label: '系统设置' },
  { path: '/admin/password', icon: '🔑', label: '修改密码' }
]
const rootMenus = [
  { path: '/admin', icon: '📊', label: '全局看板' },
  { path: '/admin/departments', icon: '🏢', label: '部门管理' },
  { path: '/admin/announcements', icon: '📢', label: '通知公告' },
  { path: '/admin/teachers', icon: '👨‍🏫', label: '教师管理' },
  { path: '/admin/students', icon: '👥', label: '学生管理' },
  { path: '/admin/topics', icon: '📝', label: '题目管理' },
  { path: '/admin/password', icon: '🔑', label: '修改密码' }
]
const menus = computed(() => isRoot.value ? rootMenus : adminMenus)
function nav(path) { router.push(path); showSide.value = false }
function doLogout() { localStorage.clear(); router.replace('/login') }
</script>

<style scoped>
.app-shell{display:flex;flex-direction:column;height:100vh;overflow:hidden;background:#fdf7f3}
.body-wrap{display:flex;flex:1;overflow:hidden}
.sidebar{width:220px;background:#fff;border-right:1px solid #eee;overflow-y:auto;flex-shrink:0;padding:8px 0;display:flex;flex-direction:column}
.side-item{display:flex;align-items:center;gap:10px;height:46px;padding:0 18px;font-size:15px;cursor:pointer;border-left:3px solid transparent;color:#555;transition:all .12s}
.side-item:hover,.side-item.active{background:#FFF3E0;border-left-color:#FB8C00;color:#E65100}
.si-icon{font-size:16px;width:24px;text-align:center}
.side-item.logout{color:#C62828;margin-top:auto;border-top:1px solid #eee}
.side-item.logout:hover{background:#FFEBEE;border-left-color:#C62828}
.content{flex:1;overflow-y:auto}
.overlay{display:none}
@media(max-width:768px){
  .sidebar{position:fixed;top:46px;left:0;bottom:0;transform:translateX(-100%);transition:transform .25s ease;z-index:100;box-shadow:4px 0 20px rgba(0,0,0,.15)}
  .sidebar.open{transform:translateX(0)}
  .overlay{display:block;position:fixed;inset:0;background:rgba(0,0,0,.35);z-index:99}
}
</style>
