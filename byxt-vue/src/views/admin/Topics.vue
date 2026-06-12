<template>
<div>
  <h3 style="color:#E65100;margin-bottom:10px">&#128221; 题目管理</h3>
  <van-empty v-if="!list.length" description="无题目数据" />
  <van-cell v-for="t in list" :key="t.id" :title="t.tm" :label="'教师：'+t.txm+' | 学生：'+(t.xh||'未选')">
    <template #right-icon><van-button size="mini" type="danger" @click="delT(t.id)">删除</van-button></template>
  </van-cell>
  <van-button v-if="list.length" type="danger" block round @click="clearAll" style="margin-top:16px">清空所有题目</van-button>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
const list = ref([])
onMounted(async () => { try { const r = await api.get('/admin/topic-list'); list.value = r.list || [] } catch (e) {} })
async function delT(id) { try { await showConfirmDialog({title:'确认',message:'删除该题目？'}) } catch { return }; try { await api.post('/admin/delete-topic',{id}); list.value = list.value.filter(t => t.id !== id); showToast('已删除') } catch (e) {} }
async function clearAll() { try { await showConfirmDialog({title:'危险操作',message:'确认清空所有题目？不可恢复！'}) } catch { return }; try { await api.post('/admin/clear-topics',{}); list.value = []; showToast('已清空') } catch (e) {} }
</script>
