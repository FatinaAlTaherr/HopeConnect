package com.HopeConnect.HC.services.ExternalAPI;

import com.HopeConnect.HC.models.OrphanManagement.Orphan;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    @Value("${chatbot.api.url}")
    private String apiUrl;

    @Value("${chatbot.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateOrphanReport(Orphan orphan) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // ✅ Shorter prompt
            String prompt = String.format("""
                Generate a compassionate and clear report for the following orphan:
                Name: %s
                Gender: %s
                Date of Birth: %s
                Education Status: %s
                Health Condition: %s
                """,
                    orphan.getFullName(),
                    orphan.getGender(),
                    orphan.getBirthDate(),
                    orphan.getEducationStatus(),
                    orphan.getHealthCondition()
            );

            // ✅ Construct the request body with model and token limit
            JsonNode requestBody = mapper.createObjectNode()
                    .put("model", "gpt-3.5-turbo")
                    .put("max_tokens", 300)
                    .set("messages", mapper.createArrayNode()
                            .add(mapper.createObjectNode()
                                    .put("role", "user")
                                    .put("content", prompt)
                            )
                    );

            // ✅ Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey); // No need to add `?key=` in URL

            HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);

            // ✅ Make the POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // ✅ Parse the response
            JsonNode root = mapper.readTree(response.getBody());

            // If there's an error node in the response
            if (root.has("error")) {
                String errorMsg = root.path("error").path("message").asText("Unknown error occurred");
                return "Failed to generate report: " + errorMsg;
            }

            // ✅ Extract assistant message
            return root
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("No report generated");

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to generate report: " + e.getMessage();
        }
    }
}
