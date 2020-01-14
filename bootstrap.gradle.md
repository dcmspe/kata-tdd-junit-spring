# Bootstrap

## Step 1: initialize with springboot

```bash
curl https://start.spring.io/starter.tgz \
-d dependencies=web,lombok \
-d language=java \
-d type=gradle-project \
-d baseDir=bootstrap-cucumber-spring \
-d groupId=com.newlight77 \ 
-d artifactId=bootstrap-cucumber-spring \
| tar -xzvf -
```

## Step 2: add dependencies

```groovy
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    testCompileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'
    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }

    compile 'com.fasterxml.jackson.core:jackson-databind:2.10.0'

    testCompile("org.assertj:assertj-core:3.11.1")
    testCompile 'com.sun.xml.bind:jaxb-osgi:2.3.2'
    testCompile("io.rest-assured:rest-assured:3.1.1") {
        exclude module: "com.sun.xml.bind:jaxb-osgi"
    }
```

## Step 3: Adding plugins

```groovy
plugins {
    id 'org.springframework.boot' version '2.2.0.RELEASE'
    id 'io.spring.dependency-management' version '1.0.8.RELEASE'
    id 'java'
    id 'jacoco'
    id "org.sonarqube" version "2.8"
}

apply from: './jacoco-config.gradle'
apply from: './sonarqube-conf.gradle'

...

test {
    useJUnit()
}

test.finalizedBy jacocoTestReport
test.finalizedBy jacocoTestCoverageVerification

```

## Step 4: Jaoco for Code Coverage

Add jacoco specific configuration at `./jacoco-config.gradle`.

```groovy
apply plugin: 'jacoco'

test {
    jacoco {
        enabled = true
        destinationFile = file("${buildDir}/jacoco/${project.name}.exec")
        classDumpDir = file("${buildDir}/jacoco/classpathdumps")
        classDumpDir = null
        includes = ['com.newlight77.bootstrap.*']
        excludes = ['com.newlight77.bootstrap.model.*', 'com.newlight77.bootstrap.exception.*']
        address = "localhost"
        port = 9000
    }
}

jacocoTestReport {

    group = "Reporting"
    description = "Generate Jacoco coverage reports"
    executionData = files("${buildDir}/jacoco/${project.name}.exec")
//    executionData = fileTree(dir: project.projectDir, includes: ["**/*.exec", "**/*-it.exec"])
    executionData.each { File file ->
        println file.name
    }

    reports {
        xml.enabled false
        csv.enabled false
        html.destination file("${buildDir}/jacoco/Html")
    }

    classDirectories = fileTree(
        dir: "${project.buildDir}/classes/java/main",
        excludes: ['**/model/*.class', '**/exception/*.class']
    )

    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: ['com.newlight77.bootstrap.model.*', 'com.newlight77.bootstrap.exception.*'])
        }))
    }
}

jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = false
            element = 'CLASS'
            includes = ['com.newlight77.*']
            excludes = ['com.newlight77.bootstrap.model.*',
                        'com.newlight77.bootstrap.exception.*']

            limit {
                counter = 'LINE'
                value = 'TOTALCOUNT'
//                value = 'COVEREDRATIO'
//                minimum = 0.8
                maximum = 0.3
            }
        }
    }
}

```

## Step 5: Jaoco and SonarQube for Code Coverage

Add SonarQube specific configuration at `./sonarqube-conf.gradle`.

```groovy
apply plugin: 'org.sonarqube'

sonarqube {
    properties {
        property 'sonar.host.url', 'http://localhost:9000'
        property "sonar.projectName", "Bootstrap-Cucumber-Spring"
        property "sonar.projectKey", "Bootstrap-Cucumber-Spring"
        property 'sonar.java.source', '8'
        property 'sonar.sourceEncoding', 'UTF-8'
        property "sonar.language", "java"
        property "sonar.login", "admin"
        property "sonar.password", "admin"

        property 'sonar.java.binaries', 'build/classes/java/main'
        property 'sonar.java.test.binaries', 'build/classes/java/test'
        property 'sonar.junit.reportsPath', 'build/test-results/test/binary'
        property 'sonar.jacoco.reportPath', fileTree(dir: project.projectDir, includes: ['**/*.exec'])
        property 'sonar.jacoco.itReportPath', fileTree(dir: project.projectDir, includes: ['**/*-it.exec'])
        property 'sonar.java.coveragePlugin', 'jacoco'
        property 'sonar.exclusions', '**/model/*.java,**/exception/*.java'
    }
}
```

