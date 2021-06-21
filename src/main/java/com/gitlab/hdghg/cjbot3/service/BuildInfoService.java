package com.gitlab.hdghg.cjbot3.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BuildInfoService {

    /**
     * Obtain content of JAR's build information stored in pom.properties file.
     * @return File content in single String
     */
    public String mavenBuildInfo() {
        var pomProperties = "/META-INF/maven/com.gitlab.hdghg/cjbot3/pom.properties";
        try (var is = getClass().getResourceAsStream(pomProperties)) {
            if (null != is) {
                var bytes = is.readAllBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            } else {
                return "pom.properties not found";
            }
        } catch (IOException e) {
            return "ERROR " + e.getMessage();
        }
    }
}
