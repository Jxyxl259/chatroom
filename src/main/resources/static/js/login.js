//jquery库冲突解决办法， 重新定义一个变量取代 $
//var $j = jQuery.noConflict();

$(function(){

    var options= {
        button: "#show_emoji_box_btn",
        showTab: true,
        animation: 'fade',
        icons: [{
            name: "贴吧表情",
            path: "./../lib/emoj/dist/img/tieba/",
            maxNum: 50,
            file: ".jpg",
            placeholder: ":{alias}:",
            alias: {
                1: "hehe",
                2: "haha",
                3: "tushe",
                4: "a",
                5: "ku",
                6: "lu",
                7: "kaixin",
                8: "han",
                9: "lei",
                10: "heixian",
                11: "bishi",
                12: "bugaoxing",
                13: "zhenbang",
                14: "qian",
                15: "yiwen",
                16: "yinxian",
                17: "tu",
                18: "yi",
                19: "weiqu",
                20: "huaxin",
                21: "hu",
                22: "xiaonian",
                23: "neng",
                24: "taikaixin",
                25: "huaji",
                26: "mianqiang",
                27: "kuanghan",
                28: "guai",
                29: "shuijiao",
                30: "jinku",
                31: "shengqi",
                32: "jinya",
                33: "pen",
                34: "aixin",
                35: "xinsui",
                36: "meigui",
                37: "liwu",
                38: "caihong",
                39: "xxyl",
                40: "taiyang",
                41: "qianbi",
                42: "dnegpao",
                43: "chabei",
                44: "dangao",
                45: "yinyue",
                46: "haha2",
                47: "shenli",
                48: "damuzhi",
                49: "ruo",
                50: "OK"
            },
            title: {
                1: "呵呵",
                2: "哈哈",
                3: "吐舌",
                4: "啊",
                5: "酷",
                6: "怒",
                7: "开心",
                8: "汗",
                9: "泪",
                10: "黑线",
                11: "鄙视",
                12: "不高兴",
                13: "真棒",
                14: "钱",
                15: "疑问",
                16: "阴脸",
                17: "吐",
                18: "咦",
                19: "委屈",
                20: "花心",
                21: "呼~",
                22: "笑脸",
                23: "冷",
                24: "太开心",
                25: "滑稽",
                26: "勉强",
                27: "狂汗",
                28: "乖",
                29: "睡觉",
                30: "惊哭",
                31: "生气",
                32: "惊讶",
                33: "喷",
                34: "爱心",
                35: "心碎",
                36: "玫瑰",
                37: "礼物",
                38: "彩虹",
                39: "星星月亮",
                40: "太阳",
                41: "钱币",
                42: "灯泡",
                43: "茶杯",
                44: "蛋糕",
                45: "音乐",
                46: "haha",
                47: "胜利",
                48: "大拇指",
                49: "弱",
                50: "OK"
            }
        }, {
            path: "./../lib/emoj/dist/img/qq/",
            maxNum: 91,
            excludeNums: [41, 45, 54],
            file: ".gif",
            placeholder: "#qq_{alias}#"
        }]
    };
    $("#input_area").emoji(options);


    /**
     * 显示/隐藏 登录div
     * click函数可能存在点击一次 实际执行两遍匿名函数的怪异事件，添加unbind函数
     */
    $("#show_login_form_btn").unbind('click').click(
        function(){
            $("#login_form_container").toggleClass("vanish");
        }
    );


    $("#show_signup_form_btn").unbind('click').click(
        function(){
            $("#signup_form_container").toggleClass("vanish");
        }
    );


    /**
     * 用户登陆
     */
    $("#login_btn").unbind('click').click(function(){
        var username = $("#username").val();
        var password = $("#password").val();
        if(isEmpty(username) || isEmpty(password) ){
            alert("请输入用户名 及密码");
            return;
        }
        //var flag = false;
        var form = new FormData(document.getElementById("login_form"));
        $.ajax({
            url:"/doLogin",
            type:"post",
            data:form,
            async:false,
            processData:false,
            contentType:false,
            success:function(res){
                if(res.success){
                    console.log("登陆成功,跳转到首页");
                    // 该请求是post请求?
                    window.refresh(true);//location.href=window.location.href.substr(0,window.location.href.lastIndexOf("/"));
                }else{
                    console.error("登陆失败, 错误原因:" + res.message);
                    alert("登录失败：" + res.message)
                    return false;
                }
            }
        });
    });

    /**
     * 用户注册
     */
    $("#signup_btn").unbind('click').click(function(){
        var username = $("#signup_username").val();
        var password = $("#signup_password").val();
        if(isEmpty(username) || isEmpty(password) ){
            alert("请输入注册 用户名 及密码");
            return;
        }
        //var flag = false;
        var form = new FormData(document.getElementById("signup_form"));
        $.ajax({
            url:"/doSignup",
            type:"post",
            data:form,
            async:false,
            processData:false,
            contentType:false,
            success:function(res){
                if(res.success){
                    console.log("注册成功成功,跳转到首页");
                    window.location.href=window.location.href.substr(0,window.location.href.lastIndexOf("/"));

                }else{
                    console.error("注册失败, 错误原因:" + res.message);
                }
            }
        });
        return false;
    });
});


// 删除字符串两边的空格
var trim = function (str){
    console.log("str ->" + typeof (str));
    return str.replace(/^\s+|\s+$/g,"");
};



function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

