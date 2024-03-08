package wikibot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WikiBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "wikibot";
    }

    @Override
    public String getBotToken() {
        try {
            return readTokenFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        User user = msg.getFrom();
        Long id = user.getId();

        switch (msg.getText()) {
            case "/article" -> {
                try {
                    sendMessage(id, getArticle());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "/start" -> sendMessage(id, "Hello! Type /article to get a random article from Wikipedia!");
            default -> sendMessage(id, "Type /article to get a random article from Wikipedia!");
        }

    }

    public void sendMessage(Long user, String msg) {
        SendMessage sm = SendMessage.builder()
                        .chatId(user.toString())
                        .text(msg).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String getArticle() throws IOException {
        URL url = new URL("https://en.wikipedia.org/api/rest_v1/page/random/summary");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String inputLine;
        StringBuilder content = new StringBuilder();
        BufferedReader in;
        ObjectMapper om = new ObjectMapper();
        JsonNode jn;

        connection.setRequestMethod("GET");
        connection.connect();

        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        jn = om.readTree(String.valueOf(content));
        return jn.get("content_urls").get("mobile").get("page").asText();
    }

    private String readTokenFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("token"));
        String line = br.readLine();

        br.close();
        return line;
    }
}
