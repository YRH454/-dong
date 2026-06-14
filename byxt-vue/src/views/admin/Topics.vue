<template>
<div class="page">
  <div class="page-header">
    <h3>题目管理</h3>
    <span class="count">共 {{ list.length }} 题</span>
  </div>

  <van-empty v-if="!list.length" description="无题目数据" />

  <div class="t-card" v-for="t in list" :key="t.id">
    <div class="t-head">
      <span class="t-title">{{ t.tm }}</span>
      <van-tag v-if="t.xh" type="success" size="small">已选</van-tag>
      <van-tag v-else size="small">待选</van-tag>
    </div>
    <div class="t-body">
      <div>教师：{{ t.txm }}（{{ t.gh }}）</div>
      <div v-if="t.xh">学生：{{ t.sxm }}（{{ t.xh }}）{{ t.zy||'' }} {{ t.bj||'' }}</div>
      <div v-if="t.bz" style="color:#999;font-size:12px">备注：{{ t.bz }}</div>
    </div>
    <div class="t-foot">
      <van-button size="mini" type="danger" @click="delT(t.id)">删除</van-button>
    </div>
  </div>

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
async function clearAll() { try { await showConfirmDialog({title:'危险操作',message:'确认清空所有题目？'}) } catch { return }; try { await api.post('/admin/clear-topics',{}); list.value = []; showToast('已清空') } catch (e) {} }
</script>

<style scoped>
.page{padding:12px 14px}
.page-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px}
.page-header h3{font-size:18px;color:#E65100}
.count{font-size:13px;color:#999}
.t-card{background:#fff;border-radius:12px;padding:14px;margin-bottom:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.t-head{display:flex;align-items:center;gap:8px;margin-bottom:6px}
.t-title{font-size:15px;font-weight:600;flex:1}
.t-body{font-size:13px;color:#666;line-height:1.8}
.t-foot{display:flex;justify-content:flex-end;margin-top:8px}
</style>
