package com.example.nomnomapp.features;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions( features = "src/test/resources",
        glue = "com.example.nomnomapp.features")
public class RunCucumberTest {

}
