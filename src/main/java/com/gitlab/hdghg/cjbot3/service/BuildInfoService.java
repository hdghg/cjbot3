package com.gitlab.hdghg.cjbot3.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BuildInfoService {

    public String mavenBuildInfo() {
        try (var is = BuildInfoService.class.getResourceAsStream("/META-INF/maven/com.gitlab.hdghg/cjbot3/pom.properties")) {
            if (null != is) {
                byte[] bytes = is.readAllBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            } else {
                return "pom.properties not found";
            }
        } catch (IOException e) {
            return "ERROR " + e.getMessage();
        }
    }
}
