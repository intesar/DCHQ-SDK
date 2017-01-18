<figure>
<img src="http://ww1.prweb.com/prfiles/2015/07/21/12907174/gI_146921_dchq-logo.png" alt="" />
</figure>



# DCHQ-Java-SDK
DCHQ-Java-SDK simplifies using DCHQ REST API's. Providers high-level java abstraction for REST API's.

# OpenSource and Community-Led
 The SDK is open-source and community driven effort. If you want to contribute please reach out to us founders@dchq.io

You can interact with live REST API here:
```
https://dchq.readme.io/
```

# build SDK (requires JDK8+, Gradle)
gradle build

# build without test
gradle build -x test


# test
gradle test

# sdk maven endpoint
## maven dependency
```
<dependency>
    <groupId>io.dchq</groupId>
    <artifactId>DCHQ-SDK</artifactId>
    <version>3.0-SNAPSHOT</version>
</dependency>
```

## gradle
# You'll add sonatype snapshot repos to pull sdk.
```
repositories {
    maven { url "http://repo.spring.io/libs-release" }
    maven { url "https://oss.sonatype.org/" }
    maven { url "https://oss.sonatype.org/content/groups/public" }
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
    mavenLocal()
    mavenCentral()
}
```
# Gradle dependency
```
compile("io.dchq:DCHQ-SDK:3.0-SNAPSHOT")
```
# Example Code
```
BlueprintService blueprintService = ServiceFactory.buildBlueprintService(rootUrl, username, password);
ResponseEntity<List<Blueprint>> responseEntity = blueprintService.get();
for (Blueprint bl : responseEntity.getResults()) {
    logger.info("Blueprint type [{}] name [{}] author [{}]", bl.getBlueprintType(), bl.getName(), bl.getCreatedBy());
}
```

Code examples and [JUnit and IT tests](https://github.com/intesar/DCHQ-SDK/tree/master/src/test/java/io/dchq/sdk/core)
