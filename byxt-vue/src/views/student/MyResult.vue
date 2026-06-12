<template>
<div>
  <h3 style="color:#2E7D32;margin-bottom:14px">&#128203; 选题结果</h3>
  <van-empty v-if="!list.length" description="暂无选题记录" />
  <van-cell-group v-for="item in list" :key="item.id" inset style="margin-bottom:12px">
    <van-cell title="题目" :value="item.tm" />
    <van-cell title="指导教师" :value="item.txm + '（' + item.tzhicheng + '）'" />
    <van-cell title="备注" :value="item.bz" />
    <van-cell title="联系方式" :label="'邮箱：'+(item.temail||'')+' 电话：'+(item.tphone||'')+' QQ：'+(item.tqq||'')" />
    <van-cell title="办公地点" :value="item.tbgdd" />
    <van-button v-if="canDelete" type="danger" block round @click="doDel" style="margin:8px 0">删除记录</van-button>
  </van-cell-group>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
const list = ref([]), canDelete = ref(false)
async function load() {
  try { const r = await api.get('/student/my-result'); list.value = r.list || []; canDelete.value = r.canDelete } catch (e) {}
}
async function doDel() {
  try { await showConfirmDialog({ title: '确认删除', message: '删除后不可恢复？' }) } catch { return }
  try { await api.get('/student/my-result?action=delete'); showToast('已删除'); load() } catch (e) {}
}
onMounted(load)
</script>
