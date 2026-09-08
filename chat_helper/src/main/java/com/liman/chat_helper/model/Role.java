package com.liman.chat_helper.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER("user") {
        @Override
        Message getMessage(String message) {
            return new UserMessage(message);
        }
    },
    ASSISTANT("assistant") {
        @Override
        Message getMessage(String message) {
            return new AssistantMessage(message);
        }
    },
    SYSTEM("system") {
        @Override
        Message getMessage(String message) {
            return new SystemMessage(message);
        }
    };

    private final String role;

    public static Role getRole(String roleName) {
        return Arrays.stream(Role.values())
                .filter(role -> Objects.equals(role.role, roleName))
                .findFirst()
                .orElseThrow(); // TODO добавить кастомное исключение
    }

    abstract Message getMessage(String prompt);
}
