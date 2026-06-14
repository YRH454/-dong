<template>
<van-pull-refresh v-model="refreshing" @refresh="load">
  <div class="dept-page">
    <div class="stat-hero-card">
      <b>{{ depts.length }}</b><span>管理部门</span>
    </div>

    <div class="section-card">
      <h4>添加部门（管理员账号）</h4>
      <van-field v-model="newUserid" label="账号" placeholder="管理员登录账号" />
      <van-field v-model="newDp" label="部门名称" placeholder="如：计算机科学与技术系" />
      <van-button block round type="primary" @click="addDept" :loading="adding" style="margin-top:10px">创建部门</van-button>
    </div>

    <div class="section-card">
      <h4>部门列表</h4>
      <div class="dept-item" v-for="d in depts" :key="d.userid">
        <div class="dept-info">
          <span class="dept-dp">{{ d.dp }}</span>
          <span class="dept-id">账号：{{ d.userid }}</span>
        </div>
        <van-button size="small" plain type="primary" @click="resetPwd(d)">重置密码</van-button>
        <van-button size="small" plain type="danger" @click="delDept(d)">删除</van-button>
      </div>
      <div v-if="!depts.length" class="empty-inline">暂无部门</div>
    </div>
  </div>
</van-pull-refresh>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'

const depts = ref([]), refreshing = ref(false), adding = ref(false)
const newUserid = ref(''), newDp = ref('')

async function load() {
  try { const r = await api.get('/admin/departments-all'); if (r.code === 0) depts.value = r.list } catch (e) {}
  refreshing.value = false
}

async function addDept() {
  if (!newUserid.value || !newDp.value) { showToast('请填写完整'); return }
  adding.value = true
  try { const r = await api.post('/admin/add-department', { userid: newUserid.value, dp: newDp.value }); showToast(r.msg); if (r.code === 0) { newUserid.value = ''; newDp.value = ''; load() } } catch (e) {}
  adding.value = false
}

async function delDept(d) {
  try { await showConfirmDialog({ title: '⚠️ 删除部门（第一步）', message: `确定要删除部门"${d.dp}"（账号：${d.userid}）吗？\n\n这将删除该部门下的所有教师、学生、题目、公告数据！` }) } catch { return }
  try { await showConfirmDialog({ title: '⚠️ 最终确认（第二步）', message: `此操作完全不可恢复！\n\n请再次确认：删除部门"${d.dp}"及所有关联数据？`, confirmButtonText: '确认删除', confirmButtonColor: '#C62828' }) } catch { return }
  try { const r = await api.post('/admin/delete-department', { userid: d.userid }); showToast(r.msg); if (r.code === 0) load() } catch (e) { showToast('删除失败') }
}

async function resetPwd(d) {
  try { const r = await api.post('/admin/reset-admin-pwd', { userid: d.userid }); showToast(r.msg) } catch (e) { showToast('重置失败') }
}

onMounted(load)
</script>

<style scoped>
.dept-page { padding: 12px 14px; min-height: 100vh }
.stat-hero-card { background: linear-gradient(135deg, #C62828, #EF5350); border-radius: 14px; padding: 24px; text-align: center; color: #fff; margin-bottom: 14px; box-shadow: 0 4px 16px rgba(198,40,40,.2) }
.stat-hero-card b { display: block; font-size: 36px; font-weight: 800 }
.stat-hero-card span { font-size: 13px; opacity: .85 }
.section-card { background: #fff; border-radius: 14px; padding: 16px; margin-bottom: 14px; box-shadow: 0 2px 12px rgba(0,0,0,.04) }
.section-card h4 { font-size: 15px; font-weight: 700; color: #333; margin-bottom: 12px }
.dept-item { display: flex; align-items: center; gap: 8px; padding: 12px 0; border-bottom: 1px solid #f0f0f0 }
.dept-item:last-child { border-bottom: none }
.dept-info { flex: 1 }
.dept-dp { display: block; font-size: 15px; font-weight: 500; color: #333 }
.dept-id { font-size: 12px; color: #999 }
.empty-inline { text-align: center; padding: 20px; color: #ccc; font-size: 14px }
</style>
