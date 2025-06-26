# Single-File Spring Boot App

A lightweight Spring Boot web service that demonstrates the power of JBang for rapid Java development. This project showcases how you can create a fully functional REST API with just a single Java file - no complex project structure or build configuration required.

## What Makes This Special

This project combines the simplicity of JBang with the robustness of Spring Boot, allowing you to run a complete web service directly from a single Java source file. Perfect for prototyping, learning, or creating microservices without the overhead of traditional project setup.

## Project Requirements

- **Java 21** - The project is configured to use Java 21 features
- **JBang** - For running the single-file Java application
- **Internet connection** - Required for automatic dependency resolution

## Dependencies

The project automatically resolves these Spring Boot dependencies through JBang:

- `spring-boot-starter-web:3.2.5` - Core web functionality and embedded Tomcat
- `spring-boot-starter-actuator:3.2.5` - Production-ready monitoring and management

No manual dependency management needed - JBang handles everything automatically.

## Getting Started

### Environment Setup

#### The project uses SDKMAN for managing Java and JBang versions

To install SDKMAN refer: [sdkman.io](https://sdkman.io/install)

- #### Initialize your development environment using SDKMAN CLI and [.sdkmanrc](.sdkmanrc) file

#### SDKMAN CLI command:

```bash
sdk env install
sdk env
```

## How to Run the Application

### Method 1: Direct Execution

Make the file executable and run it directly:

```bash
chmod +x AppScript.java
./AppScript.java
```

OR
```shell
sh +x AppScript.java
```

### Method 2: Using JBang Command (Recommended)

- Note: Need to set up the environment using [SDKMAN CLI command](#sdkman-cli-command) before running the below command

```bash
jbang AppScript.java
```

OR

### Run with Custom Arguments

```bash
jbang AppScript.java --server.port=8081
```

The application will start on port 8080 by default. You'll see Spring Boot's startup logs, and the service will be ready to accept requests.

---

### API Endpoints

### Hello World Endpoint

**GET** `/api/hello`

Returns a JSON greeting message.

**Parameters:**
- `name` (optional) - The name to greet. Defaults to "World"

**Examples:**

Basic request:
```bash
curl http://localhost:8080/api/hello
```

Response:
```json
{
  "message": "Hello, World!"
}
```

With custom name:
```bash
curl http://localhost:8080/api/hello?name=Developer
```

Response:
```json
{
  "message": "Hello, Developer!"
}
```

### Health Check (Actuator)

Spring Boot Actuator provides built-in health monitoring:

```bash
curl http://localhost:8080/actuator/health
```

Response:
```json
{
  "status": "UP"
}
```

---

## Development Tips

### Get Development Support

For development, you can use JBang's edit mode which provides better IDE integration:

```bash
jbang edit AppScript.java
```

This opens the file with proper IDE support and classpath configuration.

- #### Note: For IntelliJ IDEA add this [jetbrains-jbang-plugin](https://plugins.jetbrains.com/plugin/18257-jbang) plugin and right-click on un-imported dependencies and run `Sync JBang DEPS to module`

### Adding More Dependencies

Simply add more `//DEPS` lines to include additional libraries:

```java
//DEPS org.springframework.boot:spring-boot-starter-data-jpa:3.2.5
```

### Environment-Specific Configuration

You can customize Spring Boot properties by adding them to the JBang header:

```java
//JAVA_OPTIONS -Dserver.port=8081 -Dlogging.level.com.example=DEBUG
```

Pass Spring profiles or other properties:

```bash
jbang AppScript.java --server.port=9090 --spring.profiles.active=dev 
```

- #### We can also use a `application.properties` or `application.yml` file in project-root location as: [application.yml](application.yml) to pass spring configs  

### Dependency Resolution Problems:
Clear JBang cache if you encounter dependency issues:
```bash
jbang cache clear
```

---

## Why This Approach Works

This single-file approach is particularly powerful because it:

- **Eliminates build complexity** - No Maven or Gradle configuration needed
- **Reduces startup friction** - From idea to running service in seconds
- **Maintains full Spring Boot capabilities** - All features available despite the simple structure
- **Enables rapid prototyping** - Perfect for testing concepts or building simple services
- **Simplifies deployment** - Single file contains everything needed

Whether you're learning Spring Boot, prototyping a new API, or building a simple microservice, this JBang approach provides the perfect balance of simplicity and functionality. The power of enterprise-grade Java development is now just a single file away.

For suggestions, feedback, or issues, please open a GitHub issue or submit a pull request.

Happy coding! ✌️

---
