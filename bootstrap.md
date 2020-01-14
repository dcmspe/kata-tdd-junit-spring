# Bootstrap

## Step 1: initialize with springboot

```bash
curl https://start.spring.io/starter.tgz \
-d dependencies=web,lombok \
-d language=java \
-d type=maven-project \
-d baseDir=bootstrap-cucumber-spring \
-d groupId=com.newlight77 \ 
-d artifactId=bootstrap-cucumber-spring \
| tar -xzvf -
```

## Step 2: add dependencies

```xml
		<dependency>
			<groupId>org.assertj</groupId>
			<artifactId>assertj-core</artifactId>
			<version>3.11.1</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.9.8</version>
		</dependency>
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>rest-assured</artifactId>
			<version>3.1.1</version>
			<scope>test</scope>
		</dependency>
```

## Step 3: add build plugins

```xml
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<version>3.0.0-M3</version>
				<configuration>
					<properties>
						<configurationParameters>
							junit.jupiter.conditions.deactivate = *
							junit.jupiter.extensions.autodetection.enabled = true
							junit.jupiter.testinstance.lifecycle.default = per_class
							junit.jupiter.execution.parallel.enabled = true
						</configurationParameters>
					</properties>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.8.1</version>
				<configuration>
					<encoding>UTF-8</encoding>
					<source>1.8</source>
					<target>1.8</target>
					<compilerArgument>-Werror</compilerArgument>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-failsafe-plugin</artifactId>
				<version>2.22.2</version>
				<configuration>
					<includes>
						<!-- Run only integration tests -->
						<include>**/*IntegrationTest*.java</include>
						<include>**/*ItTest*.java</include>
						<include>**/*It*.java</include>
					</includes>
				</configuration>
				<executions>
					<execution>
						<goals>
							<goal>integration-test</goal>
							<goal>verify</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
```

## Step 4: Jaoco for Code Coverage

```xml
			<plugin>
				<groupId>org.jacoco</groupId>
				<artifactId>jacoco-maven-plugin</artifactId>
				<version>0.8.3</version>
				<configuration>
					<excludes>
						<exclude>**/*com/newlight77/bootstrap/model/**</exclude>
						<exclude>**/*com/newlight77/bootstrap/exception/**</exclude>
					</excludes>
				</configuration>
				<executions>
					<execution>
						<goals>
							<goal>prepare-agent</goal>
						</goals>
					</execution>
					<execution>
						<id>report</id>
						<phase>test</phase>
						<goals>
							<goal>report</goal>
						</goals>
						<configuration>
							<outputDirectory>target/jacoco/jacoco-report</outputDirectory>
						</configuration>
					</execution>
					<execution>
						<id>jacoco-check</id>
						<goals>
							<goal>check</goal>
						</goals>
						<configuration>
							<rules>
								<rule>
									<element>PACKAGE</element>
									<limits>
										<limit>
											<counter>LINE</counter>
											<value>COVEREDRATIO</value>
											<minimum>0.30</minimum>
<!--											<value>TOTALCOUNT</value>-->
<!--											<maximum>0.50</maximum>-->
										</limit>
									</limits>
								</rule>
							</rules>
						</configuration>
					</execution>
				</executions>
			</plugin>
```


## Step 5: add Cucumber dependencies

```xml
		<dependency>
			<groupId>io.cucumber</groupId>
			<artifactId>cucumber-java</artifactId>
			<version>4.3.0</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>io.cucumber</groupId>
			<artifactId>cucumber-junit</artifactId>
			<version>4.3.0</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>io.cucumber</groupId>
			<artifactId>cucumber-spring</artifactId>
			<version>4.3.0</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>de.monochromata.cucumber</groupId>
			<artifactId>reporting-plugin</artifactId>
			<version>3.0.9</version>
		</dependency>
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