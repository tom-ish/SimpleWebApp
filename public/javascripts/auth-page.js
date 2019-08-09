console.log("script successfully loaded")

var csrfToken = $("#csrfToken").val();
var startRoute = $("#startRoute").val();
var validateLoginRoute = $("#validateLoginRoute").val();
var validateRegisterRoute = $("#validateRegisterRoute").val();

$("#contents").load(startRoute);

/*
function createErrorLog(id, msg) {
    console.log("relatedInput");
    console.log(id);
    var relatedInput = document.getElementById(id);
    relatedInput.setAttribute("aria-describedby", relatedInput.id+"_error");
    relatedInput.classList.add("is-invalid")
    var error_div = document.createElement("div");
    error_div.classList.add("invalid-feedback");
    return error_div;
}

var validator_login_email_error = createErrorLog("login_email", "Invalid username");
this.parentNode.insertBefore(validator_login_email_error, this.nextSibling);
validator_login_email.on('keyup', function() {
    validator_login_email_error.show();
});
const validator_login_password_error = createErrorLog(validator_login_password, "Invalid password");
this.parentNode.insertBefore(validator_login_password_error, this.nextSibling);
validator_login_password.on('keyup', function() {
    validator_login_password_error.show();
});

console.log("KEYUP")
const validator_register_username_error = createErrorLog(validator_register_username, "Invalid username");
this.parentNode.insertBefore(validator_register_username_error, this.nextSibling);
$("#register_username").on('keyup', function() {
    validator_register_username_error.show();
});
const validator_register_email_error = createErrorLog(validator_register_email, "Invalid email");
this.parentNode.insertBefore(validator_register_email_error, this.nextSibling);
validator_register_email.on('keyup', function() {
    validator_register_email_error.show();
});
const validator_register_password_error = createErrorLog(validator_register_password, "Invalid password");
this.parentNode.insertBefore(validator_register_password_error, this.nextSibling);
validator_register_password.on('keyup', function() {
    validator_register_password_error.show();
});
*/

function validateLoginForm() {
    console.log("validating login form")
    const validator_login_email = $("#login_email");
    const validator_login_password = $("#login_password");

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
    console.log("validating register form");
    const validator_register_username = $("#register_username");
    const validator_register_email = $("#register_email");
    const validator_register_password = $("#register_password");

    if(validator_register_username.val().length < 6 || validator_register_username.val().length > 32) {
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