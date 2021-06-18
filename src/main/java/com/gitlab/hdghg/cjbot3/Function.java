package com.gitlab.hdghg.cjbot3;

import com.gitlab.hdghg.cjbot3.service.BuildInfoService;
import com.gitlab.hdghg.cjbot3.service.MessageService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.WebhookInfo;
import com.pengrad.telegrambot.response.BaseResponse;

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
            Integer id = me.id();
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
        sb.append("\npom.properties:\n").append(new BuildInfoService().mavenBuildInfo());

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body(sb.toString())
                .build();
    }

    /**
     * Provide configuration and suggestions
     */
    @FunctionName("webhook")
    public HttpResponseMessage webhook(
            @HttpTrigger(name = "webhook", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.FUNCTION)
                    HttpRequestMessage<?> request,
            ExecutionContext context) {
        String code = request.getQueryParameters().get("code");
        String innerHtml;
        try {
            WebhookInfo webhook = MessageService.forEnvironment().getWebhook();
            String url = webhook.url();
            var sb = new StringBuilder("<p>Webhook url: ");
            if (null == url || "".equals(url)) {
                sb.append("URL is not set");
            } else {
                sb.append(url);
            }
            sb.append("</p>\n");
            Integer pendingUpdateCount = webhook.pendingUpdateCount();
            sb.append("<p>Pending update count: ").append(pendingUpdateCount).append("</p>\n");
            sb.append("<a href='deleteWebhook?code=").append(code).append("'>Delete webhook</a>\n");
            sb.append("<a href='createWebhook?code=").append(code).append("'>Create webhook</a>\n");
            innerHtml = sb.toString();
        } catch (Exception e) {
            context.getLogger().log(Level.WARNING, "Failed to create Message Service: ", e);
            innerHtml = "<p style='color:red'>ERROR" + e.getMessage() + "</p>";
        }
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "text/html")
                .body(wrap(innerHtml))
                .build();
    }

    /**
     * Provide configuration and suggestions
     */
    @FunctionName("deleteWebhook")
    public HttpResponseMessage deleteWebhook(
            @HttpTrigger(name = "deleteWebhook", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.FUNCTION)
                    HttpRequestMessage<?> request,
            ExecutionContext context) {
        String innerHtml;
        BaseResponse baseResponse = MessageService.forEnvironment().deleteWebhook();
        if (baseResponse.isOk()) {
            String code = request.getQueryParameters().get("code");
            return request.createResponseBuilder(HttpStatus.FOUND)
                    .header("Location", "webhook?code=" + code)
                    .build();
        }
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body("Failed to delete webhook: " + baseResponse.errorCode() + " " + baseResponse.description())
                .build();
    }


    private String wrap(String innerHtml) {
        var sb = new StringBuilder("<!DOCTYPE html>\n");
        sb.append("<html><head><title>Webhook setup</title><meta charset='utf-8'></head>");
        sb.append("<body>");
        sb.append(innerHtml);
        sb.append("</body></html>");
        return sb.toString();
    }

}
