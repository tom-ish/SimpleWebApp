console.log("script successfully loaded")

const csrfToken = $("#csrfToken").val();
const startRoute = $("#startRoute").val();

$("#contents").load(startRoute);

