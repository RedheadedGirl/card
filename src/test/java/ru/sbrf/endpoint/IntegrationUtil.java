package ru.sbrf.endpoint;

import org.testcontainers.shaded.com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class IntegrationUtil {

    public static String readResourceToString(String resourceName) throws IOException {
        URL url = Resources.getResource(resourceName);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
