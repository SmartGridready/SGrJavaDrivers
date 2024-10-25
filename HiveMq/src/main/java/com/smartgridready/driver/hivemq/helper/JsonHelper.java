package com.smartgridready.driver.hivemq.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smartgridready.driver.api.common.GenDriverException;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonHelper {

    public static final Logger LOG = LoggerFactory.getLogger(JsonHelper.class);

    private JsonHelper() {
        throw new IllegalStateException("Helper class");
    }

    public static String parseJsonResponse(String jmesPath, String jsonResp) throws GenDriverException {

        if (jmesPath == null || jmesPath.trim().isEmpty()) {
            // no parsing required
            return jsonResp;
        }

        JmesPath<JsonNode> path = new JacksonRuntime();
        Expression<JsonNode> expression = path.compile(jmesPath);

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.readTree(jsonResp);
            JsonNode res = expression.search(jsonNode);

            // complex nodes: return the result as JSON string
            if (res instanceof ObjectNode) {
                return res.toString();
            }
            if (res instanceof ArrayNode) {
                return res.toString();
            }

            return res.asText();
        } catch (IOException e) {
            throw new GenDriverException("Failed to parse JSON response", e);
        }
    }
}
