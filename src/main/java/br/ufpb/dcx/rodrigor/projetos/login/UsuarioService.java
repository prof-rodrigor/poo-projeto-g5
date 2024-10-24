package br.ufpb.dcx.rodrigor.projetos.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static java.net.HttpURLConnection.HTTP_OK;

public class UsuarioService {
    private static final Logger logger = LogManager.getLogger();

    public UsuarioService() {
    }

    public Usuario buscarUsuario(String login, String senha){
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/v1/autenticar";
        Map<String, String> dados = new HashMap<>();
        dados.put("login", login);
        dados.put("senha", senha);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String loginJson = mapper.writeValueAsString(dados);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginJson, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HTTP_OK) {
                return mapper.readValue(response.body(), Usuario.class);
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
        return null;
    }
}
