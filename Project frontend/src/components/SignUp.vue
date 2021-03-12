<template>
    <div class="signups">
    	<div class="menu-bar">       
            <router-link to="/">Home</router-link>
            <router-link to="/signin">Sign In</router-link>
            <h1>UOP Knowledge Hub</h1>
        </div>

        <div class="signup_box">
            <table class="table1">
                <tr>
                    <td><h1>Sign Up</h1></td>
                </tr>
                <tr>
                    <td><input class="signup" type="text" name="username" id="username" placeholder="User Name" v-model.trim="username" required/></td>
                </tr>
                <tr>
                    <td><input class="signup" type="email" name="email" id="email" placeholder="E-mail" v-model.trim="email" required/></td>
                </tr>
                <tr>
                    <td><input class="signup" type="password" name="password" id="password" placeholder="Password" v-model.trim="password" required/></td>
                </tr>
                <tr>
                    <td><input class="signup" type="password" name="repassword" id="password" placeholder="Retype Password" v-model.trim="repassword" required/></td>
                </tr>
                <tr>
                    <td><input class="signup" type="button" name="signin" id="signup" value="Sign Up" v-on:click="signup_fun()" /></td>
                </tr>
            </table>
            
        </div>
    </div>
</template>

<script>
import router from '../router'
import axios from 'axios';

export default {
    name: 'SignUp',
    data() {
        return {
            username: '',
            email: '',
            password: '',
            repassword: '',
            resp: ''
}
    },
    methods: {
        signup_fun: async function(){
            if(this.password!==this.repassword){
                alert("Signup Unsuccessfull\nPassword does not match");
            }else if(this.username && this.email && this.password && this.repassword && this.password===this.repassword){

                let currentObject = this;
                await axios.post('http://localhost:8080/Project/REST-API/signup', {
                    userName: this.username,
                    email: this.email,
                    password: this.password,
                    status: 'user'
                })
                .then(function (response) {
                    currentObject.resp = response.data;
                    console.log(response);
                })
                .catch(function (error) {
                    currentObject.resp = error;
                });

                alert("Signup Successfull");
                router.push("/signin");
                // router.push({name: 'SignIn'});
            }else{
                alert("Signup Unsuccessfull\nPlease fill the required fields");
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
