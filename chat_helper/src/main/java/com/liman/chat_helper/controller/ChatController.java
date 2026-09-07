package com.liman.chat_helper.controller;

import com.liman.chat_helper.model.Chat;
import com.liman.chat_helper.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/")
    public String mainPage(ModelMap model) {
        model.addAttribute("chats", chatService.getAllChats());
        return "chat";
    }

    @GetMapping("/chat/{chatId}")
    public String showChat(@PathVariable UUID chatId, ModelMap model) {
        model.addAttribute("chats", chatService.getAllChats());
        model.addAttribute("chat", chatService.getChat(chatId));

        return "chat";
    }

    @PostMapping("/chat/new")
    public String createChat(@RequestParam String title) {
        Chat chat = chatService.createNewChat(title);

        return "redirect:/chat/" + chat.getId();
    }

    @PostMapping("/chat/{chatId}/delete")
    public String deleteChat(@PathVariable UUID chatId) {
        chatService.deleteChatById(chatId);

        return "redirect:/";
    }
}
