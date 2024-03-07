package wikibot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) throws TelegramApiException, MalformedURLException {
        TelegramBotsApi bA = new TelegramBotsApi(DefaultBotSession.class);
        WikiBot bot = new WikiBot();

        bA.registerBot(bot);
    }
}