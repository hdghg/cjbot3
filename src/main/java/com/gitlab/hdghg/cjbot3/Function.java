package com.gitlab.hdghg.cjbot3;

import com.gitlab.hdghg.cjbot3.service.MessageService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.pengrad.telegrambot.model.Update;

import java.util.logging.Level;

public class Function {

    @FunctionName("HttpTrigger-Java")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION)
                    HttpRequestMessage<Update> request,
            ExecutionContext context) {
        MessageService.forEnvironment().processUpdate(request.getBody(), context);
        return request.createResponseBuilder(HttpStatus.OK)
                .body("done")
                .build();
    }

    /**
     * Provide configuration and suggestions
     */
    @FunctionName("setup")
    public HttpResponseMessage setup(
            @HttpTrigger(name = "setup", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ADMIN)
                    HttpRequestMessage<?> request,
            ExecutionContext context) {
        String token = System.getenv("bot.key");
        String botIdString;
        try {
            MessageService messageService = new MessageService(token, 0, null);
            var me = messageService.getMe();
            Integer id = me.user().id();
            String botId = System.getenv("bot.id");
            if (null != botId) {
                try {
                    Integer botIdInteger = Integer.valueOf(botId);
                    if (botIdInteger.equals(id)) {
                        botIdString = "VALUE IS CORRECT: " + botId;
                    } else {
                        botIdString = "VALUE IS INCORRECT: " + botId + " SHOULD BE: " + id;
                    }
                } catch (NumberFormatException e) {
                    botIdString = "VALUE IS UNACCEPTABLE: " + botId;
                }
            } else {
                botIdString = "VALUE IS MISSING, SHOULD BE: " + id;
            }
        } catch (Exception e) {
            botIdString = "ERROR: " + e.getMessage();
        }
        String botBingKey = System.getenv("bot.bing.key");
        var sb = new StringBuilder("Bot status:\nbot.key: ");
        sb.append(null != token ? "OK" : "Not set (mandatory)").append("\nbot.id: ");
        sb.append(botIdString).append("\nbot.bing.key: ");
        sb.append(null == botBingKey ? "NOT SET" : "OK").append("\nWebhook: [TBD]");

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body(sb.toString())
                .build();
    }
}
