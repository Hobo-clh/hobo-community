function login() {
    let loginName = $("#loginName").val();
    let password = $("#password").val();
    let messageLabel = $("#message");

    if (loginName == null || loginName == "") {
        alert("请输入用户名");
        $("#message").html("请输入用户名")
        return;
    }
    if (password == null || password ==""){
        alert("密码不能未空")
        $("#message").html("密码不能未空")
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
                    $("#message").html("用户名或者密码错误")
                }
                console.log(response)
            },

        });
}