package com.gitlab.hdghg.cjbot3;

import com.gitlab.hdghg.cjbot3.service.MessageService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.pengrad.telegrambot.model.Update;

public class Function {

    @FunctionName("HttpTrigger-Java")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION)
                    HttpRequestMessage<Update> request,
            final ExecutionContext context) {
        String key = System.getenv("bot.key");
        int id = Integer.parseInt(System.getenv("bot.id"));
        new MessageService(key, id)
                .processUpdate(request.getBody(), context);
        return request.createResponseBuilder(HttpStatus.OK)
                .body("done")
                .build();
    }
}
