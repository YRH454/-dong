<template>
<div>
  <h3 style="color:#E65100;margin-bottom:14px">&#9881; 系统设置</h3>
  <div class="card">
    <van-field v-model="dp" label="部门名称" placeholder="请输入新部门名称" />
    <van-button type="primary" block round @click="saveDp" :loading="saving" style="margin-top:10px">修改部门</van-button>
  </div>
  <h3 style="color:#C62828;margin:20px 0 10px">&#9888; 未选题学生</h3>
  <van-cell v-for="s in unsel" :key="s.id" :title="s.xm" :label="'学号：'+s.xh+' | '+s.zy+' | '+s.bj" />
  <van-empty v-if="!unsel.length" description="所有学生均已选题 &#127881;" />
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast } from 'vant'
import api from '../../api'
const dp = ref(''), saving = ref(false), unsel = ref([])
onMounted(async () => {
  try { const r = await api.get('/admin/unselected-students'); unsel.value = r.list || [] } catch (e) {}
  const u = JSON.parse(localStorage.getItem('user') || '{}'); dp.value = u.dp || ''
})
async function saveDp() {
  if (!dp.value) { showToast('部门不能为空'); return }
  saving.value = true
  try { const r = await api.post('/admin/update-department', { dp: dp.value }); if (r.code === 0) { showToast('已更新'); const u = JSON.parse(localStorage.getItem('user') || '{}'); u.dp = dp.value; localStorage.setItem('user', JSON.stringify(u)) } else showToast(r.msg) } catch (e) {}
  saving.value = false
}
</script>
<style scoped>.card{background:#fff;border-radius:12px;padding:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}</style>
