/*
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
*/

console.log("landing.js called");
var landingRoute = $("#landingRoute").val();
var userId = $("#userId").val();
var username= $("#username").val();
var expirationDate = $("#expirationDate").val();

localStorage.setItem("userId", userId);
localStorage.setItem("username", username);
localStorage.setItem("expirationDate", expirationDate);

$("#main-content").load(landingRoute);


var prettyPrintDate = function (dateStr) {
    console.log(dateStr);
    var time = new Date(dateStr);
    var hours = time.getHours();
    var minutes = time.getMinutes();
    console.log(time);
    console.log(hours);
    console.log(minutes);
    return hours + ":" + minutes;
};