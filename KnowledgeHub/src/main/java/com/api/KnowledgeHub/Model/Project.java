package com.api.KnowledgeHub.Model;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class.
@Table(name="data") //data is the name of the table.
public class Project {

    public Project() {
        super();
    }

    @Id //@Id is used to generate the unique identifier for the objects of persistent class.
    @GeneratedValue(strategy= GenerationType.AUTO)
        //@GeneratedValue generates the value for the column of database table.
        // In case of GenerationType. IDENTITY , value is set by table itself that should be unique.

    //Attributes for the Project Entity
    private Integer projectID;
    private String projectAuthor;
    private String projectName;

    //Getter and Setter Methods for the attributes
    public Integer getProjectID() {

        return projectID;
    }

    public void setProjectID(Integer projectID) {

        this.projectID = projectID;
    }

    public String getProjectAuthor() {

        return projectAuthor;
    }

    public void setProjectAuthor(String projectAuthor) {

        this.projectAuthor = projectAuthor;
    }

    public String getProjectName() {

        return projectName;
    }

    public void setProjectName(String projectName) {

        this.projectName = projectName;
    }
}
