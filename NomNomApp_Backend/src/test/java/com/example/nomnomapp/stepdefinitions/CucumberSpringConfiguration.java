package com.example.nomnomapp.stepdefinitions;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@CucumberContextConfiguration
@SpringBootTest
@ComponentScan(basePackages = "com.example.nomnomapp")
public class CucumberSpringConfiguration {

}