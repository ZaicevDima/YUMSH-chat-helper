package com.liman.chat_helper.service;

import com.liman.chat_helper.model.Chat;
import com.liman.chat_helper.model.ChatEntry;
import com.liman.chat_helper.model.Role;
import com.liman.chat_helper.repository.ChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.liman.chat_helper.model.Role.ASSISTANT;
import static com.liman.chat_helper.model.Role.USER;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatClient chatClient;

    @Autowired
    private ChatService myProxy;

    public ChatService(ChatRepository chatRepository, ChatClient chatClient) {
        this.chatRepository = chatRepository;
        this.chatClient = chatClient;
    }


    public List<Chat> getAllChats() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Chat getChat(UUID id) {
        return chatRepository.findById(id).orElseThrow(); // TODO добавить кастомное исключение
    }

    public Chat createNewChat(String title) {
        Chat chat = Chat.builder()
                .title(title)
                .build();

        return chatRepository.save(chat);
    }

    public void deleteChatById(UUID chatId) {
        chatRepository.deleteById(chatId);
    }

    @Transactional
    public void proceedInteraction(UUID chatId, String prompt) {
        myProxy.addChatEntry(chatId, prompt, USER);
        String answer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        myProxy.addChatEntry(chatId, answer, ASSISTANT);
    }

    @Transactional
    public void addChatEntry(UUID chatId, String prompt, Role role) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();// TODO добавить кастомное исключение
        ChatEntry entry = ChatEntry.builder()
                .content(prompt)
                .role(role)
                .build();
        chat.addEntry(entry);
    }
}
