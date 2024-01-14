let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe('/topic/boxchat/{boxchatId}', onMessageReceived);
}

function onError(error) {
    console.log(`Could not connect to WebSocket server: ${error}`);
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    // Process the received message and display in chatMessages div
    const chatMessages = document.getElementById('chatMessages');
    const messageElement = document.createElement('div');
    messageElement.textContent = message.content; // Assuming 'content' is part of the message
    chatMessages.appendChild(messageElement);
}

function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const messageContent = messageInput.value.trim();
    if (stompClient && messageContent) {
        const message = {
            content: messageContent
        };
        stompClient.send('/app/chat.sendMessage/{boxchatId}', {}, JSON.stringify(message));
        messageInput.value = '';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const sendButton = document.getElementById('sendButton');
    sendButton.addEventListener('click', sendMessage);

    connect(); // Connect to WebSocket when the page loads
});
