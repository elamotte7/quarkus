# Backend

[![Build Status](https://travis-ci.org/elamotte7/quarkus.svg?branch=0.19.1)](https://travis-ci.org/elamotte7/quarkus)

## Description

## Components

This module is based on the following components:
- [quarkus-resteasy-json-b](https://quarkus.io/guides/rest-json-guide): Contexts and dependency injection
- [quarkus-cdi](https://quarkus.io/guides/cdi-reference): Contexts and dependency injection
- [quarkus-hibernate-orm](https://quarkus.io/guides/hibernate-orm-guide): Hibernate
- [quarkus-hibernate-validator](https://quarkus.io/guides/validation-guide): Bean validator
- [quarkus-jdbc-mariadb](https://quarkus.io/guides/datasource-guide): JDBC driver
- [quarkus-undertow](https://quarkus.io/guides/undertow-reference): Servlet container
- [quarkus-swagger-ui](https://quarkus.io/guides/openapi-swaggerui-guide) : Apis documentation
- [quarkus-kubernetes](https://quarkus.io/guides/kubernetes-guide): K8s
- [quarkus-smallrye-jwt](https://quarkus.io/guides/jwt-guide): JWT validation
- [quarkus-smallrye-rest-client](https://quarkus.io/guides/rest-client-guide): REST client

**Note** that this project requires **java 11** and **maven 3.6.1**.

## Dealing with extensions
From inside a Quarkus project, you can obtain a list of the available extensions with:

```bash
./mvnw quarkus:list-extensions
```

You can enable an extension using:

```bash
./mvnw quarkus:add-extension -Dextensions="hibernate-validator"
```

Extensions are passed using a comma-separated list.

The extension name is the GAV name of the extension: e.g. io.quarkus:quarkus-agroal. But you can pass a partial name and Quarkus will do its best to find the right extension. For example, agroal, Agroal or agro will expand to io.quarkus:quarkus-agroal. If no extension is found or if more than one extensions match, you will see a red check mark ❌ in the command result.

```bash
$ mvn quarkus:add-extensions -Dextensions=jdbc,agroal,non-exist-ent
[...]
❌ Multiple extensions matching 'jdbc'
     * io.quarkus:quarkus-jdbc-h2
     * io.quarkus:quarkus-jdbc-mariadb
     * io.quarkus:quarkus-jdbc-postgresql
     Be more specific e.g using the exact name or the full gav.
✅ Adding extension io.quarkus:quarkus-agroal
❌ Cannot find a dependency matching 'non-exist-ent', maybe a typo?
[...]
```

## Development mode
Quarkus comes with a built-in development mode. Run your application with:

```bash
./mvnw compile quarkus:dev
```

You can then update the application sources, resources and configurations. The changes are automatically reflected in your running application. This is great to do development spanning UI and database as you see changes reflected immediately.

quarkus:dev enables hot deployment with background compilation, which means that when you modify your Java files or your resource files and refresh your browser these changes will automatically take effect. This works too for resource files like the configuration property file. The act of refreshing the browser triggers a scan of the workspace, and if any changes are detected the Java files are compiled, and the application is redeployed, then your request is serviced by the redeployed application. If there are any issues with compilation or deployment an error page will let you know.

Hit CTRL+C to stop the application.

## Remote Development Mode
It is possible to use development mode remotely, so that you can run Quarkus in a container environment (such as Openshift) and have changes made to your local files become immediately visible.

This allows you to develop in the same environment you will actually run your app in, and with access to the same services.

### Warning
Do not use this in production. This should only be used in a development environment. You should not run production application in dev mode.
To do this you must have the quarkus-undertow-websockets extension installed:

```bash
./mvnw quarkus:add-extension -Dextensions="undertow-websockets"
```

You must also have the following config properties set:

* quarkus.live-reload.password

* quarkus.live-reload.url

These can be set via application.properties, or any other way (e.g. system properties, environment vars etc). The password must be set on both the local and remote processes, while the url only needs to be set on the local host.

Start Quarkus in dev mode on the remote host. Now you need to connect your local agent to the remote host:

```bash
./mvnw quarkus:remote-dev -Dquarkus.live-reload.url=http://my-remote-host:8080
```

Now every time you refresh the browser you should see any changes you have made locally immediately visible in the remote app.

## Debugging
You can run a Quarkus application in debug mode using:

```bash
./mvnw compile quarkus:dev -Ddebug=true
```

Then, attach your debugger to localhost:5005.

## Using a proxy
You can use a forward proxy in dev mode using:

```bash
./mvnw compile quarkus:dev -Djvm.args="-Dhttps.proxyHost=HTTPS_PROXY_HOST -Dhttps.proxyPort=HTTPS_PROXY_PORT -Dhttp.proxyHost=HTTP_PROXY_HOST -Dhttp.proxyPort=HTTP_PROXY_PORT"
```

## Import in your IDE
Once you have a project generated, you can import it in your favorite IDE. The only requirement is the ability to import a Maven project.

### Eclipse

In Eclipse, click on: File → Import. In the wizard, select: Maven → Existing Maven Project. On the next screen, select the root location of the project. The next screen list the found modules; select the generated project and click on Finish. Done!

In a separated terminal, run ./mvnw compile quarkus:dev, and enjoy a highly productive environment.

### IntelliJ

In IntelliJ:

From inside IntelliJ select File → New → Project From Existing Sources…​ or, if you are on the welcome dialog, select Import project.

1. Select the project root

2. Select Import project from external model and Maven

3. Next a few times (review the different options if needed)

4. On the last screen click on Finish

5. In a separated terminal or in the embedded terminal, run ./mvnw compile quarkus:dev. Enjoy!


### Visual Studio Code

Open the project directory in VS Code. If you have installed the Java Extension Pack (grouping a set of Java extensions), the project is loaded as a Maven project.

## Logging Quarkus application build classpath tree
Usually, dependencies of an application (which is a Maven project) could be displayed using mvn dependency:tree command. In case of a Quarkus application, however, this command will list only the runtime dependencies of the application. Given that the Quarkus build process adds deployment dependencies of the extensions used in the application to the original application classpath, it could be useful to know which dependencies and which versions end up on the build classpath. Luckily, the quarkus-bootstrap Maven plugin includes the build-tree goal which displays the build dependency tree for the application.

To be able to use it, the following plugin configuration has to be added to the pom.xml:
```xml
<plugin>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-bootstrap-maven-plugin</artifactId>
    <version>${quarkus.version}</version>
</plugin>
```

Now you should be able to execute mvn quarkus-bootstrap:build-tree on your project and see something like:

```bash
[INFO] --- quarkus-bootstrap-maven-plugin:0.17.0:build-tree (default-cli) @ getting-started ---
[INFO] org.acme:getting-started:jar:1.0-SNAPSHOT
[INFO] └─ io.quarkus:quarkus-resteasy-deployment:jar:0.17.0 (compile)
[INFO]    ├─ io.quarkus:quarkus-resteasy-server-common-deployment:jar:0.17.0 (compile)
[INFO]    │  ├─ io.quarkus:quarkus-core-deployment:jar:0.17.0 (compile)
[INFO]    │  │  ├─ commons-beanutils:commons-beanutils:jar:1.9.3 (compile)
[INFO]    │  │  │  ├─ commons-logging:commons-logging:jar:1.2 (compile)
[INFO]    │  │  │  └─ commons-collections:commons-collections:jar:3.2.2 (compile)
...
```
## Building a native executable
Native executables make Quarkus applications ideal for containers and serverless workloads.

Make sure to have GRAALVM_HOME configured and pointing to GraalVM version 1.0.0-rc16. Verify that your pom.xml has the proper native profile (see Maven configuration).

Create a native executable using: ./mvnw package -Pnative. A native executable will be present in target/.

To run Integration Tests on the native executable, make sure to have the proper Maven plugin configured (see Maven configuration) and launch the verify goal.

```bash
./mvnw verify -Pnative
...
[quarkus-quickstart-runner:50955]     universe:     391.96 ms
[quarkus-quickstart-runner:50955]      (parse):     904.37 ms
[quarkus-quickstart-runner:50955]     (inline):   1,143.32 ms
[quarkus-quickstart-runner:50955]    (compile):   6,228.44 ms
[quarkus-quickstart-runner:50955]      compile:   9,130.58 ms
[quarkus-quickstart-runner:50955]        image:   2,101.42 ms
[quarkus-quickstart-runner:50955]        write:     803.18 ms
[quarkus-quickstart-runner:50955]      [total]:  33,520.15 ms
[INFO]
[INFO] --- maven-failsafe-plugin:2.22.0:integration-test (default) @ quarkus-quickstart-native ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running org.acme.quickstart.GreetingResourceIT
Executing [/Users/starksm/Dev/JBoss/Quarkus/starksm64-quarkus-quickstarts/getting-started-native/target/quarkus-quickstart-runner, -Dquarkus.http.port=8081, -Dtest.url=http://localhost:8081, -Dquarkus.log.file.path=target/quarkus.log]
2019-02-28 16:52:42,020 INFO  [io.quarkus] (main) Quarkus started in 0.007s. Listening on: http://localhost:8080
2019-02-28 16:52:42,021 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.081 s - in org.acme.quickstart.GreetingResourceIT
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

...
```
## Docker

### building images

Before building the docker image run:

```bash
$ mvn clean package
```

Then, build the image with:

```bash
$ docker build -f src/main/dockerfiles/Dockerfile.jvm -t quarkus/backend-jvm .
```

Then run the container using:

```bash
$ docker run -i --rm -p 8080:9000 quarkus/backend-jvm
```

### building native images (container friendly executable)

The native executable will be specific to your operating system. To create an executable that will run in a container, use the following:

```bash
./mvnw package -Pnative -Dnative-image.docker-build=true
```

The produced executable will be a 64 bit Linux executable, so depending on your operating system it may no longer be runnable. However, it’s not an issue as we are going to copy it to a Docker container. Note that in this case the build itself runs in a Docker container too, so you don’t need to have GraalVM installed locally.

You can follow the Build a native executable guide as well as Deploying Application to Kubernetes and OpenShift for more information.

Then, build the image with:

```bash
$ docker build -f src/main/docker/Dockerfile.native -t quarkus/backend .
```

Then run the container using:

```bash
$ docker run -i --rm -p 8080:8080 quarkus/backend
```

### Use the image with environment variables

| Variable                      | Default                                              | Comment                             |
|-------------------------------|------------------------------------------------------|-------------------------------------|
| PORT                          | '9000'                                               | Port where api are available        |
| OIDC_ISSUER                   | 'https://accounts.google.com'                        | Open Id connect Issuer              |
| OIDC_PROVIDER_METADATA_URL    | 'https://www.googleapis.com/oauth2/v3/certs'         | JWKs uri                            |
| DATASOURCE_URL                | 'jdbc:mariadb://shipment-db-quarkus:3306/shipmentdb' | Database url                        |
| DATASOURCE_DRIVER             | 'org.mariadb.jdbc.Driver'                            | Database driver                     |
| DATASOURCE_USER               | 'mariadbuser'                                        | Database user                       |
| DATASOURCE_PWD                | 'mariadbpw'                                          | Database password                   |
| CONSOLE_LOG_ENABLED           | 'true'                                               | Enabling Console Log                |
| CONSOLE_LOG_FORMAT            | '%d{HH:mm:ss} %-5p [%c{2.}]] (%t) %s%e%n'            | Console Log Format                  |
| CONSOLE_LOG_LEVEL             | 'DEBUG'                                              | Console Log Level                   |
| FILE_LOG_ENABLED              | 'true'                                               | Enabling File log                   |
| FILE_LOG_PATH                 | '/tmp/trace.log'                                     | File Log directory                  |
| FILE_LOG_LEVEL                | 'TRACE'                                              | File Log Level                      |
| FILE_LOG_FORMAT               | '%d{HH:mm:ss} %-5p [%c{2.}]] (%t) %s%e%n'            | File log format                     |
| HIBERNATE_DB_GENERATION       | 'drop-and-create'                                    | DB Strategy initialisation          |
| HIBERNATE_LOG_SQL             | 'true'                                               | Enabling Hibernate SQL Log or not   |
| HTTP_PROXY_HOST               | ''                                                   | Forward proxy host                  |
| HTTP_PROXY_PORT               | ''                                                   | Forward proxy port                  |
| HTTPS_PROXY_HOST              | ''                                                   | Forward proxy https host            |
| HTTPS_PROXY_PORT              | ''                                                   | Forward proxy https port            |
| SSL_CERTIFICAT_FILE           | 'META-INF/certs/server.pem'                          | SSL certificat file                 |
| SSL_CERTIFICAT_KEY_FILE       | 'META-INF/certs/server.key'                          | SSL certificat key file             |
| SSL_PROTOCOLS                 | 'ECDHE-RSA-AES128-GCM-SHA256:...'                    | SSL Cipher suite                    |
| ...                           | ''                                                   | ...                                 |

```bash
docker run -dit --name=quarkus-backend  -e OIDC_ISSUER=oidc-issuer ... -p 9000:9000 quarkus/backend
```

Inspect the container:

```bash
$ docker ps
CONTAINER ID        IMAGE                                               COMMAND                  CREATED             STATUS              PORTS                                        NAMES
48e1575dfa51        registry.fr/backend:0.0.1-SNAPSHOT   "/deployments/run-ja…"   10 seconds ago      Up 9 seconds        8778/tcp, 9779/tcp, 0.0.0.0:9000->9000/tcp   quarkus-backend
$ docker exec -it 48e1575dfa51 sh

```

## Maven configuration

If you have not used project scaffolding, add the following elements in your pom.xml

```xml
<dependencyManagement>
    <dependencies>
        <dependency> 
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-bom</artifactId>
            <version>${quarkus.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<build>
    <plugins>
        <plugin> 
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-maven-plugin</artifactId>
            <version>${quarkus.version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>build</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>

<profiles>
    <profile> 
        <id>native</id>
        <build>
            <plugins>
                <plugin> 
                    <groupId>io.quarkus</groupId>
                    <artifactId>quarkus-maven-plugin</artifactId>
                    <version>${quarkus.version}</version>
                    <executions>
                        <execution>
                            <goals>
                                <goal>native-image</goal>
                            </goals>
                            <configuration>
                                <enableHttpUrlHandler>true</enableHttpUrlHandler>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
                <plugin> 
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-failsafe-plugin</artifactId>
                    <version>${surefire-plugin.version}</version>
                    <executions>
                        <execution>
                            <goals>
                                <goal>integration-test</goal>
                                <goal>verify</goal>
                            </goals>
                            <configuration>
                                <systemProperties>
                                    <native.image.path>${project.build.directory}/${project.build.finalName}-runner</native.image.path>
                                </systemProperties>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

Optionally use a BOM file to omit the version of the different Quarkus dependencies.

Use the Quarkus Maven plugin that will hook into the build process

Use a Æ profile and plugin to activate GraalVM compilation

If you want to test your native executable with Integration Tests, add the following plugin configuration. fr.Test names *IT and annotated @SubstrateTest will be run against the native executable. See the Native executable guide for more info.

## Uber-Jar Creation

Quarkus Maven plugin supports the generation of Uber-Jars by specifying an <uberJar> configuration option in your pom.xml.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-maven-plugin</artifactId>
            <version>${quarkus.version}</version>
            <configuration>
                <uberJar>true</uberJar> 
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>build</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Specifies that we want to build an Uber-Jar instead of a regular one. The regular jar will still be present in the target directory but it will be renamed to contain the .original suffix.

When building an Uber-Jar you can specify entries that you want to exclude from the generated jar by using the <ignoredEntries> configuration option.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-maven-plugin</artifactId>
            <version>${quarkus.version}</version>
            <configuration>
                <uberJar>true</uberJar>
                <ignoredEntries>
                    <ignoredEntry>META-INF/DEPENDENCIES.txt</ignoredEntry> 
                </ignoredEntries>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>build</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

The entries are relative to the root of the generated Uber-Jar. You can specify multiple entries by adding extra <ignoredEntry> elements.

## Testing Secured api

First generate a valid JWT Token by running fr.util.GenerateToken

The result is : 

```bash
Setting exp: 1561299210 / Sun Jun 23 16:13:30 CEST 2019
	Added claim: sub, value: jdoe-using-jwt-rbac
	Added claim: aud, value: [using-jwt-rbac]
	Added claim: upn, value: jdoe@quarkus.io
	Added claim: birthdate, value: 2001-07-13
	Added claim: auth_time, value: 1561298910
	Added claim: iss, value: https://quarkus.io/using-jwt-rbac
	Added claim: roleMappings, value: {"group2":"Group2MappedRole","group1":"Group1MappedRole"}
	Added claim: groups, value: ["Echoer","Tester","Subscriber","group2"]
	Added claim: preferred_username, value: jdoe
	Added claim: exp, value: Sun Jun 23 16:13:30 CEST 2019
	Added claim: iat, value: Sun Jun 23 16:08:30 CEST 2019
	Added claim: jti, value: a-123
eyJraWQiOiJcL3ByaXZhdGVLZXkucGVtIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJqZG9lLXVzaW5nLWp3dC1yYmFjIiwiYXVkIjoidXNpbmctand0LXJiYWMiLCJ1cG4iOiJqZG9lQHF1YXJrdXMuaW8iLCJiaXJ0aGRhdGUiOiIyMDAxLTA3LTEzIiwiYXV0aF90aW1lIjoxNTYxMjk4OTEwLCJpc3MiOiJodHRwczpcL1wvcXVhcmt1cy5pb1wvdXNpbmctand0LXJiYWMiLCJyb2xlTWFwcGluZ3MiOnsiZ3JvdXAyIjoiR3JvdXAyTWFwcGVkUm9sZSIsImdyb3VwMSI6Ikdyb3VwMU1hcHBlZFJvbGUifSwiZ3JvdXBzIjpbIkVjaG9lciIsIlRlc3RlciIsIlN1YnNjcmliZXIiLCJncm91cDIiXSwicHJlZmVycmVkX3VzZXJuYW1lIjoiamRvZSIsImV4cCI6MTU2MTI5OTIxMCwiaWF0IjoxNTYxMjk4OTEwLCJqdGkiOiJhLTEyMyJ9.YOkk1e7BXHTHPAnzWK9Pjs3EYtOi_rqHEcCPzCcVHZUxIpK77EvDjKM_mDYj45aacUgkcl1-rNE-PNM0QiD7vXuJ6oT20tMkiDv9T0K0sJcJbpbv6GoLONDA2ETGLDMtgkvJJW4tBeZIkIX6-bhVlxG4adMgIhdpTOewA6jK831lI_Zlwz9SuZNd4oKtv-uFbgUvwCQpn4qIRzP4ymJHL3Oxx7m-CH4W4gXFXpPiemoempw3odB8sbA8vdt3ZymfJvGue2Aha3OFYRqdeHOVGRnci-ILcY0cdX46G74199CtnqjN5G8WQ3txa_wTa455PRVekbjeYMXtjtbZX-EBrg

Process finished with exit code 0
```

Then copy the result base64 encoded and paste it in the command above:

```bash
$ curl -H "Authorization: Bearer <jwtToken>" http://localhost:8080/secured/roles-allowed
```

## Swagger UI and open api

### openapi

You can use openapi to retrieve details on the apis served by quarkus : 

```bash
$ curl http://localhost:8080/openapi
---
openapi: 3.0.1
info:
  title: Generated API
  version: "1.0"
paths:
  /hello:
    get:
      responses:
        200:
          description: OK
          content:
            text/plain:
              schema:
                type: string
  /secured/deny-all:
    get:
      responses:
        200:
          description: OK
          content:
            text/plain:
              schema:
                type: string
  /secured/permit-all:
    get:
      responses:
        200:
          description: OK
          content:
            text/plain:
              schema:
                type: string
  /secured/roles-allowed:
    get:
      responses:
        200:
          description: OK
          content:
            text/plain:
              schema:
                type: string

```

### Swagger

By default, Swagger UI is only available when Quarkus is started in dev or test mode.

If you want to make it available in production too, you can include the following configuration in your application.properties:

```bash
quarkus.swagger-ui.always-include=true
```

You can test your api with swagger @ 

```bash
http://localhost:8080/swagger-ui
```

If you want to chenge the swagger uri, you can modify your application.properties like follow

```bash
quarkus.swagger-ui.path=/<swagger-ui-uri>
```