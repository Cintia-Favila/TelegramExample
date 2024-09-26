package org.switf.telegramexample.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.switf.telegramexample.models.PlaceModel;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class AdminBot extends TelegramLongPollingBot {

    @Autowired
    private RestTemplate restTemplate;

    private final String botName;

    private final String botToken;

    public AdminBot(DefaultBotOptions botOptions,
                    @Value("${telegram.bot.name}") String botName,
                    @Value("${telegram.bot.token}") String botToken,
                    RestTemplate restTemplate) {
        super(botOptions);
        this.botName = botName;
        this.botToken = botToken;
        this.restTemplate = restTemplate;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken(){
        return botToken;
    }

    private final Map<Long, String> userStates = new HashMap<>();

    private final Map<Long, PlaceModel> userPlaces = new HashMap<>();


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        Long chatId = message.getChatId();

        String state = userStates.get(chatId);

        if(state==null){
            if (text.equalsIgnoreCase("add place")){
                sendText(chatId, "Ingresa el nombre del lugar");
                userStates.put(chatId, "esperando_el_nombre_del_lugar");
                userPlaces.put(chatId, new PlaceModel());
            } else {
                sendText(chatId, "Hola, que deceas hacer?");
            }
        } else {
            PlaceModel place = userPlaces.get(chatId);

            switch (state){
                case "esperando_el_nombre_del_lugar":
                    place.setNombre(text);
                    userPlaces.put(chatId, place);
                    sendText(chatId, "Ingresa la direccion del lugar");
                    userStates.put(chatId, "esperando_la_direccion_del_lugar");
                    break;

                case "esperando_la_direccion_del_lugar" :
                    place.setDireccion(text);
                    userPlaces.put(chatId, place);
                    sendText(chatId,"Ingresa la descripcion del lugar");
                    userStates.put(chatId, "esperando_la_descripcion_del_lugar");
                    break;

                case "esperando_la_descripcion_del_lugar":
                    place.setDescripcion(text);
                    userPlaces.put(chatId, place);

                    String url = "http://localhost:8080/places/addPlace";

                    try {
                        restTemplate.postForObject(url, place, PlaceModel.class);
                        sendText(chatId, "Lugar creado con exito: " + place.getNombre());
                    } catch (Exception e){
                        sendText(chatId, "Error " + e.getMessage());
                    } finally {
                        userStates.remove(chatId);
                        userPlaces.remove(chatId);
                    }
                    break;
            }

        }
    }

    private void sendText(Long chatId, String text){
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

}
