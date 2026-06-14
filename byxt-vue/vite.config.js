import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  base: '/byxt/',
  server: { host: '0.0.0.0', port: 3000, proxy: { '/byxt': 'http://localhost:8088' } }
})
