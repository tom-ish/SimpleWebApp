console.log("script successfully loaded")

var csrfToken = $("#csrfToken").val();
var startRoute = $("#startRoute").val();
var validateLoginRoute = $("#validateLoginRoute").val();
var validateRegisterRoute = $("#validateRegisterRoute").val();

$("#contents").load(startRoute);


function validateLoginForm() {
    console.log("validating login form")
    const validator_login_email = $("#login_email")
    const validator_login_password = $("#login_password")

    if(validator_login_email.val().indexOf("@") == -1
    || validator_login_email.val().indexOf("@") == validator_login_email.val().length-1) {
        window.alert("Please enter a valid e-mail address.");
        validator_login_email.focus();
        return false;
    } else if(validator_login_password.val().length < 6 || validator_login_password.val().length > 32) {
        window.alert("Your password must contains between 6 and 32 characters.");
        validator_login_password.focus();
        return false;
    } else
        return true;
}

function validateRegisterForm() {
    console.log("validating register form")
    const validator_register_username = $("#register_username")
    const validator_register_email = $("#register_email")
    const validator_register_password = $("#register_password")

    if(validator_register_username.val().length < 6 || validtor_register_username.val().length > 32) {
        window.alert("Your username must contains between 6 and 32 characters.");
        validator_register_username.focus();
        return false;
    } else if(validator_register_email.val().indexOf("@") == -1
    || validator_register_email.val().indexOf("@") == validator_register_email.val().length-1){
        window.alert("Please enter a valid e-mail address.");
        validator_register_password.focus();
        return false;
    } else if(validator_register_password.val().length < 6 || validator_register_password.val().length > 32) {
        window.alert("Your password must contains between 6 and 32 characters.");
        validator_register_password.focus();
        return false;
    } else
        return true;
}