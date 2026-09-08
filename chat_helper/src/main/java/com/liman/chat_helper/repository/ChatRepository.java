package com.liman.chat_helper.repository;

import com.liman.chat_helper.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
