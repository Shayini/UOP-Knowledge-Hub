package com.api.KnowledgeHub.Model;

public class Document {
    public Document() {
        super();
    }


//    private Integer userID;
    private String username;
    private String author;
    private String title;
    private String docType;
    private String description;
    private String file_name;
    private String file_path;
    private String text;

//    public Integer getUserID() {
//
//        return userID;
//    }
//
//    public void setUserID(Integer userID) {
//
//        this.userID = userID;
//    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getDocType() {

        return docType;
    }

    public void setDocType(String docType) {

        this.docType = docType;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getFile_name() {

        return file_name;
    }

    public void setFile_name(String file_name) {

        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
