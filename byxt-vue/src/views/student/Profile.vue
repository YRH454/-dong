<template>
<van-form @submit="save">
  <van-cell-group inset>
    <van-field v-model="form.xh" label="学号" readonly />
    <van-field v-model="form.xm" label="姓名" required :rules="[{required:true,message:'请输入姓名'}]" />
    <van-field v-model="form.zy" label="专业" />
    <van-field v-model="form.bj" label="班级" />
    <van-field v-model="form.email" label="邮箱" type="email" />
    <van-field v-model="form.phone" label="电话" required :rules="[{required:true,message:'请输入电话'}]" />
    <van-field v-model="form.qq" label="QQ" required :rules="[{required:true,message:'请输入QQ'}]" />
  </van-cell-group>
  <div style="margin:16px">
    <van-button round block type="primary" native-type="submit" :loading="saving">保存信息</van-button>
  </div>
</van-form>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { showToast } from 'vant'
import api from '../../api'
const form = reactive({ xh:'',xm:'',zy:'',bj:'',email:'',phone:'',qq:'' }), saving = ref(false)
onMounted(async () => {
  try { const r = await api.get('/student/profile'); Object.assign(form, r.profile) } catch (e) {}
})
async function save() {
  saving.value = true
  try { await api.post('/student/update-profile', {...form}); showToast({message:'保存成功',icon:'success'}) } catch (e) {}
  saving.value = false
}
</script>
