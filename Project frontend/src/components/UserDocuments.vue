<template>
    <div class="myuploads">
    	<div class="menu-bar">       
            <router-link :to="{ path: '/'+ username + '/myuploads'}">{{username}}</router-link>
            <router-link :to="{ path: '/'+ username + '/home'}">Home</router-link>
            <router-link to="/">Sign Out</router-link>
            <h1>UOP Knowledge Hub</h1>
        </div>

        <div class="user_upload">
            <div>
                <h3>User Name: {{subusername}}</h3>
                <h3>Email: {{subuserDetail.email}}</h3>
                <input class="delete" type="submit" value="Delete" @click="deleteUser(subusername)">
            </div>

            <div class="MyUploadsResult">
                <table class="table3" v-if="output && output.length">
                    <tr v-for="(element, i) in output" :key="element.id">
                        <td><a :href="element.file_path" target="_blank"><img src="/static/img/pdf.png" alt="PDF"></a></td>
                        <td>Title: {{element.title}}<br>
                        Author: {{element.author}}<br>
                        Document Type: {{element.documentType}}<br>
                        <p>{{element.data.substring(0,400)}}................</p></td>
                        <td><input class="delete" type="submit" value="Delete" @click="deleteData(element.Did, i)"></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>


<script>
import router from '../router'
import axios from 'axios';

export default {
    name: 'UserDocuments',
    data () {
        return {
            username: '',
            subusername: '',
            subuserDetail: [],
            files: [],
            hits: [],
            output: []
        }
    },
    created() {
        this.username = this.$route.params.id;
        this.subusername = this.$route.params.user;
        this.getData();
        
    },
    methods: {
        getData: async function() {
            await axios.get('http://localhost:8080/Project/REST-API/admin?userName=' + this.subusername)
            .then(response => {
                this.subuserDetail = response.data;
                console.log(this.userDetail.status);
            })
            .catch(error => {
                console.log(error);
            })
            
            await axios.get('http://localhost:8080/Project/REST-API/search?field=user&value=' + this.subusername)
            .then(response => {
                this.hits = response.data
                console.log(this.hits);
                for(let i=0; i<this.hits.length; i++) {
                    this.files[i] = this.hits[i].sourceAsMap;
                    this.files[i].Did = this.hits[i].id;
                }
            })
            .catch(error => {
            console.log(error);
            })

            this.output = this.files;
            console.log(this.output);
        },
        deleteData: async function(id, i) {
            await axios.delete('http://localhost:8080/Project/REST-API/delete?id=' + id)
            .then(response => {
                console.log(response);
                console.log(i);
                this.output.splice(i, 1);
            })
        },
        deleteUser: async function(username) {
            await axios.delete('http://localhost:8080/Project/REST-API/deleteuser?username=' + username)
            .then(response => {
                console.log(response);
            })
            router.push({
                path: '/' + this.username + '/userdetails'
            });
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
