//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function (e) {
    // 保存聊天tab到session
    var _chat_content = $("#chat_record_container").html();

    if( trim(_chat_content) != ""){
        window.sessionStorage["chat_contant"] = _chat_content;
    }

    _web_socket.closeSocket();
};



$(function() {

    // 如果浏览器session缓存中有之前保存的，打开的聊天窗口，直接显示出来
    var _chat_content = window.sessionStorage["chat_contant"];
    if (trim(_chat_content) != "") {
        $("#chat_record_container").html(_chat_content)
    }

    var _login_username = trim($("#login_username").text());
    if ( !isEmpty(_login_username) ) {
        _web_socket.initSocket(_login_username);
    }

    // 点击
    $("#send_message").click(function(){
        var msg = trim(document.getElementById("input_area").value);
        if( msg === "" || msg === null ){
            console.log("发送消息内容为空...");
            return;
        }else{
            var tab_ul = $("#chat_record_tab_ul");
            // 当前没有聊天活动tab页
            if( tab_ul.children("li").length === 0 ){
                console.log("未选择聊天对象...");
                return;
            }else{
                var _active_li = tab_ul.children("li.active")[0];
                var _contact = trim($(_active_li).children("a")[0].innerHTML);
                send_msg( _contact, msg )
            }
        }
    })


});



/**
 * 发送消息
 * @param contact
 * @param msg
 */
var send_msg = function(contact, msg){
    // $.ajax({
    //     url:"/chat/msg/receive",
    //     type:"post",
    //     data:JSON.stringify({'msg':msg,'contactName':contact}),
    //     dataType : 'json',
    //     async:true,
    //     contentType:"application/json",
    //     success:function(res) {
    //         if (res.success) {
    //             console.log("send msg status -> " + res.message);
    //
    //         } else {
    //             console.error("send msg error -> " + res.message);
    //             return
    //         }
    //     }
    // })

    _web_socket.sendChattingMsg(JSON.stringify({'msg':msg,'contactName':contact}));
    var _msg_div =
       ' <div style="display:block;float:left;width:100%">' +
       '     <p style="margin:2px 20px 2px 0px;width: fit-content;max-width: 40%;float:right;border-radius:5px ;border:1px solid #d5d5d5;padding: 2px 5px;">'+ msg +'</p>'+
       ' </div>';
    $("#"+contact).append(_msg_div);
    $("#input_area").val("");
};




/* -------------------------------------- WebSocket --------------------------------------*/
var _web_socket = {

    websocket : null,

    isSupport : function(){
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {
            console.log('当前浏览器支持 websocket');

        } else {
            alert('当前浏览器 Not support websocket')
        }
    },

    initSocket : function(name){

        // 初始化 socket连接
         this.websocket = new WebSocket("ws://10.112.98.226:8080/dispatcher/" + name);
        //this.websocket = new WebSocket("ws://94.191.27.230/:80/dispatcher/" + name);
        // this.websocket = new WebSocket("ws://192.168.0.109:80/dispatcher/" + name);

        // 连接成功建立的回调方法
        this.websocket.onopen = function () {
            setMessageInnerHTML("WebSocket连接成功...");
        };

        // 接收到消息的回调方法
        this.websocket.onmessage = function (event) {
            var _sock_msg = JSON.parse(event.data);
            if( _sock_msg.msgType === "0") {
                // 好友消息
                appendMsg(_sock_msg);
            }else{
                // 系统消息（目前只有下线通知，刷新好友列表）
                systemNotification(_sock_msg);
            }
        };

        // 连接关闭的回调方法
        this.websocket.onclose = function () {
            setMessageInnerHTML("WebSocket连接关闭...");
        };

        // 发生连接错误时候的回调方法
        this.websocket.onerror = function () {
            setMessageInnerHTML("WebSocket连接发生错误...");
        };
    },


    //关闭WebSocket连接
    closeSocket : function closeWebSocket() {
        this.websocket.close();
    },

    //发送消息
    sendChattingMsg : function (msg) {
        this.websocket.send(msg);
    }
};


//将消息显示在网页上
function setMessageInnerHTML(content) {
    document.getElementById('message').innerHTML += content + '<br/>';
}


/**
 * 收到聊天消息后处理函数
 * @param _sock_msg
 */
function appendMsg( _sock_msg ){

    var msg = _sock_msg.msg;
    var name = trim(_sock_msg.contactName);

    // 判断当前是否有打开的tab聊天窗口
    var _chat_tabs = $("#chat_record_tab_ul li>a");

    // 当前没有任何打开的tab页，创建该好友tab页
    if( _chat_tabs.length === 0 ){
        _init_tab( );

    }else{
        var _tab_exist = false;
        var _current_tab_li = $("#chat_record_tab_ul li.active");
        _chat_tabs.each(function(index){
            if( name === trim(this.innerHTML) ){
                // 移除当前 tab div & chat div 里 .active ,
                $(_current_tab_li[0]).removeClass("active");
                var _current_contact = trim($(_current_tab_li[0]).children("a")[0].innerHTML);
                $("#"+_current_contact).removeClass("active in");

                // 设置 tab div & chat div 的 .active
                $(this).parent("li").addClass("active");
                $("#"+name).addClass("active in");

                // 只追加收到的聊天消息即可
                _use_tab( );
                _tab_exist = true;
            }
        });

        if(!_tab_exist){
            // 移除当前 _tab_header 里的 li的 .active ，设置获取收到消息对应 tab的li .active
            $( _current_tab_li[0]).removeClass("active");
            var _current_contact = trim($( _current_tab_li[0]).children("a")[0].innerHTML);
            $("#"+_current_contact).removeClass("active in");
            _init_tab();
        }
    }

    // 创建好友tab页
    function _init_tab(){
        var _chat_tab_head =
            '<li class="active">'+
            '   <a href="#'+ name +'" data-toggle="tab" style="padding: 3px 6px;" > '+ name +' </a>'+
            '</li>';

        var _chat_tab_content =
            ' <div class="tab-pane fade in active scroll" id="' + name + '">'+
            '    <div style="display:block;float:left;width:100%;">'+
            '        <p style="margin:2px 0px 2px 20px;width: fit-content;max-width: 40%;border-radius:5px;border:1px solid #d5d5d5;padding: 2px 5px;"> ' + msg+ '</p>'+
            '    </div>'+
            ' </div>';

        $("#chat_record_tab_ul").append(_chat_tab_head);
        $("#chat_recoed_area_container").append(_chat_tab_content);
    }

    // 聊天tab页已经存在，只接追加聊天消息到对应的聊天窗口
    function _use_tab(){
        var appendMsg =
        '   <div style="display:block;float:left;width:100%;">'+
        '        <p style="margin:2px 0px 2px 20px;width: fit-content;max-width: 40%;border-radius:5px;border:1px solid #d5d5d5;padding: 2px 5px;"> ' + msg+ '</p>'+
        '    </div>';
        $("#"+name).append(appendMsg);
    }

}

/**
 * 系统通知该用户好友，该用户已下线
 * @param _sock_msg
 */
function systemNotification(_sock_msg ){
    user = JSON.parse(_sock_msg.msg);
    var icon = user.online ? "qq_icon" : "head_icon_offline_02" ;
    $("#friend_list_ul #"+ user.id +" div").html('<img width="30" height="30" src="./../img/'+ icon +'.png"/><span>'+ user.userName +'</span>');

}