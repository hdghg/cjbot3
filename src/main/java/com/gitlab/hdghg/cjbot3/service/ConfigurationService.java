package com.gitlab.hdghg.cjbot3.service;

import com.gitlab.hdghg.cjbot3.exception.ActionFailedException;
import com.pengrad.telegrambot.response.BaseResponse;

import static java.text.MessageFormat.format;

public class ConfigurationService {

    public String deleteWebHook() throws ActionFailedException {
        BaseResponse r;
        try {
            r = MessageService.forEnvironment().deleteWebhook();
        } catch (Exception e) {
            String text = format("Failed to delete webhook {0} {1}", e.getClass().getSimpleName(), e.getMessage());
            throw new ActionFailedException(text, e);
        }
        if (r.isOk()) {
            return "webhook deleted";
        }
        var text = format("Could not delete webhook, code: {0}, message: {1}", r.errorCode(), r.description());
        throw new ActionFailedException(text);
    }

    public String activateWebHook(String url) throws ActionFailedException  {
        BaseResponse r;
        try {
            r = MessageService.forEnvironment().activateWebhook(url);
        } catch (Exception e) {
            String text = format("Failed to create webhook {0} {1}", e.getClass().getSimpleName(), e.getMessage());
            throw new ActionFailedException(text, e);
        }
        if (r.isOk()) {
            return "webhook created for url: " + url;
        }
        var text = format("Could not create webhook, code: {0}, message: {1}", r.errorCode(), r.description());
        throw new ActionFailedException(text);
    }
}
