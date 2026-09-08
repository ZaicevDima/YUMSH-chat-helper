package com.liman.chat_helper.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.ai.chat.messages.Message;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static ChatEntry toChatEntry(Message message) {
        Role messageRole = getRoleFromMessage(message);

        return ChatEntry.builder()
                .role(messageRole)
                .content(message.getText())
                .build();
    }

    private static Role getRoleFromMessage(Message message) {
        String messageRoleValue = message.getMessageType().getValue();
        return Role.getRole(messageRoleValue);
    }

    public Message toMessage() {
        return role.getMessage(content);
    }
}