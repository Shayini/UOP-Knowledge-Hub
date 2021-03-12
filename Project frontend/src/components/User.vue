<template>
    <div class="user">
    	<div class="menu-bar">       
            <router-link :to="{ path: '/'+ username + '/home'}">Home</router-link>
            <router-link to="/">Sign Out</router-link>
            <h1>UOP Knowledge Hub</h1>
        </div>

        <div class="welcome">
            <h1>Welcome {{username}}</h1>   <!-- {{$route.params.id}} -->
        </div>

        <div>
            <router-link :to="{ path: '/'+ username +'/uploads'}">Upload</router-link><br>
            <router-link :to="{ path: '/'+ username +'/myuploads'}">My Uploads</router-link><br>
            <router-link :to="{ path: '/'+ username +'/userdetails'}"><a v-if="userDetail.status === 'admin'">Users</a></router-link>     
        </div>
    </div>
</template>

<script>
import router from '../router'
import axios from 'axios';

export default {
    name: 'User',
    data () {
        return {
            username: '',
            userDetail: []
        }
    },
    created() {
        this.username = this.$route.params.id;
        this.getUserDetail();
    },
    methods: {
        getUserDetail: async function() {
            await axios.get('http://localhost:8080/Project/REST-API/admin?userName=' + this.username)
            .then(response => {
                this.userDetail = response.data;
                console.log(this.userDetail.status);
            })
            .catch(error => {
                console.log(error);
            })
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
    body {
        font-family: Georgia;
        font-weight: bold;
        background-color: rgb(224, 224, 224);
        background-image: linear-gradient(rgba(255,255,255,0.7),rgba(255,255,255,0.7)), url('/static/img/7.jpg');
        background-size: cover;
    }
</style>
