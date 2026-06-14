<template>
<div>
  <h3 style="color:#1565C0;margin-bottom:10px">&#128230; 批量出题</h3>
  <div class="stats"><div class="stat"><b>{{ info.topicCount }}</b><small>已出</small></div><div class="stat"><b>{{ info.remaining }}</b><small>剩余</small></div></div>
  <van-empty v-if="info.remaining <= 0" description="已达上限" />
  <div v-else class="card">
    <van-field v-model="count" label="出题数量" readonly is-link @click="showCt=true" :placeholder="info.remaining+' 题可选'" />

    <div class="topic-list">
      <van-field v-for="(t, i) in topics" :key="i" v-model="topics[i]" :label="'题目 '+(i+1)" :placeholder="'请输入第'+(i+1)+'个题目名称'" required :rules="[{required:true,message:'请填写题目'}]" />
    </div>

    <van-field v-model="bz" label="统一备注" placeholder="可选（所有题目共用）" type="textarea" rows="2" />
    <van-button type="primary" block round @click="submit" :loading="saving" style="margin-top:14px">批量出题 ({{ count }}题)</van-button>
    <p v-if="msg" class="msg">{{ msg }}</p>
  </div>
  <van-popup v-model:show="showCt" round position="bottom">
    <van-picker :columns="countOptions" @confirm="c => { count = c.selectedValues[0]; showCt = false }" @cancel="showCt=false" />
  </van-popup>
</div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { showToast } from 'vant'
import api from '../../api'
const count = ref(1), bz = ref(''), showCt = ref(false), saving = ref(false), msg = ref(''), info = reactive({ topicCount: 0, remaining: 0 })
const topics = reactive([''])
const countOptions = computed(() => Array.from({ length: info.remaining }, (_, i) => ({ text: (i + 1) + ' 题', value: i + 1 })))

watch(count, (n) => {
  while (topics.length < n) topics.push('')
  while (topics.length > n) topics.pop()
})

onMounted(async () => { try { const r = await api.get('/teacher/info'); Object.assign(info, r); if (info.remaining > 0) { count.value = Math.min(1, info.remaining); topics.splice(0); topics.push('') } } catch (e) {} })

async function submit() {
  const emptyIdx = topics.findIndex(t => !t || !t.trim())
  if (emptyIdx >= 0) { showToast('请填写第' + (emptyIdx+1) + '个题目名称'); return }
  saving.value = true; msg.value = ''
  let ok = 0, fail = 0
  for (const name of topics) {
    try {
      const r = await api.post('/teacher/add-topic', { tm: name.trim(), bz: bz.value })
      if (r.code === 0) ok++; else fail++
    } catch (e) { fail++ }
  }
  msg.value = '成功 ' + ok + ' 个' + (fail > 0 ? '，失败 ' + fail + ' 个' : '')
  if (ok > 0) { bz.value = ''; count.value = 1; const r = await api.get('/teacher/info'); Object.assign(info, r) }
  saving.value = false
}
</script>

<style scoped>
.stats{display:flex;gap:10px;margin-bottom:12px}.stat{flex:1;background:#fff;border-radius:10px;padding:14px;text-align:center;box-shadow:0 1px 3px rgba(0,0,0,.04)}.stat b{display:block;font-size:22px;color:#1E88E5}.stat small{color:#888;font-size:12px}
.card{background:#fff;border-radius:12px;padding:8px;box-shadow:0 1px 4px rgba(0,0,0,.04)}
.topic-list{margin:4px 0}.topic-list :deep(.van-field){margin-bottom:4px;border-radius:10px}
.msg{text-align:center;color:#1565C0;margin-top:8px;font-size:14px}
</style>
