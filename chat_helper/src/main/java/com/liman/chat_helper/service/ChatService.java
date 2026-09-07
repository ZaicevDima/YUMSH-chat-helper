package com.liman.chat_helper.service;

import com.liman.chat_helper.model.Chat;
import com.liman.chat_helper.repository.ChatRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
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
}
