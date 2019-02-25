// jQuery库冲突解决方案
//var $j = jQuery.noConflict();

$(function(){

    fetch_friend_list()

});

var fetch_friend_list = function(){
    console.log("拉取用户好友列表...");
    var templ_friend_list = function(t){
        var _f_list_content =
           ' <ul id="friend_list_ul">'+
           '{{each(i, _user_friend) friend}}'+
           '     <li id="${_user_friend.id}">'+
           '         ${_user_friend.userName}'+
           '     </li>'+
           '{{/each}}'+
           ' </ul>';

        $.template("_user_friends_templ", _f_list_content);

        $.tmpl("_user_friends_templ", t).appendTo("#_user_friends_list");
    }

    $.ajax({
        url:"/user/fetchUserFriendsList",
        type:"post",
        async:true,
        contentType:false,
        success:function(res) {
            if (res.success) {
                console.log("拉取好友列表成功...加载好友列表");
                // 好友列表
                templ_friend_list(res.t)
            } else {
                console.error("拉取用户好友列表失败, 错误原因:" + res.resultMsg);
                return
            }
        }
    })


}