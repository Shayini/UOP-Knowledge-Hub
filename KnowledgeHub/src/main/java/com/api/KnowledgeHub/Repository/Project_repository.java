package com.api.KnowledgeHub.Repository;

import com.api.KnowledgeHub.Model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/*
  In a typical Java application, you’d expect to write a class that implements ProjectRepository.
  But that’s what makes Spring Data JPA so powerful: You don’t have to write an implementation of the repository interface.
*/

// This will be AUTO IMPLEMENTED by Spring into a Bean called ProjectRepository.
// CRUD refers Create, Read, Update, Delete.

public interface Project_repository extends CrudRepository<Project,String>

        /*By extending CrudRepository, ProjectRepository inherits several methods for working with Project persistence,
         including methods for saving, deleting, and finding Project entities.*/

        //Spring Data JPA also allows you to define other query methods.
        // In this case of ProjectRepository, this is shown with findByProjectId(),findByProjectAuthor() and findByProjectName() methods.

{

    Optional<Project> findByProjectID(Integer projectID);
    Optional<Project> findByProjectAuthor(String projectAuthor);
    Optional<Project> findByProjectName(String projectName);

}
