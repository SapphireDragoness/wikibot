package wikibot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
            case "\\article" -> sendMessage(id, msg.getText());
            default -> sendMessage(id, "I don't understand :(");
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

    public void getArticle() {

    }

    private String readTokenFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("token"));
        String line = br.readLine();

        br.close();
        return line;
    }
}
