<template>
    <div class="signins">
    	<div class="menu-bar">       
            <router-link to="/">Home</router-link>
            <h1>UOP Knowledge Hub</h1>
        </div>

        <div class="signin_box">
            <table class="table1">
                <tr>
                    <td><h1>Sign In</h1></td>
                </tr>
                <tr>
                    <td><input class="signin" type="text" name="username" id="username" v-model="username" placeholder="User Name" required/></td>
                </tr>
                <tr>
                    <td><input class="signin" type="password" name="password" id="password" v-model="password" placeholder="Password" required/></td>
                </tr>
                <tr>
                    <td><input class="signin" type="button" name="signin" id="signin" v-on:click="signin_fun()" value="Sign In"/></td>
                </tr>
                <tr>
                    <td><router-link to="signup">Create Account</router-link></td>
                </tr>
            </table>
        </div>
    </div>
</template>

<script>
import router from '../router'
import axios from 'axios';

export default {
    name: 'SignIn',
    data() {
        return {
            username: "",
            password: ""
        }
    },
    methods: {
        signin_fun: async function() {
            if(this.username != "" && this.password != "") {
                await axios.get('http://localhost:8080/Project/REST-API/login?userName=' + this.username + '&password=' + this.password)
                .then(response => {
                    console.log(response.data);
                    // console.log(response.data.length);
                    // if(response.data.length >= 1) {
                    if(response.data.userName === this.username && response.data.password === this.password) {
                        alert("Signin Successfull");
                        router.push("/" + this.username + "/myuploads");
                            // router.push({name: 'SignIn'});
                    } else {
                        alert("Username or Password is incorrect");
                    }
                })
                .catch(error => {
                    console.log(error);
                })
            } else {
                alert("A username and password must be present");
            }


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
