function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": "1"
        }),
        success:function (response) {
            if(response.code == 200){
                window.location.reload();
                // $("#comment_section").hide();
            }else{
                // 2003对应未登录，此时打开新页面进行登录，随后关闭登录页面
                // 使用localstorage放置一个变量来进行页面间通信
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=672c37506027960fcb57&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        // 关于localstorage：https://www.runoob.com/jsref/prop-win-localstorage.html
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        error:function () {

        }
    });
}