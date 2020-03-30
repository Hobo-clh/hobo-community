function register() {
    let loginName = $("#loginName").val();
    let password = $("#password").val();
    let continuePwd = $("#continuePwd").val();

    if (loginName == null || loginName === "") {
        $("#message").html("请输入用户名")
        return;
    }
    if (password == null || password ===""){
        $("#message").html("密码不能未空")
        return;
    }
    if (continuePwd!=password){
        $("#message").html("两次密码不一致错误")
        return;
    }
        $.ajax({
            type: "POST",
            url: "/register",
            contentType: "application/json",
            //stringify方法将js对象转换为字符串
            data: JSON.stringify({
                "loginName": loginName,
                "password": password
            }),
            dataType: "json",
            success: function (response) {
                if (response.code === 200) {
                    alert(response.message)
                    window.open("/login", "_self");
                } else {
                    $("#message").html("用户名已存在~")
                }
                console.log(response)
            },

        });
}