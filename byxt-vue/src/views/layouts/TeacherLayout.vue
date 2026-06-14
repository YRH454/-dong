<template>
<div class="shell">
  <van-nav-bar title="山东农业大学" left-text="菜单" @click-left="showSide=!showSide" fixed placeholder>
    <template #right><span class="nav-user">{{ user?.userId }} {{ user?.name }}</span></template>
  </van-nav-bar>
  <div class="body">
    <div class="sidebar" :class="{open:showSide}">
      <div class="side-header">
        <div class="side-avatar">🎯</div>
        <div class="side-user"><b>{{ user?.name || user?.userId }}</b><span>{{ user?.dp || '' }}</span></div>
      </div>
      <div class="side-menu">
        <div class="side-item" v-for="m in menus" :key="m.path" @click="nav(m.path)" :class="{active:currentPath===m.path}">
          <span class="si">{{ m.icon }}</span>{{ m.label }}
          <span v-if="m.badge&&m.badge>0" class="menu-badge">{{ m.badge }}</span>
        </div>
      </div>
      <div class="side-footer" @click="doLogout"><span class="si">🚪</span>退出登录</div>
    </div>
    <div class="overlay" v-if="showSide" @click="showSide=false"></div>
    <div class="content"><router-view v-slot="{Component}"><keep-alive><component :is="Component"/></keep-alive></router-view></div>
  </div>
</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'; import { useRouter, useRoute } from 'vue-router'
import api from '../../api'
const router=useRouter(), route=useRoute(), showSide=ref(false), pending=ref(0)
const user=JSON.parse(localStorage.getItem('user')||'{}')
const currentPath=computed(()=>route.path)
const menus=computed(()=>[{path:'/teacher/add',icon:'✏️',label:'教师出题'},{path:'/teacher/batch',icon:'📦',label:'批量出题'},{path:'/teacher/my-topics',icon:'📋',label:'选题结果',badge:pending.value},{path:'/teacher/my-students',icon:'👨‍🎓',label:'指导学生'},{path:'/teacher/profile',icon:'👤',label:'个人信息'},{path:'/teacher/password',icon:'🔑',label:'修改密码'}])
onMounted(async()=>{try{const r=await api.get('/teacher/my-topics');pending.value=r.list?.filter(t=>t.status===1).length||0}catch(e){}})
function nav(p){router.push(p);showSide.value=false}
function doLogout(){localStorage.clear();router.replace('/login')}
</script>

<style scoped>
.shell{display:flex;flex-direction:column;height:100vh;background:#f4f6f9}
.body{display:flex;flex:1;overflow:hidden}
.sidebar{width:240px;background:#fff;border-right:1px solid #eee;flex-shrink:0;display:flex;flex-direction:column}
.side-header{padding:20px 18px 16px;border-bottom:1px solid #f0f0f0;display:flex;align-items:center;gap:12px}
.side-avatar{width:40px;height:40px;border-radius:12px;background:#E3F2FD;display:flex;align-items:center;justify-content:center;font-size:20px}
.side-user b{display:block;font-size:15px;color:#333}.side-user span{font-size:12px;color:#999}
.side-menu{flex:1;padding:8px 0;overflow-y:auto}
.side-item{display:flex;align-items:center;gap:10px;height:44px;padding:0 18px;font-size:15px;cursor:pointer;border-left:3px solid transparent;color:#555;transition:all .15s;margin:2px 0}
.side-item:hover,.side-item.active{background:#E3F2FD;border-left-color:#1E88E5;color:#1565C0}
.si{font-size:18px;width:24px;text-align:center;flex-shrink:0}
.side-footer{display:flex;align-items:center;gap:10px;height:44px;padding:0 18px;cursor:pointer;color:#C62828;border-top:1px solid #f0f0f0;font-size:15px}
.menu-badge{background:#C62828;color:#fff;font-size:11px;min-width:18px;height:18px;border-radius:9px;display:flex;align-items:center;justify-content:center;margin-left:auto;padding:0 4px;font-weight:700}
.content{flex:1;overflow-y:auto;background:#f4f6f9}
.overlay{display:none}
@media(max-width:768px){.sidebar{position:fixed;top:46px;left:0;bottom:0;transform:translateX(-100%);transition:transform .25s ease;z-index:100;box-shadow:4px 0 20px rgba(0,0,0,.1)}.sidebar.open{transform:translateX(0)}.overlay{display:block;position:fixed;inset:0;background:rgba(0,0,0,.35);z-index:99}}
</style>