## Step 4: add Cucumber dependencies

```groovy
    testCompile 'io.cucumber:cucumber-java:4.8.0'
    testCompile 'io.cucumber:cucumber-junit:4.8.0'
    testCompile 'io.cucumber:cucumber-spring:4.8.0'
    testCompile 'de.monochromata.cucumber:reporting-plugin:3.0.9'
```

## Step 6: add Cucumber runner and StepRefs

src/java/test/cucumber/RunCucumberTest.java :

```java
package cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {
            "pretty",
            "json:target/cucumber/cucumber.json",
            "json:build/cucumber/cucumber.json",
            "de.monochromata.cucumber.report.PrettyReports:build/cucumber/monochromata",
            "de.monochromata.cucumber.report.PrettyReports:target/cucumber/monochromata"
    },
    features = {
            "features"
    },
    glue = {
        "cucumber/stepdefs",
        "cucumber/config"
    }
)
public class RunCucumberTest {
}
```

## Step 7: Features

Features will reside under `./feactures`.

```sh
mkdir ./features
```

__./features/bootstrap.feature__ :

Example:

```feature
Feature: Withdrawal from an ATM
  A client wants to withdrawal money at an ATM

  @Withdraw
  Scenario Outline: Withdrawal money from an ATM that contains enough money
    Given a client having a valid account
    And the account balance is <init_balance> euros
    And the card is valid
    And the ATM contains enough money
    When the account holder requests <money> euros
    Then the ATM should dispense <money> euros
    And the account balance should be <new_balance> euros
    And the card should be returned

    Examples:
      | init_balance | money   | new_balance |
      | 100.0        | 20.0    | 80.0        |
```

## Step 8: Cucumber & Spring integration

__./src/test/java/cucumber/config/SpringIntegrationTest__ :

```java
package cucumber.config;

import DemoApplication;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpringIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(SpringIntegrationTest.class);

    @Before
    public void setUp() {
        LOG.info("------------- setup -------------");
    }

    @After
    public void tearDown() {
        LOG.info("------------- teardown -------------");

    }

}
```

## Step 9: Cucumber shared context

__./src/test/java/cucumber/shared/StepDefsContext__ :

```java
package cucumber.shared;

import io.restassured.response.Response;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum StepDefsContext {

    CONTEXT;

    private final ThreadLocal<Map<String, Object>> givenObjects = ThreadLocal.withInitial(HashMap::new);
    private final ThreadLocal<Map<String, Object>> thenObjects = ThreadLocal.withInitial(HashMap::new);
    private final ThreadLocal<Map<String, Object>> errorObjects = ThreadLocal.withInitial(HashMap::new);

    public <T> T givenObject(Class<T> clazz) {
        return clazz.cast(givenObjects.get()
                .get(clazz.getName()));
    }

    public <T> void givenObject(T object, Class<T> clazz) {
        givenObjects.get()
                .put(clazz.getName(), object);
    }

    public <T> T thenObject(Class<T> clazz) {
        return clazz.cast(thenObjects.get()
                .get(clazz.getName()));
    }

    public <T> void thenObject(T object, Class<T> clazz) {
        thenObjects.get()
                .put(clazz.getName(), object);
    }

    public <T> T errorObject(Class<T> clazz) {
        return clazz.cast(errorObjects.get()
                .get(clazz.getName()));
    }

    public <T> void errorObject(T object, Class<T> clazz) {
        errorObjects.get()
                .put(clazz.getName(), object);
    }


    public void reset() {
        givenObjects.get()
                .clear();
        thenObjects.get()
                .clear();
        errorObjects.get()
                .clear();
    }
}
```

## Step 10: Step definitions

Step definitions will reside under `./src/test/java/cucumber/steps`.

```sh
mkdir ./src/test/java/cucumber/steps
```

__./src/test/java/cucumber/StepsBootstrap.java__ :

```java
package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepsBootstrap {
}
```