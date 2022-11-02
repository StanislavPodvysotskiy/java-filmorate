/*
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

class UserControllerTest {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @BeforeAll
    public static void beforeAll() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"email\": \"mail@yandex.ru\",\n" +
                "\t\"login\": \"login\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"birthday\": \"1995-12-28\"\n" +
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
    public void shouldBeCode500WhenSameIdAdd() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"email\": \"mail@yandex.ru\",\n" +
                "\t\"login\": \"login\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"birthday\": \"1995-12-28\"\n" +
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
    public void shouldBeCode500WhenEmailIsEmpty() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"email\": \"\",\n" +
                "\t\"login\": \"login\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"birthday\": \"1995-12-28\"\n" +
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
    public void shouldBeCode500WhenEmailPatterNotMatch() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"email\": \"mail.yandex.ru@\",\n" +
                "\t\"login\": \"login\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"birthday\": \"1995-12-28\"\n" +
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
    public void shouldBeCode500WhenLoginHaveSpaces() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"email\": \"mail@yandex.ru\",\n" +
                "\t\"login\": \"log in\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"birthday\": \"1995-12-28\"\n" +
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
    public void shouldReplaceNameByLoginWhenItsEmpty() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"2\",\n" +
                "\t\"email\": \"mail@yandex.ru\",\n" +
                "\t\"login\": \"login\",\n" +
                "\t\"name\": \"\",\n" +
                "\t\"birthday\": \"1995-12-28\"\n" +
                "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        assertEquals("login", name);
    }

    @Test
    public void shouldBeCode500WhenBirthdayAfterLocalDateNow() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"3\",\n" +
                "\t\"email\": \"mail@yandex.ru\",\n" +
                "\t\"login\": \"login\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"birthday\": \"2995-12-28\"\n" +
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
    public void shouldUpdateUser() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"email\": \"mail@mail.ru\",\n" +
                "\t\"login\": \"updateLogin\",\n" +
                "\t\"name\": \"updateName\",\n" +
                "\t\"birthday\": \"1980-10-18\"\n" +
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
        URI url = URI.create("http://localhost:8080/users");
        String json = "{\n" +
                "\t\"id\": \"3\",\n" +
                "\t\"email\": \"mail@yandex.ru\",\n" +
                "\t\"login\": \"login\",\n" +
                "\t\"name\": \"name\",\n" +
                "\t\"birthday\": \"1995-12-28\"\n" +
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

}*/
