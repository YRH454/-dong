import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import 'vant/lib/index.css'
import {
  Button, Field, Popup, Picker, Search, Cell, CellGroup,
  Pagination, Empty, Tag, Collapse, CollapseItem, Dialog, Toast, Notify
} from 'vant'

import './style.css'

const app = createApp(App)
app.use(router)
app.use(Button).use(Field).use(Popup).use(Picker).use(Search)
   .use(Cell).use(CellGroup).use(Pagination).use(Empty)
   .use(Tag).use(Collapse).use(CollapseItem)
app.mount('#app')
