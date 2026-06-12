import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/student', component: () => import('../views/layouts/StudentLayout.vue'),
    children: [
      { path: '', component: () => import('../views/student/Topics.vue') },
      { path: 'topics', component: () => import('../views/student/Topics.vue') },
      { path: 'my-result', component: () => import('../views/student/MyResult.vue') },
      { path: 'profile', component: () => import('../views/student/Profile.vue') },
      { path: 'password', component: () => import('../views/student/Password.vue') },
      { path: 'view-topics', component: () => import('../views/student/ViewTopics.vue') }
    ]
  },
  {
    path: '/teacher', component: () => import('../views/layouts/TeacherLayout.vue'),
    children: [
      { path: '', component: () => import('../views/teacher/AddTopic.vue') },
      { path: 'add', component: () => import('../views/teacher/AddTopic.vue') },
      { path: 'batch', component: () => import('../views/teacher/BatchTopic.vue') },
      { path: 'my-topics', component: () => import('../views/teacher/MyTopics.vue') },
      { path: 'profile', component: () => import('../views/teacher/Profile.vue') },
      { path: 'password', component: () => import('../views/teacher/Password.vue') }
    ]
  },
  {
    path: '/admin', component: () => import('../views/layouts/AdminLayout.vue'),
    children: [
      { path: '', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'teachers', component: () => import('../views/admin/Teachers.vue') },
      { path: 'students', component: () => import('../views/admin/Students.vue') },
      { path: 'topics', component: () => import('../views/admin/Topics.vue') },
      { path: 'settings', component: () => import('../views/admin/Settings.vue') },
      { path: 'password', component: () => import('../views/admin/Password.vue') }
    ]
  }
]

const router = createRouter({ history: createWebHashHistory(), routes })

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  if (to.path !== '/login' && !token) return next('/login')
  if (to.path.startsWith('/student') && role !== 'student') return next('/login')
  if (to.path.startsWith('/teacher') && role !== 'teacher') return next('/login')
  if (to.path.startsWith('/admin') && role !== 'admin') return next('/login')
  next()
})

export default router
