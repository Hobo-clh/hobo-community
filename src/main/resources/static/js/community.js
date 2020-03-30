function post() {
    let questionId = $("#question_id").val();
    let commentContent = $("#comment_content").val();
    comment2target(questionId,1,commentContent)
}

function two_comment(e) {
    let commentId = e.getAttribute("data-id");
    let content = $("#reply-"+commentId).val();
    comment2target(commentId,2,content)
}

function comment2target(targetId,type,content) {
    if(!content){
        alert("内容不能为空");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        //stringify方法将js对象转换为字符串
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code === 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    let isAccepted = window.confirm(response.message);
                    if (isAccepted) {
                        //跳转登录
                        window.open("https://github.com/login/oauth/authorize?client_id=Iv1.e1445cb4e1c12491&redirect_uri=http://39.97.226.211/callback&scope=user&state=1","_self");
                        window.localStorage.setItem("closable", "true");
                        window.localStorage.setItem("questionId",targetId);
                    }
                }
            }
            console.log(response)
        },
        dataType: "json"
    });
}


/**
 * 展示二级评论
 */
function collapseComments(e) {

    let id = e.getAttribute("data-id");
    let icon = $("#"+id);
    let comments = $("#comment-"+id);
    //获取二级评论的展开状态
    let collapse = e.getAttribute("data-collapse");
    if (collapse === "in"){
        //收回二级评论
        icon.removeClass("add-comment-icon")
        comments.removeClass("in");
        e.setAttribute("data-collapse","")
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            //标记二级评论展开状态
            icon.addClass("add-comment-icon");
            e.setAttribute("data-collapse","in")
        }else {
            $.getJSON("/comment/"+id,function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    let mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    let mediaBodyElement = $("<div/>", {
                        "class": "media-body my-media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.loginName
                    })).append($("<div/>", {
                        "class": "comment-desc",
                        "html": comment.content
                    })).append($("<div/>", {
                            "class": "my-menu text-desc"
                    }).append($("<span/>",{
                        "class": "glyphicon glyphicon-thumbs-up icon"
                    }))
                        // .append($("<span/>", {
                        //     "class": "pull-right",
                        //     "html": moment(comment.gmtCreate).format('YYYY-MM-dd HH:mm')
                        // }))
                    );

                    let mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    // let commentElement = $("<div/>", {
                    //     "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    // }).append(mediaElement);

                    subCommentContainer.prepend(mediaElement);
                });
                //标记二级评论展开状态
                comments.addClass("in");
                icon.addClass("add-comment-icon");
                e.setAttribute("data-collapse","in")
            });
        }
    }
}

function selectTag(e) {
    let value = e.getAttribute("data-tag");
    let previous = $("#tag").val();
    if (previous!=null&&previous!=""){
        let tags = previous.split(",")
        var flag = judge(tags,value)
        if(flag){
            $("#tag").val(previous+','+value);
        }
        return;

    }else{
        $("#tag").val(value);
    }
}

function judge(tags,value){
    let index;
    for(let i=0;i<tags.length;i++){
        if (tags[i]===value){
            return false;
        }
    }return true;
}

function showSelectTag() {
    $("#select-tag").show();
}