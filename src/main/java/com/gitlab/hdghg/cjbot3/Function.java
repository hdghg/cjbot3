package com.gitlab.hdghg.cjbot3;

import com.gitlab.hdghg.cjbot3.service.BuildInfoService;
import com.gitlab.hdghg.cjbot3.service.ConfigurationService;
import com.gitlab.hdghg.cjbot3.service.MessageService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.WebhookInfo;

import java.util.concurrent.Callable;
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
        sb.append(null == botBingKey ? "NOT SET" : "OK").append("\nWebhook: go to /webhookConfig");
        sb.append("\npom.properties:\n").append(new BuildInfoService().mavenBuildInfo());

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body(sb.toString())
                .build();
    }

    /**
     * Provide configuration and suggestions
     */
    @FunctionName("webhookConfig")
    public HttpResponseMessage webhookConfig(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<?> request,
            ExecutionContext context) {
        WebhookInfo webhook;
        try {
            webhook = MessageService.forEnvironment().getWebhook();
        } catch (Exception e) {
            context.getLogger().log(Level.WARNING, "Failed to create Message Service: ", e);
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "text/html")
                    .body(wrap("<p style='color:red'>ERROR" + e.getMessage() + "</p>"))
                    .build();
        }
        String url = webhook.url();
        var sb = new StringBuilder("<p>Webhook url: ");
        if (null == url || "".equals(url)) {
            sb.append("URL is not set");
        } else {
            sb.append(url);
        }
        sb.append("</p>\n");
        Integer pendingUpdateCount = webhook.pendingUpdateCount();
        String code = request.getQueryParameters().get("code");
        sb.append("<p>Pending update count: ").append(pendingUpdateCount).append("</p>\n");
        sb.append("<form method='GET' action='webhook'>");
        sb.append("<p>Manage webhook:</p>");
        sb.append("Code: <input type='text' required name='code' value='");
        sb.append(null == code ? "" : code).append("'><br>");
        sb.append("<input type='radio' name='action' value='delete' selected>Delete<br>");
        sb.append("<input type='radio' name='action' value='create'>Create<br>");
        sb.append("<input type='submit'>Delete</input>");
        sb.append("</form>");
        sb.append("<br>");
        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "text/html")
                .body(wrap(sb.toString()))
                .build();
    }

    /**
     * Provide configuration and suggestions
     */
    @FunctionName("webhook")
    public HttpResponseMessage webhook(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST, HttpMethod.GET},
                    authLevel = AuthorizationLevel.FUNCTION)
                    HttpRequestMessage<Update> request,
            ExecutionContext context) {
        if (HttpMethod.POST.equals(request.getHttpMethod())) {
            MessageService.forEnvironment().processUpdate(request.getBody(), context);
            return request.createResponseBuilder(HttpStatus.OK).build();
        }

        var actionString = request.getQueryParameters().get("action");
        Callable<String> action;
        switch (actionString) {
            case "delete":
                action = () -> new ConfigurationService().deleteWebHook();
            case "create":
                action = () -> new ConfigurationService().createWebHook("TODO");
            default:
                action = () -> "";
        }
        try {
            action.call();
            String code = request.getQueryParameters().get("code");
            return request.createResponseBuilder(HttpStatus.FOUND)
                    .header("Location", "webhookConfig?code=" + code)
                    .build();
        } catch (Exception e) {
            context.getLogger().log(Level.WARNING, e.getMessage(), e);
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage())
                    .build();
        }
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
