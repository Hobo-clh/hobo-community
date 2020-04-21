
$(function () {
    $(".email-signup").hide();
})
function loginForm() {
    $(".email-login").delay(100).fadeIn(100);;
    $(".email-signup").fadeOut(100);
    $("#login-box-link").addClass("active");
    $("#signup-box-link").removeClass("active");
}
function registerForm() {
    $(".email-login").fadeOut(100);
    $(".email-signup").delay(100).fadeIn(100);
    $("#login-box-link").removeClass("active");
    $("#signup-box-link").addClass("active");
}

function sendCode() {
    let email = $("#email").val()
    if(email==null||email==""){
        alert("邮箱号不能为空")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/checkRegister",
        //stringify方法将js对象转换为字符串
        data: {
            "email": email,
        },
        dataType: "json",
        success: function (response) {
            if (response.code === 200) {
                invokeSetTime("#getCode")
                $.ajax({
                    url: "/sendCode",
                    type: "POST",
                    data: {
                        "email": email
                    }
                })
            } else {
                alert(response.message)
            }
        },
    });
}



function register() {
    let email = $.trim($("#email").val());
    let pwd = $.trim($("#pwd").val());
    let code = $.trim($("#code").val());

    if(email==null||email==""){
        alert("邮箱号不能为空")
        return;
    }
    if(pwd ===null||pwd ===""){
        alert("密码不能为空")
        return;
    }
    if(code==null||code==""){
        alert("请填写验证码！")
        return;
    }

    $.ajax({
        type: "POST",
        url: "/register",
        //stringify方法将js对象转换为字符串
        data: {
            "accountId": email,
            "password":pwd,
            "verityCode":code
        },
        dataType: "json",
        success: function (response) {
            if (response.code === 200) {
                alert("注册成功，快去登录吧！");
                window.open("/login", "_self");
            } else {
                alert(response.message)
            }
        },
    });
}

function login() {
    let email = $("#loginEmail").val();
    let pwd = $.trim($("#loginPwd").val());
    if(email==null||email.trim().length === 0){
        alert("邮箱号不能为空")
        return;
    }
    if(pwd ===null||pwd ===""){
        alert("密码不能为空")
        return;
    }

    $.ajax({
        type: "POST",
        url: "/login",
        data: {
            "accountId": email,
            "pwd":pwd
        },
        dataType: "json",
        success: function (response) {
            if (response.code === 200) {
                alert("登录成功！");
                window.open("/", "_self");
            } else {
                alert(response.message)
            }
        },
    });
}
function develop() {
    alert("开发中...");
    return;
}

