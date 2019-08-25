var connection;

function initChatroom() {
    console.log("init chatroom.js")
    connection = new WebSocket('ws://scala-web-app.herokuapp.com/socket');
    connection.onopen = function(e) {
        console.log("connected");
        if(connection.readyState === WebSocket.OPEN){
            $("#status").html("Connected to: " + event.currentTarget.url);
            $("#messageField").removeAttr('disabled');
        }
    };
    connection.onclose = function(e) {
        console.log("connection is closed");
        $("#status").html("CONNECTION CLOSED");
        $("#status").removeClass("open");
        $("#messageField").attr('disabled', 'disabled');
        $("#reconnectButton").attr("disabled", false);
    };
    connection.onerror = function(error) {
        // just in there were some problems with connection...
        console.log('WebSocket Error: ' + error);
    };

    connection.onmessage = function(message) {
        console.log("message received: ");
        console.log(message);
        displayMessage(message.data);
    };

    $("#reconnectButton").attr("disabled", (connection.readyState !== WebSocket.CLOSED));


    console.log("chatroom initialized");
};

function sendMessage(){
    var msg = $("#messageField").val();
    var processedMsg = addInfo(msg);
    console.log("sending message : " + processedMsg);
    if(!msg) return;
    // send the message as an ordinary text
    connection.send(processedMsg);
    // clear out the message field
    $("#messageField").val('');
    // disable the input field to make the user wait until server
    // sends back response
//        messageField.attr('disabled', 'disabled');
};
function closeConnection() {
    if(connection != null) {
        console.log("closing connection");
        connection.close();
    }
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

function displayMessage(data) {
    console.log("displayMessage");
    $("#messagesContainer").children().last().removeClass("last-group");

    var parsedData = data.split(SEPARATOR);
    var authorId = parsedData[0];
    var authorUsername = parsedData[1];
    var receivedDate = parsedData[2];
    var msg = parsedData[3];

    console.log(parsedData[2]);
    var parsedTime = prettyPrintDate(receivedDate);
    //var time = parsedTime.getHours() + ":" + parsedTime.getMinutes();

    var rawDiv = $(document.createElement("div"));
    var userDiv = $(document.createElement("div"));
    var messageDiv = $(document.createElement("div"));
    var dateDiv = $(document.createElement("div"));

    rawDiv.addClass("group-rom");
    userDiv.addClass("first-part");
    messageDiv.addClass("second-part");
    dateDiv.addClass("third-part");

    userDiv.html(authorUsername);
    messageDiv.html(msg);
    dateDiv.html(parsedTime);

    if(authorId === localStorage.getItem("userId")) {
        // display self-messages
        userDiv.addClass("odd");
    }

    rawDiv.append(userDiv);
    rawDiv.append(messageDiv);
    rawDiv.append(dateDiv);

    $("#messagesContainer").append(rawDiv);
    $("#messagesContainer").children().last().addClass("last-group");

    var messagesContainerBody = document.querySelector('#messagesContainer');
    messagesContainerBody.scrollTop = messagesContainerBody.scrollHeight - messagesContainerBody.clientHeight;
};

const SEPARATOR = "|=|";
function addInfo(msg) {
    return localStorage.getItem("userId") + SEPARATOR
    + localStorage.getItem("username") + SEPARATOR
    + Date.now() + SEPARATOR
    + msg;
};

$(document).ready(function() {
    var sendButton = $("#sendButton");
    var reconnectButton = $("#reconnectButton");
    var messageField = $("#messageField");
    initChatroom();


    reconnectButton.on('click', reconnectButton, function() {
        if(connection.readyState === WebSocket.CLOSED)
            initChatroom();
    })


    /**
      * Send message when user presses Enter key
      */
    messageField.keydown(function(e) {
       if (e.keyCode === 13 && $("#messageField").val()) {
           sendMessage();
       }
    });

    sendButton.on('click', sendButton, function() {
        console.log("sendButton clicked");
        if($("#messageField").val())
            sendMessage();
    });
});
