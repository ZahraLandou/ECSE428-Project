package com.example.nomnomapp.features;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.nomnomapp.repository.CommentRepository;
import com.example.nomnomapp.repository.UserRepository;
import com.example.nomnomapp.repository.RecipeRepository;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions( features = "src/test/resources",
        glue = "com.example.nomnomapp.features")
public class RunCucumberTest {

}