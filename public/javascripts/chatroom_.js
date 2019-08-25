var connection;

var socketStatus = $("#status");
var messageField = $("#message");
var sendButton = $("#send");

var chatroom = {
    init: function() {
        console.log("init chatroom.js")
        connection = new WebSocket('ws://127.0.0.1:9000/socket');
        connection.onopen = function(e) {
            console.log("connected");
            if(connection.readyState === WebSocket.OPEN){
                socketStatus.html("Connected to: " + event.currentTarget.url);
                console.log(socketStatus);
                connection.send('Hello, Server!!');
                console.log(connection);
                messageField.removeAttr('disabled');
            }
        };
        connection.onclose = function(e) {
            console.log("connection is closed");
            socketStatus.html("CONNECTION CLOSED");
            socketStatus.removeClass("open");
            console.log(connection);
            messageField.attr('disabled', 'disabled');
        };
        connection.onerror = function (error) {
            // just in there were some problems with connection...
            console.log('WebSocket Error: ' + error);
        };

        connection.onmessage = function(message) {
            console.log("message received: " + message.data)
        };

        /**
          * Add message to the chat window
          */
        function addMessage(author, message, color, dt) {
            content.prepend('<p><span style="color:' + color + '">'
               + author + '</span> @ ' + (dt.getHours() < 10 ? '0'
               + dt.getHours() : dt.getHours()) + ':'
               + (dt.getMinutes() < 10
                 ? '0' + dt.getMinutes() : dt.getMinutes())
               + ': ' + message + '</p>');
        };
        console.log("chatroom initialized");
    },
    sendMessage: function() {
        var messageField = $('#message');
        var sendButton = $('#send');
        console.log("sendMessage " + messageField.val());
        function send(e) {
           e.preventDefault();
           var msg = messageField.val();
           console.log("sending message : " + msg);
           console.log(connection);
           if(!msg) return;
           // send the message as an ordinary text
           connection.send(msg);
           // clear out the message field
           messageField.val('');
           // disable the input field to make the user wait until server
           // sends back response
           messageField.attr('disabled', 'disabled');
           return false;
        };

        /**
          * Send message when user presses Enter key
          */
        messageField.keydown(function(e) {
           if (e.keyCode === 13) {
               send(e);
           }
        });

        sendButton.click(function(e) {
            console.log("form submit");
            send(e);
        });
    },
    closeConnection: function() {
        if(connection != null) {
            console.log("closing connection");
            connection.close();
        }
    }
}

