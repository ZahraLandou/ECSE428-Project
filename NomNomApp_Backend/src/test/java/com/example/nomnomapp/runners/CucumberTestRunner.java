package com.example.nomnomapp.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "json:target/cucumber.json"},
        features = "src/test/resources",
        glue = "com.example.nomnomapp.stepdefinitions")
public class CucumberTestRunner {

}