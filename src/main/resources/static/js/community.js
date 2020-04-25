function post() {
    let questionId = $("#question_id").val();
    let commentContent = $("#comment_content").val();
    comment2target(questionId, 1, commentContent)
}

/**
 * 进行二级评论
 * @param e
 */
function two_comment(e) {
    let commentId = e.getAttribute("data-id");
    let content = $("#reply-" + commentId).val();
    comment2target(commentId, 2, content)
}

function comment2target(targetId, type, content) {
    if (!content) {
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
                        window.open("/login", "_self");
                    }
                }
            }
            console.log(response)
        },
        dataType: "json"
    });
}
function judgeTwoComment(commentId) {
    /**
     * 判断用户是否点赞了该二级评论
     */
    let flag;
    $.ajax({
        url: "/judgeLike",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        async: false,
        data: JSON.stringify({
            "outerid": commentId,
            "type": 5
        }),
        success: function (response) {
            //当前用户点赞了该问题
            if (response.code == 200) {
                console.log("点赞了该二级评论");
                flag =  true;
            }else {
                console.log("没有点赞该二级评论")
                flag = false;
            }
        }
    })
    return flag;
}
/**
 * 展示二级评论
 */
function collapseComments(e) {

    $(function () {
        let id = e.getAttribute("data-id");
        let icon = $("#" + id);
        let comments = $("#comment-" + id);
        //获取二级评论的展开状态
        let collapse = e.getAttribute("data-collapse");
        if (collapse === "in") {
            //收回二级评论
            icon.removeClass("add-comment-icon")
            comments.removeClass("in");
            e.setAttribute("data-collapse", "")
        } else {
            var subCommentContainer = $("#comment-" + id);
            if (subCommentContainer.children().length != 1) {
                //展开二级评论
                comments.addClass("in");
                //标记二级评论展开状态
                icon.addClass("add-comment-icon");
                e.setAttribute("data-collapse", "in");

            } else {
                $.getJSON("/comment/" + id, function (data) {
                    $.each(data.data.reverse(), function (index, comment) {
                        let onclickMethod;
                        let imgSrc;
                        /**
                         * 判断用户是否点赞了该二级评论
                         */
                        let flag = judgeTwoComment(comment.id);
                        if (flag){
                            $(this).children(".my-menu").children(".media-icon").children("img").addClass("question-like-flag")
                            onclickMethod = "";
                            imgSrc= "/images/svg/likeThree.svg";
                        }else {
                            onclickMethod = "twoCommentLike(this)";
                            imgSrc = "/images/svg/likeTwo.svg"
                        }

                        let mediaLeftElement = $("<div/>", {
                            "class": "media-left"
                        }).append($("<img/>", {
                            "class": "media-object img-rounded",
                            "src": comment.user.avatarUrl
                        }));

                        let mediaBodyElement = $("<div/>", {
                            "class": "media-body my-media-body two-comment-class",
                            "data-id":comment.id
                        }).append($("<h5/>", {
                            "class": "media-heading",
                            "html": comment.user.loginName
                        })).append($("<div/>", {
                            "class": "comment-desc",
                            "html": comment.content
                        })).append($("<div/>", {
                                "class": "pull-right my-menu text-desc"
                            }).append($("<span/>", {
                                "class": "media-icon",
                            }).append($("<img>",{
                                "class":"cursor-pointer",
                                "src": imgSrc,
                                "title": "点赞数",
                                "onclick": onclickMethod
                            })).append($("<span/>",{
                                "class":"question-icon text-desc",
                                "html":comment.likeCount
                            }))
                            ).append($("<span/>", {
                                "class": "pull-right text-desc comment-time-desc",
                                "html": new Date(comment.gmtCreate).format('yyyy-MM-dd HH:mm')

                            }))
                        );
                        let mediaElement = $("<div/>", {
                            "class": "media"
                        }).append(mediaLeftElement).append(mediaBodyElement);

                        let commentElement = $("<div/>", {
                            "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                        }).append(mediaElement);

                        subCommentContainer.prepend(mediaElement);
                    });
                    //标记二级评论展开状态
                    comments.addClass("in");
                    icon.addClass("add-comment-icon");
                    e.setAttribute("data-collapse", "in");
                });

            }
        }
    })
}

/**
 * 发布页面：选择标签
 * @param e
 */
function selectTag(e) {
    let value = e.getAttribute("data-tag");
    let previous = $("#tag").val();
    if (previous != null && previous != "") {
        let tags = previous.split(",")
        let flag = judge(tags, value);
        if (flag) {
            $("#tag").val(previous + ',' + value);
        }
        return;

    } else {
        $("#tag").val(value);
    }
}

function judge(tags, value) {
    let index;
    for (let i = 0; i < tags.length; i++) {
        if (tags[i] === value) {
            return false;
        }
    }
    return true;
}

//在publish页面展示标签栏
function showSelectTag() {
    $("#select-tag").show();
}

//定时器
// setTimeout(function () {
// },1000*5);

/*每日一句*/
// $.getJSON("https://api.ooopn.com/ciba/api.php?type=json",
//     function (data) {
//         $("#one-day-text-cn").text(data.ciba);
//         $("#one-day-time").text(data.date);
//         $("#one-day-img").attr("src", data.imgurl);
//     })

$.getJSON("https://interface.meiriyiwen.com/article/today?dev=1",
    function ({data}) {
        $("#everyday-title").text(data.title);
        // $("#article-author,#modal-article-author").text(data.author);
        $("#everyday-author,#modal-everyday-author").text(data.author);
        $("#everyday-summary").html("&emsp;&emsp;"+data.digest+"...");

        $("#modal-everyday-title").text(data.title);
        $("#modal-everyday-content").html(data.content);
        $("#modal-everyday-wc").text("共"+data.wc+"字");
    });



Date.prototype.format = function(fmt)
{
    var o = {
        "M+" : this.getMonth()+1, //月份
        "d+" : this.getDate(), //日
        "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
        "H+" : this.getHours(), //小时
        "m+" : this.getMinutes(), //分
        "s+" : this.getSeconds(), //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S" : this.getMilliseconds() //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}