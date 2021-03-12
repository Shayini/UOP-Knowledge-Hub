import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Home2 from '@/components/Home2'
import Search from '@/components/search'
import SignIn from '@/components/SignIn'
import SignUp from '@/components/SignUp'
import User from '@/components/User'
import Uploads from '@/components/Uploads'
import MyUploads from '@/components/MyUploads'
import UserDetails from '@/components/UserDetails'
import UserDocuments from '@/components/UserDocuments'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/:id/home',
      name: 'Home2',
      component: Home2
    },
    {
      path: '/search',
      name: 'Search',
      component: Search
    },
    {
      path: '/signin',
      name: 'SignIn',
      component: SignIn
    },
    {
      path: '/signup',
      name: 'SignUp',
      component: SignUp
    },
    {
      path: '/:id',
      name: 'User',
      component: User
    },
    {
      path: '/:id/uploads',
      name: 'Uploads',
      component: Uploads
    },
    {
      path: '/:id/myuploads',
      name: 'MyUploads',
      component: MyUploads
    },
    {
      path: '/:id/userdetails',
      name: 'UserDetails',
      component: UserDetails
    },
    {
      path: '/:id/userdocuments/:user',
      name: 'UserDocuments',
      component: UserDocuments
    }
  ]
})
