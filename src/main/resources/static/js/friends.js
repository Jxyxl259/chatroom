// jQuery库冲突解决方案
//var $j = jQuery.noConflict();

$(function(){

    fetch_friend_list()

});

var fetch_friend_list = function(){
    console.log("拉取用户好友列表...");

    // 好友列表 ul
    var templ_friend_list = function(t){
        var _f_list_content =
           '<p>我的好友</p>'+
           ' <ul style="padding-left: 20px" id="friend_list_ul" type="none">'+
           '{{each(i, _user_friend) friend}}'+
           '     <li id="${_user_friend.id}">'+
           '        <div style="display: inline;cursor:pointer;" onclick="show_chat_tab(this)">' +
            '           <img width="30" height="30" src="./../img/qq_icon.png"/><span>${_user_friend.userName}</span>' +
            '       </div>'+
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
};


/**
 * 点击好友头像，弹出聊天对话框
 * //TODO 拉取相对应的聊天记录
 * @param ele div(用户好友头像 + name)
 */
var show_chat_tab = function(ele){

    //先获取当前打开的活动窗口，如果有先关闭该窗口
    var active_tab = $("#chat_record_tab_ul li.active")[0];
    if(!isEmpty(active_tab)){
        $(active_tab).removeClass("active");
    }

    // 获取到聊天对象昵称 ul>li>div>span
    var _f_name_span = $(ele).children("span")[0];
    var friend_name = _f_name_span.innerHTML;

    // 获取所有的 ul>li>a 的文本，如果与即将打开的聊天对象的昵称相同则使用该聊天窗口，否则新打开一个聊天窗口



    // 标签页头部信息
    var user_tab_header =
                ' <li class="active">'+
                '     <a href="#'+ friend_name +'" data-toggle="tab">'+
                        friend_name+
                '     </a>'+
                ' </li>';

    var chat_container =
                '<div class="tab-pane fade in active" id="'+ friend_name +'">'+
                    '<p> 聊天记录聊天记录聊天记录聊天记录</p>'+
                '</div>';

    // 设置新的活动标签
    $("#chat_record_tab_ul").append(user_tab_header);

    // 获取当前好友的聊天记录
    $("#chat_recoed_area_container").html("");
    $("#chat_recoed_area_container").append(chat_container)


};
