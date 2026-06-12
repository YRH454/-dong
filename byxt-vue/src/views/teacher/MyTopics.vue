<template>
<div>
  <div class="bar"><h3 style="color:#1565C0">&#128203; 选题结果</h3><span class="tag">已选 {{ selCount }} 人</span></div>
  <p class="tip">点击题目可修改分配学生信息</p>
  <van-empty v-if="!list.length" description="暂无出题记录" />
  <van-collapse v-model="activeNames" accordion v-for="t in list" :key="t.id">
    <van-collapse-item :title="t.tm" :name="t.id">
      <van-cell title="教师" :value="t.txm" />
      <van-cell title="备注" :value="t.bz" />
      <van-field v-model="edit.xh" label="学号" placeholder="输入学号" />
      <van-button size="small" @click="checkStu" style="margin:4px 0">检测学号</van-button>
      <van-field v-model="edit.sxm" label="学生姓名" />
      <van-field v-model="edit.zy" label="专业" />
      <van-field v-model="edit.bj" label="班级" />
      <div style="display:flex;gap:8px;margin-top:8px">
        <van-button type="primary" size="small" @click="updateTopic(t.id)">更新</van-button>
        <van-button type="danger" size="small" @click="delTopic(t.id)">删除</van-button>
      </div>
    </van-collapse-item>
  </van-collapse>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
const list = ref([]), selCount = ref(0), activeNames = ref([])
const edit = ref({ xh:'', sxm:'', zy:'', bj:'' })
async function load() { try { const r = await api.get('/teacher/my-topics'); list.value = r.list || []; selCount.value = r.selectedCount } catch (e) {} }
async function checkStu() {
  try { const r = await api.post('/teacher/check-student', { xh: edit.value.xh }); if (r.code === 0) { edit.value.sxm = r.xm; edit.value.zy = r.zy; edit.value.bj = r.bj } else showToast(r.msg) } catch (e) {}
}
async function updateTopic(id) {
  try { await api.post('/teacher/update-topic', { id, ...edit.value }); showToast('更新成功'); load() } catch (e) {}
}
async function delTopic(id) {
  try { await showConfirmDialog({ title: '确认删除', message: '删除该题目？' }) } catch { return }
  try { await api.post('/teacher/delete-topic', { id }); showToast('已删除'); load() } catch (e) {}
}
onMounted(load)
</script>
<style scoped>.bar{display:flex;align-items:center;gap:10px;margin-bottom:6px}.tag{background:#E3F2FD;color:#1565C0;padding:3px 10px;border-radius:12px;font-size:12px;font-weight:600}.tip{font-size:12px;color:#888;margin-bottom:10px}</style>
