$(document).ready(function() {
    console.log("sideMenu document ready");
    var chatroomRoute = $("#chatroomRoute").val();
    var dashboardRoute = $("#dashboardRoute").val();

    var mainContent = $("#main-content");
    var activeTab = $("#dashboardTab");




    $("header").on('click', '#sideMenuToggle', function() {
        console.log("fa-bars click");
        if ($('#sidebar > ul').is(":visible") === true) {
          $('#main-container').css({
            'margin-left': '0px'
          });
          $('#sidebar').css({
            'margin-left': '-210px'
          });
          $('#sidebar > ul').hide();
          $("#container").addClass("sidebar-closed");
        } else {/*
          $('#main-container').css({
            'margin-left': '210px'
          });*/
          $('#sidebar > ul').show();
          $('#sidebar').css({
            'margin-left': '0'
          });
          $("#container").removeClass("sidebar-closed");
        }
    });

    $("#sidebar").on('click', '#dashboardTab', function() {
        console.log("dashboard tab clicked - updating dashboard view");
        activeTab.toggleClass("active");
        $("#dashboardTab").toggleClass("active");

        if(activeTab.id == $("#chatroomTab").id)
            closeConnection();
        activeTab = $("#dashboardTab");
        mainContent.load(dashboardRoute);
    });

    $("#sidebar").on('click', '#chatroomTab', function() {
        console.log("chatroom tab clicked - updating chatroom view");
        activeTab.toggleClass("active");
        $("#chatroomTab").toggleClass("active");

        activeTab = $("#chatroomTab");
        mainContent.load(chatroomRoute);


       

        //initChatroom();
//
//        console.log("init chatroom.js")
//        connection = new WebSocket('ws://127.0.0.1:9000/socket');
//        connection.onopen = function(e) {
//            console.log("connected");
//            if(connection.readyState === WebSocket.OPEN){
//                socketStatus.innerHTML = "Connected to: " + event.currentTarget.url;
//                console.log(socketStatus);
//                connection.send('Hello, Server!!');
//                console.log(connection);
//                messageField.removeAttr('disabled');
//            }
//        };
//        connection.onclose = function(e) {
//            console.log("connection is closed");
//            socketStatus.html("CONNECTION CLOSED");
//            socketStatus.removeClass("open");
//            console.log(connection);
//            messageField.attr('disabled', 'disabled');
//        };
//        connection.onerror = function (error) {
//            // just in there were some problems with connection...
//            console.log('WebSocket Error: ' + error);
//        };
//
//        connection.onmessage = function(message) {
//            console.log("message received: " + message.data)
//        };
//
//        console.log("chatroom initialized");

    });
});