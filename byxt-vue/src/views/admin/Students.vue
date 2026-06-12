<template>
<div>
  <h3 style="color:#E65100;margin-bottom:10px">&#128101; 学生管理</h3>
  <van-empty v-if="!list.length" description="无学生数据" />
  <van-cell v-for="s in list" :key="s.id" :title="s.xm" :label="'学号：'+s.xh+' | 专业：'+(s.zy||'')+' | 班级：'+(s.bj||'')">
    <template #right-icon v-if="s.email"><van-tag type="primary">{{ s.email }}</van-tag></template>
  </van-cell>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../api'
const list = ref([])
onMounted(async () => { try { const r = await api.get('/admin/student-list'); list.value = r.list || [] } catch (e) {} })
</script>
