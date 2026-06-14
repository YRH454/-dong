import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import 'vant/lib/index.css'
import {
  Button, Field, Popup, Picker, Search, Cell, CellGroup,
  Pagination, Empty, Tag, Collapse, CollapseItem,
  NavBar, Loading, Icon, Image, PullRefresh, List,
  Radio, RadioGroup, Form, NoticeBar, Swipe, SwipeItem, Tab, Tabs
} from 'vant'

import './style.css'

const app = createApp(App)
app.use(router)
app.use(Button).use(Field).use(Popup).use(Picker).use(Search)
   .use(Cell).use(CellGroup).use(Pagination).use(Empty)
   .use(Tag).use(Collapse).use(CollapseItem)
   .use(NavBar).use(Loading).use(Icon).use(Image)
   .use(PullRefresh).use(List)
   .use(Radio).use(RadioGroup).use(Form).use(NoticeBar)
   .use(Swipe).use(SwipeItem).use(Tab).use(Tabs)
app.mount('#app')
