package com.liman.chat_helper.controller;

import com.liman.chat_helper.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
public class StreamingChatController {

    private final ChatService chatService;

    public StreamingChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "/chat-stream/{chatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter talkToModel(@PathVariable UUID chatId, @RequestParam(name = "userPrompt") String prompt) {
        return chatService.proceedInteractionWithStreaming(chatId, prompt);
    }
}
