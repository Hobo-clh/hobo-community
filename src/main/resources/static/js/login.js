function login() {
    let loginName = $("#loginName").val();
    let password = $("#password").val();
    let messageLabel = $("#message");

    if (loginName == null || loginName == "") {
        alert("请输入用户名");
        messageLabel.html("请输入用户名").css("color","red");
        return;
    }
    if (password == null || password ==""){
        alert("密码不能未空")
        messageLabel.html("密码不能未空").css("color","red");
        return;
    }
        $.ajax({
            type: "POST",
            url: "/login",
            contentType: "application/json",
            //stringify方法将js对象转换为字符串
            data: JSON.stringify({
                "loginName": loginName,
                "password": password
            }),
            dataType: "json",
            success: function (response) {
                if (response.code === 200) {
                    window.open("/", "_self");
                } else {
                    messageLabel.html("用户名或者密码错误").css("color","red");
                }
                console.log(response)
            },

        });
}