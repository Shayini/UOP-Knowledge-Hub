<template>
    <div class="user">
    	<div class="menu-bar">       
            <router-link :to="{ path: '/'+ username + '/myuploads'}">{{username}}</router-link>
            <router-link :to="{ path: '/'+ username + '/home'}">Home</router-link>
            <router-link to="/">Sign Out</router-link>
            <h1>UOP Knowledge Hub</h1>
        </div>

        <div class="welcome">
            <h1>User Details</h1>   <!-- {{$route.params.id}} -->
        </div>

        <!-- <div>
            <router-link :to="{ path: '/'+ username +'/uploads'}">Upload</router-link><br>
            <router-link :to="{ path: '/'+ username +'/myuploads'}">My Uploads</router-link><br>
        </div> -->

        <div class="UserResult">
            <br><br><br>
            <table class="table2" v-if="userDetail && userDetail.length">
                <tr v-for="(element, i) in userDetail" :key="element.id" v-if="element.userName!=='Admin'">
                    <!-- <p><a :href="element.file_path" target="_blank"><img src="/static/img/pdf.png" alt="PDF"></a>{{element.Did}}: {{element.user}}<br> -->
                    <!-- <a :href="element.file_path" target="_blank">{{element.title}}</a><br> -->
                    <!-- {{element.userID}}<br> -->
                    <td><router-link :to="{ path: '/'+ username + '/userdocuments/' + element.userName}">{{element.userName}}</router-link></td>
                    <td>{{element.email}}</td>
                    <td><input class="delete" type="submit" value="Delete" @click="deleteUser(element.userName, i)"></td>
                </tr>
            </table>
        </div>
    </div>
</template>

<script>
import router from '../router'
import axios from 'axios';

export default {
    name: 'UserDetails',
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
            await axios.get('http://localhost:8080/Project/REST-API/userdetails')
                .then(response => {
                    this.userDetail = response.data;
                    console.log(this.userDetail);
                })
                .catch(error => {
                    console.log(error);
                })
        },
        deleteUser: async function(username, i) {
            await axios.delete('http://localhost:8080/Project/REST-API/deleteuser?username=' + username)
            .then(response => {
                console.log(response);
                console.log(i);
                this.userDetail.splice(i, 1);
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
