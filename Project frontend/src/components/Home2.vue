<template>
    <div class="home">
        <div class="menu-bar">
            <router-link :to="{ path: '/'+ username + '/myuploads'}">{{username}}</router-link>
            <router-link to="/">Sign Out</router-link>
        </div>

        <div class="main">
            <h1>UOP Knowledge Hub</h1>
            <form @submit.prevent="search()">
                <input id="textbox" type="text"  list="dialog" autocomplete="off" name="search-text" v-on:keyup="display()" placeholder="Search..." v-model.trim="input" >
                <datalist id="dialog"></datalist>
                <input id="searchbox" type="submit" value="Search"><br>
            </form>
        </div>
        <div class="radio">
                <input type="radio" name="Field" v-model="search_field" value="author" class="radio1"> Author
                <input type="radio" name="Field" v-model="search_field" value="title" class="radio1"> Title
                <input type="radio" name="Field" v-model="search_field" value="data" class="radio1" checked> Documents
        </div>
    </div>
</template>

<script>
import router from '../router'
import axios from 'axios';

export default {
    name: 'Home2',
    data() {
        return {
            input: "",
            username: '',
            // list: ['anisette','anisettes','asset','assets','audiocassette','audiocassettes','avoset','avosets','backset','backsets','basset','basseted','basseting','bassets','bassetted','bassetting','beset','besetment','besetments','besets','besetter','besetters','besetting','boneset','bonesets','bonesetter','bonesetters'],
            hits:[],
            array: [],
            search_field: 'data',
            position: ''
        }
    },
    created() {
        this.username = this.$route.params.id;        
    },
    methods: {
        // display: function() {
        //     //this.input = document.getElementById("textbox").value.toLowerCase();
        //     var dialog = document.getElementById("dialog");

        //     document.getElementById('dialog').innerHTML = ''; 
        //     this.hits = [];

        //     for(let i=0 ; i<this.list.length ;i++){
        //         if(this.list[i].toLowerCase().indexOf(this.input) == 0){
        //             this.hits.push(this.list[i]);
        //         }
        //     }
        //     if(this.input==""){
        //         this.hits = [];
        //     }
        //     this.hits.forEach(function(element) {
        //         var option = document.createElement('option');
        //         option.value = element;
        //         dialog.appendChild(option);
        //     });
        // }
        display: async function() {
            let currentObject = this;

            //this.input = document.getElementById("textbox").value.toLowerCase();
            var dialog = document.getElementById("dialog");

            document.getElementById('dialog').innerHTML = ''; 
            this.hits = [];
            this.hits2 = [];
            this.array = [];

            await axios.get('http://localhost:8080/Project/REST-API/search?field=' + this.search_field + '&value=' + this.input)
            .then(response => {
                // console.log(response.data);
                this.hits = response.data;
                this.hits2 = response.data;
                console.log(this.hits);
                let count = 0;
                for(let i=0; i<this.hits.length; i++) {
                    for(let j=0; j<this.hits.length; j++) {
                        if(this.hits[i].sourceAsMap[this.search_field]===this.hits[j].sourceAsMap[this.search_field]) {
                            count++;
                            if(count>1) {
                                this.hits2.splice(j,1);
                            }
                        }
                    }
                    count=0;
                }
                console.log(this.hits);
                console.log(this.hits2);
                for(let i=0; i<this.hits2.length; i++) {
                    this.array[i] = this.hits2[i].sourceAsMap;
                    this.array[i].Did = this.hits2[i].id;
                    this.array[i].position = this.array[i].data.indexOf(" " + this.input);
                    console.log(this.array[i].data.substring(this.array[i].data.indexOf(this.input), this.array[i].data.indexOf(this.input)+20));
                }
                // console.log(this.array[0][this.search_field]);
                console.log(response);
            })
            .catch(error => {
                console.log(error);
            })

            if(this.input==""){
                this.hits = [];
                this.hits2 = [];
                this.array = [];
            }

            this.array.forEach(function(element) {
                var option = document.createElement('option');
                if(currentObject.search_field=='data'){
                    option.value = element[currentObject.search_field].substring(element.position, element.position+100);
                }else{
                    option.value = element[currentObject.search_field];
                }
                dialog.appendChild(option);
            });
        },
        search: async function() {
            let currentObject = this;

            //this.input = document.getElementById("textbox").value.toLowerCase();
            var dialog = document.getElementById("dialog");

            document.getElementById('dialog').innerHTML = ''; 
            this.hits = [];
            this.array = [];

            await axios.get('http://localhost:8080/Project/REST-API/search?field=' + this.search_field + '&value=' + this.input)
            .then(response => {
                console.log(response);
                this.hits = response.data;
                console.log(this.hits);
                
                for(let i=0; i<this.hits.length; i++) {
                    this.array[i] = this.hits[i].sourceAsMap;
                    this.array[i].Did = this.hit[i].id;
                    this.array[i].position = this.array[i].data.indexOf(this.input);
                    console.log(this.array[i].data.substring(this.array[i].data.indexOf(this.input), this.array[i].data.indexOf(this.input)+20));
                }
                console.log(this.array);
            })
            .catch(error => {
                console.log(error);
            })

            if(this.input==""){
                this.hits = [];
                this.array = [];
            }

            this.array.forEach(function(element) {
                var option = document.createElement('option');
                if(currentObject.search_field=='data'){
                    option.value = element[currentObject.search_field].substring(element.position, element.position+100);
                }else{
                    option.value = element[currentObject.search_field];
                }
                dialog.appendChild(option);
            });

            router.push({
                path: "/search",
                query: {array: this.array}
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
        background-image: linear-gradient(rgba(255,255,255,0.2),rgba(255,255,255,0.2)), url('/static/img/7.jpg');
        background-size: cover;
    }
</style>
