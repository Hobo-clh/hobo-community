$(function () {
    $(".modify-pwd").hide();
})

//找回密码
function sendCodePwd() {
    let email = $("#email").val()
    if (email == null || email == "") {
        alert("邮箱号不能为空")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/verifyUser",
        data: {
            "email": email,
        },
        dataType: "json",
        success: function (response) {
            if (response.code === 200) {
                invokeSetTime("#getCode")
                // alert("验证码已发送，请在5分钟内完成修改")
                $.ajax({
                    type: "POST",
                    url: "/gbPwd",
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

function verifyCode() {
    let email = $("#email").val()
    let code = $("#code").val();
    if (email == null || email == "") {
        alert("邮箱号不能为空")
        return;
    }
    if (code == null || code == "") {
        alert("邮箱号不能为空")
        return;
    }
    $.ajax({
        url: "/verifyCode",
        type: "POST",
        data: {
            "email": email,
            "code": code
        },
        dataType: "json",
        success: function (response) {
            if (response.code === 200) {
                alert("验证码正确")
                $(".modify-pwd").html("<form class=\"email-modify-pwd\">\n" +
                    "        <div class=\"u-form-group\">\n" +
                    "            <input type=\"password\" id=\"pwd\" placeholder=\"密码\"/>\n" +
                    "        </div>\n" +
                    "        <div class=\"u-form-group\">\n" +
                    "            <input type=\"password\" id=\"confirmPwd\" placeholder=\"确认密码\"/>\n" +
                    "        </div>\n" +
                    "        <div class=\"u-form-group\">\n" +
                    "            <input type=\"button\" onclick=\"changePwd()\" value=\"确认修改\">\n" +
                    "        </div>\n" +
                    "    </form>")
                $(".modify-pwd").delay(100).fadeIn(100);
                $(".email-verify-code").fadeOut(100);
            } else {
                alert(response.message)
            }
        },
    })
}

function changePwd() {
    let email = $("#email").val();
    let pwd = $.trim($("#pwd").val())
    let confirmPwd = $.trim($("#confirmPwd").val())
    if (pwd !== confirmPwd) {
        alert("密码不一致，请重新输入");
        $("#pwd").attr("value", "");
        $("#confirmPwd").attr("value", "");
        return;
    }
    $.ajax({
        url: "/modifyPwd",
        type: "POST",
        data: {
            "email": email,
            "password": pwd
        },
        dataType: "json",
        success: function (response) {
            if (response.code == 200) {
                alert("密码修改成功，快去登录吧！");
                window.open("/login", "_self");
            } else {
                alert(response.message);
            }
        }
    })
}

/**
 * 发送邮件后定时60秒
 * @param obj
 */
function invokeSetTime(obj) {
    let countdown = 60;
    setTime(obj);

    function setTime(obj) {
        if (countdown === 0) {
            $(obj).attr("disabled", false);
            $(obj).attr("value", "重新获取验证码");
            countdown = 60;
            return;
        } else {
            $(obj).attr("disabled", true);
            $(obj).attr("value", countdown + "s 重新发送");
            countdown--;
        }
        setTimeout(function () {
            setTime(obj)
        }, 1000);
    }
}

