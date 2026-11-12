package com.itjyr.travelserver.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LLMutils {
    private String baseURL;
    private String apiKey;
    private String model;
    private OkHttpClient client;
    private ObjectMapper objectMapper = new ObjectMapper();

    public LLMutils(String baseURL, String apiKey, String model) {
        this.baseURL = baseURL;
        this.apiKey = apiKey;
        this.model = model;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public String chat(String systemPrompt, String userPrompt) {
        String requestBody = buildRequestBody(systemPrompt, userPrompt, false);
        Request request = new Request.Builder()
                .url(baseURL + "/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8")))
                .build();
        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("LLM调用异常： " + response.code());
            }
            String responseBody = response.body().string();
            return extractContent(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 模型返回数据处理
    private String extractContent(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode choices = root.path("choices");
        if (choices.isArray() && choices.size() > 0) {
            return choices.get(0).path("message").path("content").asText();
        }
        return "";
    }

    public String buildRequestBody(String systemPrompt, String userPrompt, Boolean stream) {
        StringBuilder str = new StringBuilder();
        str.append("{");
        str.append("\"model\":\"" + model + "\",");
        str.append("\"stream\":" + stream + ",");
        str.append("\"messages\":[");
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            str.append("{\"role\":\"system\",\"content\":\"" + systemPrompt + "\"},");
        }
        str.append("{\"role\":\"user\",\"content\":\"" + escapeJson(userPrompt) + "\"}");
        str.append("],");
        str.append("\"temperature\":0.7");
        str.append("}");
        return str.toString();
    }

    // JSON字符串转义方法
    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\")      // 转义反斜杠
                .replace("\"", "\\\"")      // 转义双引号
                .replace("\n", "\\n")       // 转义换行符
                .replace("\r", "\\r")       // 转义回车符
                .replace("\t", "\\t");      // 转义制表符
    }
}
