package com.api.KnowledgeHub.Controller;

import com.api.KnowledgeHub.Model.Project;
import com.api.KnowledgeHub.Repository.Project_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin(origins = "http://localhost:8081")
@RestController //Encapsulating @Controller + @ResponseBody annotations,@RestController means that this class is a Controller.
@RequestMapping(path="/Project/REST-API") //This means URL's start with /Project/REST-API (after Application path).
public class ProjectController {

    @Autowired // This means to get the bean called Project_Repository which is auto-generated by Spring, we will use it to handle the data.
    private Project_repository projectRepository;

    @GetMapping(value="/read") // Map ONLY GET Requests and means URL's start with /read (after Application path).
    @ResponseBody // @ResponseBody means the returned Integer is the response, not a view Id.
    public Iterable findAll() {

        return projectRepository.findAll(); // This returns a JSON or XML with the users.

    }

    @GetMapping(value="/author/{projectAuthor}") //Map ONLY GET Requests and means URL's start with /author/{projectAuthor}(after Application path).
    public Optional findByProjectAuthor(@PathVariable String projectAuthor) {

        //@PathVariable is used to extract data right from the URI.It is used to retrieve values from URI itself.
        return projectRepository.findByProjectAuthor(projectAuthor);
        //Java Optional can help in writing neat code without using too many null checks.

    }

    @GetMapping(value="/name/{projectName}") // Map ONLY GET Requests and means URL's start with /name/{projectName}(after Application path).
    public Optional findByProjectName(@PathVariable String projectName) {

        //@PathVariable is used to extract data right from the URI.It is used to retrieve values from URI itself.
        return projectRepository.findByProjectName(projectName);
        //Java Optional can help in writing neat code without using too many null checks.

    }

    @GetMapping(value="/read/{projectID}") // Map ONLY GET Requests and means URL's start with /read/{projectID} (after Application path).
    public Project readId(@PathVariable("projectID") Integer projectID) {

        //@PathVariable is used to extract data right from the URI.It is used to retrieve values from URI itself.
        //Java Optional is a container object which may or may not contain a non-null value.
        //This Java Optional can help in writing neat code without using too many null checks.
        Optional<Project> optional1 = projectRepository.findByProjectID(projectID);
        return optional1.get(); //Inherited get() method is used here.

    }

    @PostMapping("/add") // Map ONLY POST Requests and means URL's start with /add (after Application path).
    @ResponseBody // @ResponseBody means the returned Integer is the response, not a view Id.
    public Project add(@RequestBody Project project) { //@RequestBody annotation is used to bind the HTTP request/response body with a domain object in method parameter or return type.

            projectRepository.save(project); //Inherited save() method is used here.
            return project;

    }

    @PostMapping("/insert") // Map ONLY POST Requests and means URL's start with /add (after Application path).
    @ResponseBody // @ResponseBody means the returned Integer is the response, not a view Id.
    public String addURL(@RequestParam Integer projectID,@RequestParam String projectAuthor,@RequestParam String projectName) {
        //@RequestParam annotation is used to obtain a parameter from the URL.

        Project project = new Project(); //Instantiating the Project class.
        project.setProjectID(projectID); //Invoking the setProjectID() method.
        project.setProjectAuthor(projectAuthor); //Invoking the setProjectAuthor() method.
        project.setProjectName(projectName); //Invoking the setProjectName() method.
        projectRepository.save(project); //Inherited save() method is used here.
        return "Response Is Successfully Added"; //Method addURL returning the String.

    }

}


