function register() {
    let loginName = $.trim($("#loginName").val());
    let password = $.trim($("#password").val());
    let messageLabel = $("#message");
    let continuePwd = $.trim($("#continuePwd").val());

    if (loginName == null || loginName === "") {
        messageLabel.html("请输入用户名").css("color","red");
        return;
    }
    if (password == null || password === "") {
        messageLabel.html("密码不能未空").css("color","red");
        return;
    }
    if (continuePwd != password) {
        messageLabel.html("两次密码不一致").css("color","red");
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
                messageLabel.html("用户名已存在~").css("color","red");
            }
            console.log(response)

        },

    });
}
//入口函数
$(function () {
    let messageLabel = $("#message");

    //鼠标聚焦在userName输入框
    $(":input:eq(0)").focus(function () {
        $("#message").text("");
    })


    $("#continuePwd").blur(function () {
        let pwd = $("#password").val();
        let continuePwd = $("#continuePwd").val();
        if (pwd!=continuePwd){
            messageLabel.text("两次密码不一致！").css("color","red");
        }else {
            messageLabel.text("");
        }
    })

})

function registerBlur() {
    let loginName = $.trim($("#loginName").val());
    let messageLabel = $("#message");
    if (loginName == null || loginName === "") {
        messageLabel.text("用户名不能为空！").css("color","red");
        return;
    }

    $.ajax({
        type: "post",
        contentType: "application/json",
        url: "/verify",
        data: JSON.stringify({
            "loginName": loginName
        }),
        success: function (response) {
            console.log(response.code);
            console.log(response.message);
            if (response.code == 200) {
                messageLabel.text("用户名可用！").css("color","green");
            } else {
                messageLabel.text("用户名已经存在，换个试试吧！").css("color","red");
            }
        }
    })
}