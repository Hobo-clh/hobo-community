function post() {
    let questionId = $("#question_id").val();
    let commentContent = $("#comment_content").val();
    if(!commentContent){
        alert("内容不能为空");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        //stringify方法将js对象转换为字符串
        data: JSON.stringify({
            "parentId": questionId,
            "content": commentContent,
            "type": 1
        }),
        success: function (response) {
            if (response.code === 200) {
                //$("#comment-section").hide();
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    let isAccepted = window.confirm(response.message);
                    if (isAccepted) {
                        //跳转登录
                        window.open("/login","_self");
                        window.localStorage.setItem("closable", "true");
                        window.localStorage.setItem("questionId",questionId);
                    }
                }
            }
            console.log(response)
        },
        dataType: "json"
    });

}