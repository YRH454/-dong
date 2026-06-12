<template>
<div class="shell">
  <van-nav-bar :title="'选题系统'" left-text="菜单" @click-left="showSide=!showSide" fixed placeholder>
    <template #right>
      <span class="nav-user">{{ user?.userId }} {{ user?.name }}</span>
    </template>
  </van-nav-bar>

  <div class="body">
    <!-- Sidebar -->
    <div class="sidebar" :class="{open:showSide}">
      <div class="side-header">
        <div class="side-avatar">🎓</div>
        <div class="side-user">
          <b>{{ user?.name || user?.userId }}</b>
          <span>{{ user?.dp || '' }}</span>
        </div>
      </div>
      <div class="side-menu">
        <div class="side-item" v-for="m in menus" :key="m.path"
             @click="nav(m.path)" :class="{active: currentPath===m.path}">
          <span class="si">{{ m.icon }}</span>{{ m.label }}
        </div>
      </div>
      <div class="side-footer" @click="doLogout">
        <span class="si">🚪</span>退出登录
      </div>
    </div>
    <!-- Overlay -->
    <div class="overlay" v-if="showSide" @click="showSide=false"></div>
    <!-- Content -->
    <div class="content">
      <router-view v-slot="{ Component }">
        <keep-alive><component :is="Component" /></keep-alive>
      </router-view>
    </div>
  </div>
</div>
</template>

<script setup>
import { ref, computed } from 'vue'; import { useRouter, useRoute } from 'vue-router'
const router=useRouter(), route=useRoute(), showSide=ref(false)
const user=JSON.parse(localStorage.getItem('user')||'{}')
const currentPath=computed(()=>route.path)
const menus=[{path:'/student/topics',icon:'📝',label:'选择题目'},{path:'/student/my-result',icon:'📋',label:'选题结果'},{path:'/student/profile',icon:'👤',label:'个人信息'},{path:'/student/password',icon:'🔑',label:'修改密码'}]
function nav(p){router.push(p);showSide.value=false}
function doLogout(){localStorage.clear();router.replace('/login')}
</script>

<style scoped>
.shell { display:flex; flex-direction:column; height:100vh; background:#f5f7f5 }
.body { display:flex; flex:1; overflow:hidden }
.sidebar { width:240px; background:#fff; border-right:1px solid #eee; flex-shrink:0; display:flex; flex-direction:column }
.side-header { padding:20px 18px 16px; border-bottom:1px solid #f0f0f0; display:flex; align-items:center; gap:12px }
.side-avatar { width:40px; height:40px; border-radius:12px; background:#E8F5E9; display:flex; align-items:center; justify-content:center; font-size:20px }
.side-user b { display:block; font-size:15px; color:#333 } .side-user span { font-size:12px; color:#999 }
.side-menu { flex:1; padding:8px 0; overflow-y:auto }
.side-item { display:flex; align-items:center; gap:10px; height:44px; padding:0 18px; font-size:15px; cursor:pointer; border-left:3px solid transparent; color:#555; transition:all .15s; margin:2px 0 }
.side-item:hover,.side-item.active { background:#E8F5E9; border-left-color:#4CAF50; color:#2E7D32 }
.si { font-size:18px; width:24px; text-align:center; flex-shrink:0 }
.side-footer { display:flex; align-items:center; gap:10px; height:44px; padding:0 18px; cursor:pointer; color:#C62828; border-top:1px solid #f0f0f0; font-size:15px }
.content { flex:1; overflow-y:auto; background:#f5f7f5 }
.overlay { display:none }
@media(max-width:768px){
  .sidebar { position:fixed; top:46px; left:0; bottom:0; transform:translateX(-100%); transition:transform .25s ease; z-index:100; box-shadow:4px 0 20px rgba(0,0,0,.1) }
  .sidebar.open { transform:translateX(0) }
  .overlay { display:block; position:fixed; inset:0; background:rgba(0,0,0,.35); z-index:99 }
}
</style>
