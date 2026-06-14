<template>
<div>
  <h3 style="color:#2E7D32;margin-bottom:10px">&#128065; 题目浏览</h3>
  <div class="alert">当前仅可查看，不能选题</div>
  <van-search v-model="kw" placeholder="搜索..." @search="onS" @input="onS" shape="round" />
  <van-empty v-if="!filtered.length" description="无匹配题目" />
  <van-cell v-for="t in paged" :key="t.id" :title="t.tm" :label="'教师：' + t.txm + (t.bz ? ' | ' + t.bz : '')" />
  <van-pagination v-model="page" :total-items="filtered.length" :page-size="10" style="margin-top:12px" />
</div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../../api'
const list = ref([]), kw = ref(''), page = ref(1)
const filtered = computed(() => list.value.filter(t => !kw.value || t.tm.includes(kw.value) || t.txm.includes(kw.value)))
const paged = computed(() => filtered.value.slice((page.value-1)*10, page.value*10))
onMounted(async () => { try { const r = await api.get('/student/topics'); list.value = r.list || [] } catch (e) {} })
function onS() { page.value = 1 }
</script>
<style scoped>.alert{background:#FFF8E1;color:#E65100;padding:12px;border-radius:8px;font-size:14px;margin-bottom:10px;border:1px solid #FFECB3}</style>
