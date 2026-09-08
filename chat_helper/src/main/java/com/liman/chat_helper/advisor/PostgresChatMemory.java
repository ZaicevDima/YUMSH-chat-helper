package com.liman.chat_helper.advisor;

import com.liman.chat_helper.model.Chat;
import com.liman.chat_helper.model.ChatEntry;
import com.liman.chat_helper.repository.ChatRepository;
import lombok.Builder;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Builder
public class PostgresChatMemory implements ChatMemory {

    private ChatRepository chatMemoryRepository;

    private int maxMessages;

    @Override
    public void add(String conversationId, List<Message> messages) {
        UUID chatId = UUID.fromString(conversationId);
        Chat chat = chatMemoryRepository.findById(chatId).orElseThrow();

        for (Message message : messages) {
            ChatEntry chatEntry = ChatEntry.toChatEntry(message);
            chat.addEntry(chatEntry);
        }

        chatMemoryRepository.save(chat);
    }

    @Override
    public List<Message> get(String conversationId) {
        UUID chatId = UUID.fromString(conversationId);
        Chat chat = chatMemoryRepository.findById(chatId).orElseThrow();

        return chat.getHistory().stream()
                .sorted(Comparator.comparing(ChatEntry::getCreatedAt).reversed())
                .map(ChatEntry::toMessage)
                .limit(maxMessages)
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        // not implemented
    }
}
