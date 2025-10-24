
package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;
import com.example.demo.dtos.ApiRequestPayload;
import com.example.demo.dtos.Content;
import com.example.demo.dtos.ImageUrl;
import com.example.demo.dtos.Message;

import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ExternalApiService {

    private final HttpClient httpClient;

    public ExternalApiService() {
        this.httpClient = HttpClients.createDefault();
    }

    public String fetchMetadata(String base64Image) throws IOException, ParseException {
        HttpPost post = new HttpPost("https://frida-llm-api.azurewebsites.net/v1/chat/completions");

        // Example payload setup matching requirements
        String prompt = "You are an image analysis assistant. Your task is to analyze an image provided in base64 encoding and return ONLY a valid JSON object with this exact structure:\n" + //
                        " \n" + //
                        "            {\n" + //
                        "            \"tags\": [string]\n" + //
                        "            }\n" + //
                        " \n" + //
                        "            ### Rules:\n" + //
                        "            1. Output ONLY the JSON object, no explanations, no comments, no markdown.\n" + //
                        "            2. Use double quotes for all keys and values.\n" + //
                        "            3. The \"tags\" list must contain between 1 and 10 items.\n" + //
                        "            4. Tags must be selected ONLY from this predefined set:\n" + //
                        "            [\"portrait\", \"landscape\", \"nature\", \"architecture\", \"family\", \"friends\", \"travel\", \"celebration\", \"food\", \"pets\", \"sports\", \"art\", \"night\", \"day\", \"urban\", \"beach\", \"mountains\", \"historic\", \"candid\", \"selfie\"]\n" + //
                        "            5. All tags must be lowercase.\n" + //
                        "            6. If no relevant tags apply, return an empty list: [].\n" + //
                        "            7. Always include the \"tags\" attribute.\n" + //
                        "            8. Never include any extra attributes other than \"tags\".\n" + //
                        "            9. Do NOT add confidence scores, probabilities, or any other metadata.\n" + //
                        "            10. Ensure the output is valid JSON (curly braces, brackets, commas, double quotes).\n" + //
                        " \n" + //
                        "            ### Example Output:\n" + //
                        "            {\n" + //
                        "            \"tags\": [\"travel\", \"beach\", \"friends\"]\n" + //
                        "            }\n" + //
                        " \n" + //
                        "            Analyze the image and return ONLY the JSON object following these rules.";

        // Crear ImageUrl
        ImageUrl imageUrl = new ImageUrl();
        String imageSend = "data:image/jpeg;base64,".concat(base64Image);
        imageUrl.setUrl(imageSend);
        imageUrl.setDetail("auto");

        // Crear contenido de texto
        Content textContent = new Content();
        textContent.setType("text");
        // textContent.setText("¿Qué hay en esta imagen?");
        textContent.setText(prompt);

        // Crear contenido de imagen
        Content imageContent = new Content();
        imageContent.setType("image_url");
        imageContent.setImage_url(imageUrl);

        // Crear mensaje
        Message message = new Message();
        message.setRole("user");
        message.setContent(List.of(textContent, imageContent));

        // Crear payload
        ApiRequestPayload payload = new ApiRequestPayload();
        payload.setModel("claude-4-sonnet");
        payload.setMessages(List.of(message));
        payload.setStream(false);
        payload.setEnable_caching(true);

        // Token de autenticación
        String token = "uGcSkG1nXy8b41d9zuep";

        // Encabezado Authorization
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(payload);
        StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
        post.setEntity(entity);

        try (CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

}