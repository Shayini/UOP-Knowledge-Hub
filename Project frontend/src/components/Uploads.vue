<template>
    <div class="uploads">
    	<div class="menu-bar">       
            <router-link :to="{ path: '/'+ username + '/myuploads'}">{{username}}</router-link>
            <router-link :to="{ path: '/'+ username + '/home'}">Home</router-link>
            <router-link to="/">Sign Out</router-link>
            <h1>UOP Knowledge Hub</h1>
        </div>

        <!-- <div v-if="message">
            <div class="required">*{{message}}</div>
        </div> -->

        <div class="upload">
            <form @submit.prevent="formSubmit()" enctype="multipart/form-data">
                <table class="table1">
                    <tr>
                        <td>
                            Author<span class=required>*</span> :
                        </td>
                        <td>
                            <input class="upbox" type="text" name="authorName" id="authorName" v-model.trim="author" required>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Title<span class=required>*</span> :
                        </td>
                        <td>
                            <input class="upbox" type="text" name="reserchName" id="researchName" v-model.trim="title" required>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Document Type<span class=required>*</span> :
                        </td>
                        <td>
                            <input list="tags" class="upbox" type="text" name="tag" id="tag" v-model.trim="doctype" required>
                            <datalist id="tags">
                                <option value="Research Papers"></option>
                                <option value="Project Proposals"></option>
                            </datalist>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Description :
                        </td>
                        <td>
                            <textarea class="disbox" type="text" name="discription" id="discription" v-model.trim="discription"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Choose a file<span class=required>*</span> :
                        </td>
                        <td>
                            <input class="upfile" type="file" ref="file" name="file1" accept="application/pdf" @change="onFileSelected()" required/><br>
                            <h5 class="required">*Only PDFs are allowed</h5>
                            <h5 class="required" v-if="file_size > max_file_size">*Too large. Maximum size is {{max_file_size/1000000}} Mb</h5>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input class="upsubmit" type="submit" value="Submit"/>
                            <!-- <a v-if="file_path" :href="file_path">Link</a> -->
                        </td>
                    </tr>
                </table>   
            </form>
        </div>
    </div>
</template>

<script>
import router from '../router'
import axios from 'axios';

export default {
    name: 'Uploads',
    data () {
        return {
            username: '',
            author: '',
            title: '',
            doctype: '',
            discription: '',
            file: '',
            file_name: '',
            file_status: '',
            file_size: '',
            file_path: '',
            text: '',
            max_file_size: '5000000',//5000Kb - 5Mb
            resp: '',
            // message: '',
            error: false
        }
    },
    created() {
        this.username = this.$route.params.id;
    },
    methods: {
        onFileSelected: function(event) {
            const file = this.$refs.file.files[0];
            this.file = file;
            this.file_name = file.name;
            this.file_size = file.size;
            // console.log(this.file.size);
            this.error = false;
            // this.message = '';            
        },
        formSubmit: async function() {
            let currentObject = this;

            if(this.file_size <= this.max_file_size) {
                const formData = new FormData();
                formData.append('file', this.file);
                try {
                    await axios.post('http://localhost:8080/Project/REST-API/uploadFile', formData)
                    .then(function (response){
                        currentObject.file_status = response.status;
                        // currentObject.file_path = response.data.file.path;
                        console.log(response.data);
                        //console.log(response.file.filename);
                        currentObject.file_path = response.data.fileDownloadUri;
                        currentObject.text = response.data.text;
                        console.log(currentObject.text);

                    });
                    // this.message = 'File has been uploaded';
                    this.file = '';
                    this.error = false;
                } catch(err) {
                    console.log(err);
                    // this.message = 'Something went wrong';
                    this.error = true;
                }

                if(currentObject.file_status >= 200 && currentObject.file_status < 300) {
                    await axios.post('http://localhost:8080/Project/REST-API/upload', {
                        username: this.username,
                        author: this.author,
                        title: this.title,
                        docType: this.doctype,
                        description: this.discription,
                        file_name: this.file_name,
                        file_path: this.file_path,
                        text: this.text
                    })
                    .then(function (response) {
                        currentObject.resp = response.data;
                        console.log(response);
                        console.log("Submitted!!!");
                        alert("Upload Successfull");
                        router.push('/' + currentObject.username + '/myuploads');
                    })
                    .catch(function (error) {
                        currentObject.resp = error;
                    });
                } else {
                    this.message = 'There is an error in File upload';
                    alert("File name already exist");
                    this.error = true;
                }
            } else {
                alert('Too large. Maximum size is ' + this.max_file_size/1000000 + ' Mb');
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
