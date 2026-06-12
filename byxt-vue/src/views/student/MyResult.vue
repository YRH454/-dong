<template>
<van-pull-refresh v-model="refreshing" @refresh="load">
  <van-empty v-if="!refreshing && !list.length" image="records" description="暂无选题记录" />
  <div v-else>
    <van-cell-group v-for="item in list" :key="item.id" inset style="margin-bottom:12px">
      <van-cell title="题目" :value="item.tm" />
      <van-cell title="指导教师" :value="`${item.txm}（${item.tzhicheng||''}）`" />
      <van-cell title="备注" :value="item.bz||'无'" />
      <van-cell title="联系方式" label="办公地点" :value="item.tbgdd||'无'" />
      <van-cell title="电话">{{ item.tphone||'无' }} / QQ:{{ item.tqq||'无' }} / {{ item.temail||'无' }}</van-cell>
      <div style="padding:8px;text-align:center" v-if="canDelete">
        <van-button type="danger" size="small" round @click="doDel" block>删除此记录</van-button>
      </div>
    </van-cell-group>
  </div>
</van-pull-refresh>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
const list = ref([]), canDelete = ref(false), refreshing = ref(false)
async function load() {
  try { const r = await api.get('/student/my-result'); list.value = r.list || []; canDelete.value = r.canDelete } catch (e) {}
  refreshing.value = false
}
async function doDel() {
  try { await showConfirmDialog({ title: '确认删除', message: '删除后不可恢复，确定删除？' }) } catch { return }
  try { await api.get('/student/my-result?action=delete'); showToast({ message: '已删除', icon: 'success' }); load() } catch (e) {}
}
onMounted(load)
</script>
