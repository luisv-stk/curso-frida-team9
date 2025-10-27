
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
        String prompt = "\r\n" + //
                        "Eres un asistente de análisis de imágenes. Tu tarea es analizar una imagen codificada en base64 y devolver SOLO un objeto JSON válido" + //
                        "            ### Reglas:" + //
                        "            1. Salida SOLO del objeto JSON, sin explicaciones y sin comentarios." + //
                        "            2. Utiliza comillas dobles para todas las claves y valores." + //
                        "            3. La lista de \"tags\" debe contener entre 1 y 10 elementos." + //
                        "            4. Las etiquetas deben seleccionarse SÓLO de este conjunto predefinido:" + //
                        "            [\"retrato\", \"paisaje\", \"naturaleza\", \"arquitectura\", \"familia\", \"amigos\", \"viajes\", \"celebración\", \"comida\", \"mascotas\", \"deportes\", \"arte\", \"noche\", \"dia\", \"urbano\", \"playa\", \"montañas\", \"histórico\", \"selfi\"]" + //
                        "            5. Todas las etiquetas deben estar en minúsculas." + //
                        "            6. Si no se aplican etiquetas relevantes, devuelve una lista vacía: []." + //
                        "            7. Incluye siempre el atributo \"tags\"." + //
                        "            8. Devuelve siempre el JSON formateado para que cada atributo se lea en una línea distinta." + //
                        "            Analiza la imagen y devuelve SÓLO el objeto JSON siguiendo estas reglas.";

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
            //return EntityUtils.toString(response.getEntity());
            String jsonString = EntityUtils.toString(response.getEntity());
            ObjectMapper mapperFormat = new ObjectMapper();
            Object json = mapperFormat.readValue(jsonString, Object.class);
            // Devuelve el JSON formateado (pretty print)
            return mapperFormat.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        }
    }

}