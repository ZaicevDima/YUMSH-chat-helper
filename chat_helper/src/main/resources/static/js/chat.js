document.addEventListener("DOMContentLoaded", function() {
    const sendButton = document.getElementById("send-button");
    const chatInput = document.getElementById("chat-input");

    sendButton.addEventListener("click", function() {
        const prompt = chatInput.value;
        if (!prompt) return;
        chatInput.value = "";

        const pathParts = window.location.pathname.split("/");
        const chatId = pathParts[pathParts.length - 1];

        // Просто отправляем форму через fetch
        const formData = new FormData();
        formData.append('prompt', prompt);

        fetch(`/chat/${chatId}/entry`, {
            method: 'POST',
            body: formData
        }).then(() => {
            // Перезагружаем страницу, чтобы увидеть ответ
            window.location.href = `/chat/${chatId}`;
        });
    });

    // Отправка по Enter (Shift+Enter для переноса)
    chatInput.addEventListener("keydown", function(e) {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            sendButton.click();
        }
    });
});