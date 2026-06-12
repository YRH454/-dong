<template>
<div>
  <h3 style="color:#E65100;margin-bottom:10px">&#128100; 教师管理</h3>
  <van-empty v-if="!list.length" description="无教师数据" />
  <van-cell v-for="t in list" :key="t.id" :title="t.xm" :label="'工号：'+t.gh+' | 职称：'+(t.zhicheng||'')+' | 上限：'+t.shangxian">
    <template #right-icon>
      <van-button size="mini" type="warning" @click="resetPwd(t.gh)" style="margin-right:4px">重置密码</van-button>
      <van-button size="mini" type="danger" @click="delT(t.id)">删除</van-button>
    </template>
  </van-cell>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { showToast, showConfirmDialog } from 'vant'
import api from '../../api'
const list = ref([])
onMounted(async () => { try { const r = await api.get('/admin/teacher-list'); list.value = r.list || [] } catch (e) {} })
async function resetPwd(gh) { try { await showConfirmDialog({title:'确认',message:'重置 '+gh+' 的密码为工号？'}) } catch { return }; try { await api.post('/admin/reset-teacher-pwd',{gh}); showToast('已重置') } catch (e) {} }
async function delT(id) { try { await showConfirmDialog({title:'确认删除',message:'删除该教师？'}) } catch { return }; try { await api.post('/admin/delete-teacher',{id}); showToast('已删除'); list.value = list.value.filter(t => t.id !== id) } catch (e) {} }
</script>
