//    sidebar toggle
$(function() {
function responsiveView() {
  var wSize = $(window).width();
  if (wSize <= 768) {
    $('#container').addClass('sidebar-close');
    $('#sidebar > ul').hide();
  }

  if (wSize > 768) {
    $('#container').removeClass('sidebar-close');
    $('#sidebar > ul').show();
  }
}
$(window).on('load', responsiveView);
$(window).on('resize', responsiveView);
});

var landingRoute = $("#landingRoute").val();
var chatroomRoute = $("#chatroomRoute").val();

$("#main-content").load(landingRoute)

var sideMenu = {
    updateSideMenuDisplay: function() {
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
        } else {
          $('#main-container').css({
            'margin-left': '210px'
          });
          $('#sidebar > ul').show();
          $('#sidebar').css({
            'margin-left': '0'
          });
          $("#container").removeClass("sidebar-closed");
        }
    },
    chatroomView: function() {
        console.log("updating chatroom view");
        $("#main-content").load(chatroomRoute)
    }
}