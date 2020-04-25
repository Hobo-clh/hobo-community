$(function () {
    judgeLike();
    judgeQuestionLike();
})


/**
 * 点赞评论
 */
function likeCount(e) {
    let commentId = e.getAttribute("data-id");
    if ($("#"+commentId).siblings(".glyphicon-thumbs-up").is(".add-comment-icon")){
        return;
    }
    $.ajax({
        type: "POST",
        url: "/like",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "outerid": commentId,
            //点赞评论
            "type": 4
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    let isAccepted = window.confirm(response.message);
                    if (isAccepted) {
                        //跳转登录
                        window.open("/login", "_self");
                    }
                }
            }
        }
    })
}
/**
 * 判断当前用户是否点赞了该评论
 */
function judgeLike(){
    let likeIcon = $(".icon-message");
    likeIcon.each(function () {
        let commentId = $(this).attr("id");
        $.ajax({
            type: "POST",
            url: "/judgeLike",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                "outerid": commentId,
                "type" : 4
            }),
            success: function (response) {
                //当前用户点赞了该评论
                if (response.code==200){
                    $("#"+commentId).siblings(".glyphicon-thumbs-up").addClass("add-comment-icon");
                }
            }
        })
    })
}

/**
 * 点赞问题
 */
function questionLike() {
    let likeIcon = $(".icon-like")
    if(likeIcon.is(".question-like-flag")){
        return;
    }

    let questionId = $("#question_id").val();

    $.ajax({
        url: "/like",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "outerid": questionId,
            "type" : 3
        }),
        success: function (response) {
            //当前用户点赞了该问题
            if (response.code==200){
                window.location.reload();
            }else {
                if (response.code == 2003) {
                    let isAccepted = window.confirm(response.message);
                    if (isAccepted) {
                        //跳转登录
                        window.open("/login", "_self");
                    }
                }
            }
        }
    })
}

/**
 * 判断用户是否点赞了问题
 */
function judgeQuestionLike() {
    let questionId = $("#question_id").val();
    let likeIcon = $(".icon-like");
    $.ajax({
        url: "/judgeLike",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "outerid": questionId,
            "type" : 3
        }),
        success: function (response) {
            //当前用户点赞了该问题
            if (response.code==200){
                likeIcon.addClass("question-like-flag");
            }
        }
    })
}

/**
 * 二级评论点赞
 * @param e
 */
function twoCommentLike(e){

    if ($(e).is(".question-like-flag")) {
        console.log("直接返回")
        return;
    }

    let twoCommentId = $(e).parent().parent().parent().attr("data-id");
    $.ajax({
        type: "POST",
        url: "/like",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "outerid": twoCommentId,
            //点赞评论
            "type": 5
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    let isAccepted = window.confirm(response.message);
                    if (isAccepted) {
                        //跳转登录
                        window.open("/login", "_self");
                    }
                }
            }
        }
    })
}


