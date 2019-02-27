//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    _web_socket.closeSocket();
};

$(function() {

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
         this.websocket = new WebSocket("ws://10.112.98.226:80/dispatcher/" + name);
        // this.websocket = new WebSocket("ws://192.168.0.109:80/dispatcher/" + name);

        // 连接成功建立的回调方法
        this.websocket.onopen = function () {
            setMessageInnerHTML("WebSocket连接成功...");
        };

        // 接收到消息的回调方法
        this.websocket.onmessage = function (event) {

            // TODO 根据event.data中的消息发送人，来判断当前tab是否属于该发送人，如果是，直接追加消息，如果不是，新建窗口并追加消息
            // TODO 现假定当前tab 是该发送人
            var _sock_msg = JSON.parse(event.data);
            var _target_contact_tab_name = _sock_msg.contactName;
            var _recive_msg =
              '  <div style="display:block;float:left;width:100%;">'+
              '      <p style="margin:2px 0px 2px 20px;width: fit-content;max-width: 40%;border-radius:5px;border:1px solid #d5d5d5;padding: 2px 5px;">' + _sock_msg.msg + '</p>'+
              '  </div>';
            $("#"+_target_contact_tab_name).append(_recive_msg);
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