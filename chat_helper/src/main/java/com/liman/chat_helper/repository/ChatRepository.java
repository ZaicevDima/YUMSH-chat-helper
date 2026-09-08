package com.liman.chat_helper.repository;

import com.liman.chat_helper.model.Chat;
import com.liman.chat_helper.model.ChatEntry;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID>, ChatMemoryRepository {
    @Override
    default List<String> findConversationIds() {
        return findAll().stream()
                .map(Chat::getId)
                .map(UUID::toString)
                .toList();
    }

    @Override
    default List<Message> findByConversationId(String conversationId) {
        UUID chatId = UUID.fromString(conversationId);
        Chat chat = findById(chatId).orElseThrow();
        return chat.getHistory().stream()
                .map(ChatEntry::toMessage)
                .toList();
    }

    @Override
    default void saveAll(String conversationId, List<Message> messages) {
        Chat chat = findById(UUID.fromString(conversationId)).orElseThrow();

        messages.stream()
                .map(ChatEntry::toChatEntry)
                .forEach(chat::addEntry);

        save(chat);
    }

    @Override
    default void deleteByConversationId(String conversationId) {
        // not implemented
    }
}
