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

    _web_socket.sendChattingMsg(JSON.stringify({'msg':msg,'contactName':contact}))
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

        // 连接成功建立的回调方法
        this.websocket.onopen = function () {
            setMessageInnerHTML("WebSocket连接成功");
        };

        // 接收到消息的回调方法
        this.websocket.onmessage = function (event) {
            setMessageInnerHTML(event.data);
        };

        // 连接关闭的回调方法
        this.websocket.onclose = function () {
            setMessageInnerHTML("WebSocket连接关闭");
        };

        // 发生连接错误时候的回调方法
        this.websocket.onerror = function () {
            setMessageInnerHTML("WebSocket连接发生错误");
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