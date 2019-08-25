$("#infoContainer").html(
    localStorage.getItem("userId") + " - " + localStorage.getItem("username") +
     " \n "+ localStorage.getItem("expirationDate")
);