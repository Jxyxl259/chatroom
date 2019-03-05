// jQuery库冲突解决方案
//var $j = jQuery.noConflict();



$(function(){

    $("#show_add_friend_btn").unbind('click').click(
        function(){
            $("#add_friend_window").toggleClass("vanish");
        }
    );

    fetch_friend_list();


    /**
     * 添加好友
     */
    $("#add_friend_btn").unbind('click').click(function(){
        var username = $("#friendName").val();

        if(isEmpty(username) ){
            alert("请输入用户昵称");
            return;
        }
        //var flag = false;
        var form = new FormData(document.getElementById("add_friend_from"));
        $.ajax({
            url:"/user/addFriend",
            type:"post",
            data:form,
            async:false,
            processData:false,
            contentType:false,
            success:function(res){
                if(res.success){
                    console.log("添加好友成功,跳转到首页");
                    $("#add_friend_window").toggleClass("vanish");


                    // 添加成功后重新刷新好友列表
                    fetch_friend_list()
                }else{
                    console.error("注册失败, 错误原因:" + res.message);
                    $("#add_friend_from span").eq(0).html("").append("<label>"+ res.message +"</label>")
                }
            }
        });
        return false;
    });

});

var fetch_friend_list = function(){
    console.log("拉取用户好友列表...");
    $("#_user_friends_list").html("");
    // 好友列表 ul
    var templ_friend_list = function(t){
        var _f_list_content =
           ' <ul style="padding-left: 20px" id="friend_list_ul" type="none">'+
           '{{each(i, _user_friend) friend}}'+
           '     <li id="${_user_friend.id}">'+
           '        <div style="display: inline;cursor:pointer;" onclick="show_chat_tab(this)">' +
            '           {{if _user_friend.online}}'+
            '               <img width="30" height="30" src="./../img/qq_icon.png"/><span>${_user_friend.userName}</span>' +
            '          {{else}}'+
            '               <img width="30" height="30" src="./../img/head_icon_offline_02.png"/><span>${_user_friend.userName}</span>' +
            '           {{/if}}'+
            '       </div>'+
           '     </li>'+
           '{{/each}}'+
           ' </ul>';

        $.template("_user_friends_templ", _f_list_content);

        $.tmpl("_user_friends_templ", t).appendTo("#_user_friends_list");
    };

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

    // 获取到好友列表聊天对象昵称 ul>li>div>span
    var _f_name_span = $(ele).children("span")[0];
    var friend_name = trim( _f_name_span.innerHTML );

    //先获取当前打开的活动tab页 ，如果有先将该窗口置为非活动tab页
    var active_tab = $("#chat_record_tab_ul li.active")[0];
    if(!isEmpty(active_tab)){

        // 获取到当前活动的tab头信息 ul>li>a 的文本，如果与即将打开的聊天对象的昵称不相同 则移除当前tab的活动标签并 新打开一个tab页
        var _active_tab_name = $("#chat_record_tab_ul li.active a")[0];
        var _active_tab_contact_name = _active_tab_name.innerHTML;
        if( trim(_active_tab_contact_name) === friend_name ){
            // 当前tab页为A 点击好友列表中的A，TODO 拉取聊天记录
            return;
        }
        $(active_tab).removeClass("active");
    }

    // 获取到所有tab页头部，判断如果有与 好友列表聊天对象昵称相同的则激活
    var _tab_exist = false;
    $("#chat_record_tab_ul li a").each(function(index) {
        var _a_ele = $(this);
        if (friend_name === trim(this.innerHTML)) {
            _a_ele.parent("li").addClass("active");
            _tab_exist = true;
        }
    });

    if( _tab_exist ){
        // TODO 拉取聊天记录
        return;
    }


    // 新增tab标签
    // 标签页头部信息
    var user_tab_header =
                ' <li class="active">'+
                '     <a href="#'+ friend_name +'" data-toggle="tab" style="padding: 3px 6px;">'+
                        friend_name+
                '     </a>'+
                ' </li>';
    // 聊天记录
    var chat_container =
                '<div class="tab-pane fade in active scroll" id="'+ friend_name +'">'+
                    /*'<p> 聊天记录聊天记录聊天记录聊天记录</p>'+*/
                '</div>';

    // 设置新的活动标签
    $("#chat_record_tab_ul").append(user_tab_header);

    // 获取当前好友的聊天记录
    $("#chat_recoed_area_container").html("");
    $("#chat_recoed_area_container").append(chat_container)


};


