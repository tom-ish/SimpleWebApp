var serverServices = {

    register: function () {
        console.log("entering serverServices.register")
        if(validateRegisterForm()) {
            const register_username = $("#register_username").val();
            const register_email = $("#register_email").val();
            const register_password = $("#register_password").val();

            $.post(
                validateRegisterRoute,
                { register_username, register_email, register_password, csrfToken },
                data => {
                    $("#contents").html(data)
                }
            )
        }
    },

    login: function () {
        console.log("entering serverServices.login")
        if(validateLoginForm()) {
            const login_email = $("#login_email").val();
            const login_password = $("#login_password").val();

            $.post(
                validateLoginRoute,
                { login_email, login_password, csrfToken },
                data => {
                    $("#contents").html(data)
                }
            )
        }
    }
}