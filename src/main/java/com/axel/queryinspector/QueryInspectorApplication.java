package com.axel.queryinspector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
@SpringBootApplication
@ConfigurationPropertiesScan
public class QueryInspectorApplication { public static void main(String[] args) { SpringApplication.run(QueryInspectorApplication.class, args); } }
