package ru.yandex.practicum.filmorate.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @BeforeAll
    public static void beforeAll() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"description\": \"123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-\",\n" +
                "\t\"releaseDate\": \"1895-12-28\",\n" +
                "\t\"duration\": \"0\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    @Test
    public void shouldCode500IfSameIdAdd() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"description\": \"123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-\",\n" +
                "\t\"releaseDate\": \"1895-12-28\",\n" +
                "\t\"duration\": \"0\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(500, responseCode);
    }

    @Test
    public void shouldBeCode500WhenNameIsBlank() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"name\": \"\",\n" +
                "\t\"description\": \"Description\",\n" +
                "\t\"releaseDate\": \"1980-03-25\",\n" +
                "\t\"duration\": \"200\"\n" +
            "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(500, responseCode);
    }

    @Test
    public void shouldBeCode500WhenDescriptionLengthMoreThan200() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"description\": \"123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-123456789-123456789-123456789-123456789-123456789-" +
                "123456789-123456789-123456789-123456789-1\",\n" +
                "\t\"releaseDate\": \"1980-03-25\",\n" +
                "\t\"duration\": \"200\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(500, responseCode);
    }

    @Test
    public void shouldBeCode500WhenYearBefore1895() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"description\": \"Description\",\n" +
                "\t\"releaseDate\": \"1894-03-25\",\n" +
                "\t\"duration\": \"200\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(500, responseCode);
    }

    @Test
    public void shouldBeError500WhenDurationNegative() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"description\": \"Description\",\n" +
                "\t\"releaseDate\": \"1980-03-25\",\n" +
                "\t\"duration\": \"-200\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(500, responseCode);
    }

    @Test
    public void shouldUpdateFilm() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"name\": \"updateName\",\n" +
                "\t\"description\": \"updateDescription\",\n" +
                "\t\"releaseDate\": \"1980-03-25\",\n" +
                "\t\"duration\": \"200\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        assertEquals(200, responseCode);
        assertEquals("updateName", name);
    }

    @Test
    public void shouldBeCode500WhenIdNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/films");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"name\": \"updateName\",\n" +
                "\t\"description\": \"updateDescription\",\n" +
                "\t\"releaseDate\": \"1980-03-25\",\n" +
                "\t\"duration\": \"200\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        assertEquals(500, responseCode);
    }
}