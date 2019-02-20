$(function(){

    /**
     * 显示/隐藏 登录div
     */
    $("#show_login_form_btn").click(
        function(){
            $("#login_form_container").toggleClass("vanish");
        }
    );


    $("#show_signup_form_btn").click(
        function(){
            $("#signup_form_container").toggleClass("vanish");
        }
    );


    /**
     * 用户登陆
     */
    $("#login_btn").click(function(){
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
                    window.location.href=window.location.href.substr(0,window.location.href.lastIndexOf("/"));

                }else{
                    console.error("登陆失败, 错误原因:" + res.resultMsg);
                }
            }
        });
       return false;
    });

    /**
     * 用户注册
     */
    $("#signup_btn").click(function(){
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
                    console.log("登陆成功,跳转到首页");
                    window.location.href=window.location.href.substr(0,window.location.href.lastIndexOf("/"));

                }else{
                    console.error("登陆失败, 错误原因:" + res.resultMsg);
                }
            }
        });
        return false;
    });
});





function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

