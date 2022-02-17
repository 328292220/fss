layui.use(['layer','form'], function () {
    layer = layui.layer;
    let form = layui.form;//select、单选、复选等依赖form

    /**
     * 监听键盘事件
     */
    document.addEventListener("keydown",function (even) {
        //按下Enter回车键
        if(even.keyCode === 13){
            formSubmit();
        }
    })
});

function encrypt(word) {
    var publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9UcbGHLrLGbX1HA1BP8jZzMkXdC2yNLmmwfKq2IJnJ5oG9gKKApkIvzDr9EBVNWh4UKYW+uXoIyRUg3iKjS/d+lu16AJ/Yz9z5TJM3KM6AH5/kMXj1XycazZk8PsgikNsEyIOp0q5DvDgOq0vDqtmh5IXiRWc1B6GWvLxj+BnAwIDAQAB";
    const encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    let password = encrypt.encrypt(word);// rac加密后的字符串
    return password
}

/**
 * 登录
 */
function formSubmit() {
    let rememberMe = true;
    if($("input[name='rememberMe']:checked").length <= 0){
        rememberMe = false;
    }
    var password = encrypt($("#password").val());
    var params = {
        "username": $("#username").val(),
        "password": password,
        "grant_type": "my_password",
        "client_id": "fss_web",
        "client_secret": "fss_secret",
    }
    $.ajax({
        type: 'POST',
        url: ctx + "/auth/oauth/token",
        data: params,
        success: function (data) {
            if (data.code == 200) {
                console.log(data.data.token);
                layer.msg(data.msg, {icon: 1,time: 1000}, function () {
                    sessionStorage.setItem("token","Bearer "+data.data.token);
                    window.location.href = "index";
                });
            } else {
                layer.msg(data.msg, {icon: 2,time: 2000}, function () {});
            }
        },
        error: function (data) {
            layer.msg(data.msg, {icon: 2,time: 2000}, function () {});
        },
        dataType: "json"
    });
}

/**
 * 刷新验证码
 */
function changeCode() {
    let img = document.getElementById("captchaImg");
    img.src = ctx + "/getVerifyCodeImage?time=" + new Date().getTime();
}
$(function(){
    $("#formSubmit").on("click",function(){
        formSubmit();
    })
});