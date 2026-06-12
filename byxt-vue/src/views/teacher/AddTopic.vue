<template>
<div>
  <h3 style="color:#1565C0;margin-bottom:10px">&#9999; 教师出题</h3>
  <div class="stats"><div class="stat"><b>{{ info.topicCount }}</b><small>已出</small></div><div class="stat"><b>{{ info.remaining }}</b><small>剩余</small></div><div class="stat"><b>{{ info.teacher?.shangxian }}</b><small>上限</small></div></div>
  <van-empty v-if="info.remaining <= 0" description="题目数量已达上限" />
  <div v-else class="card">
    <van-field v-model="tm" label="题目名称" placeholder="请输入题目名称" required />
    <van-field v-model="bz" label="备注" placeholder="可选" type="textarea" rows="3" />
    <van-button type="primary" block round @click="submit" :loading="saving" style="margin-top:14px">提交出题</van-button>
    <p v-if="msg" class="msg">{{ msg }}</p>
  </div>
</div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { showToast } from 'vant'
import api from '../../api'
const tm = ref(''), bz = ref(''), saving = ref(false), msg = ref(''), info = reactive({ topicCount: 0, remaining: 0, teacher: null })
onMounted(async () => { try { const r = await api.get('/teacher/info'); Object.assign(info, r) } catch (e) {} })
async function submit() {
  if (!tm.value) { showToast('请填写题目'); return }
  saving.value = true
  try { const r = await api.post('/teacher/add-topic', { tm: tm.value, bz: bz.value }); msg.value = r.msg; if (r.code === 0) tm.value = bz.value = '' } catch (e) {} finally { saving.value = false }
}
</script>
<style scoped>.stats{display:flex;gap:10px;margin-bottom:12px}.stat{flex:1;background:#fff;border-radius:10px;padding:14px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,.04)}.stat b{display:block;font-size:22px;color:#1E88E5}.stat small{color:#888;font-size:12px}.card{background:#fff;border-radius:12px;padding:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}.msg{text-align:center;color:#E65100;margin-top:8px;font-size:14px}</style>
