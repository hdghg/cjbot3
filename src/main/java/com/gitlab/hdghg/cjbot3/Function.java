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
            final ExecutionContext context) {
        try {
            MessageService.forEnvironment()
                    .processUpdate(request.getBody(), context);
        } catch (Exception e) {
            context.getLogger().log(Level.SEVERE, "Failed to process message", e);
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("fail")
                    .build();
        }
        return request.createResponseBuilder(HttpStatus.OK)
                .body("done")
                .build();
    }
}
