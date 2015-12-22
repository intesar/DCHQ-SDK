# DCHQ-SDK

# build without test
gradle build -x test

# build
gradle build

# test
gradle test

# upload jars to sonatype
gradle uploadArchives

# sdk maven endpoint
## maven
<dependency>
    <groupId>io.dchq</groupId>
    <artifactId>DCHQ-SDK</artifactId>
    <version>3.0-SNAPSHOT</version>
</dependency>

## gradle
# add the snapshot repos
repositories {
    maven { url "http://repo.spring.io/libs-release" }
    maven { url "https://oss.sonatype.org/" }
    maven { url "https://oss.sonatype.org/content/groups/public" }
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
    mavenLocal()
    mavenCentral()
}
# add actual dependency
compile("io.dchq:DCHQ-SDK:3.0-SNAPSHOT")

