$(function(){

    fetch_friend_list()

})

var fetch_friend_list = function(){
    $.ajax({
        url:"/user/fetchUserFriendsList",
        type:"get",
        async:false,
        contentType:false,
        success:function(res) {
            if (res.success) {
                console.log("登陆成功,跳转到首页");
                //window.location.href = window.location.href.substr(0, window.location.href.lastIndexOf("/"));

            } else {
                console.error("拉取用户好友列表失败, 错误原因:" + res.resultMsg);
            }
        }
    })
}