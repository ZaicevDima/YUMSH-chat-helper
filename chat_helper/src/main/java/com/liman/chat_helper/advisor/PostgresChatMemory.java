package com.liman.chat_helper.advisor;

import com.liman.chat_helper.model.Chat;
import com.liman.chat_helper.model.ChatEntry;
import com.liman.chat_helper.repository.ChatRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class PostgresChatMemory implements ChatMemory {

    private final ChatRepository chatRepository;

    public PostgresChatMemory(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public void add(String conversationId, List<Message> messages) {
        for (Message message : messages) {
            UUID chatId = UUID.fromString(conversationId);
            Chat chat = chatRepository.findById(chatId).orElseThrow();
            ChatEntry chatEntry = ChatEntry.toChatEntry(message);
            chat.addEntry(chatEntry);
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        UUID chatId = UUID.fromString(conversationId);
        Chat chat = chatRepository.findById(chatId).orElseThrow();

        return chat.getHistory().stream()
                .map(ChatEntry::toMessage)
                .limit(lastN)
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        // not implemented
    }
}
