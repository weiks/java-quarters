# java-quarters
Java API wrapper for Quarters
### Installation
Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.weiks</groupId>
    <artifactId>java-quarters</artifactId>
    <version>1.2</version>
</dependency>
```
Gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.weiks:java-quarters:1.2'
}
```
Download from [releases page](https://github.com/weiks/java-quarters/releases)
### Usage
[Create an app](http://poq.gg/apps/create/new) and use app ID and app key to authenticate
```java
// Create new QuartersBuilder instance with credentials
QuartersBuilder builder = new QuartersBuilder("<app-ID>", "<app-key>");
Quarters quarters = builder.build();

// Fetch user access token
Call<AccessToken> call = quarters.getAccessToken("<refresh-token>");

// Execute synchronously
String refreshToken = call.execute().body().getRefreshToken();

// Execute asynchronously with callback
call.enqueue(new MyAccessTokenCallback());
```
### Documentation
- [Javadocs](https://javadoc.jitpack.io/com/github/weiks/java-quarters/index/javadoc/index.html)
- [General API documentation](https://weiks.github.io/quarters-docs/)
