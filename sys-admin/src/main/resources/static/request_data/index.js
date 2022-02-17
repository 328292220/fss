
function getCurrentUser(Vue){
    $.ajax({
        type: 'POST',
        url: ctx + "/account/user/currentUser",
        data: null,
        beforeSend: function(request) {
            request.setRequestHeader("Authorization",token);
        },
        success: function (data) {
            console.log(data);
            if (data.code == 200) {
                Vue["currentUser"] = data.data;
            } else {
                layer.msg(data.msg, {icon: 2,time: 5000}, function () {});
            }
        },
        error: function (data) {
            layer.msg(data.msg, {icon: 2,time: 10000}, function () {});
        },
        dataType: "json"
    });
}


new Vue({
    el: '#indexPage',
    data :{
        currentUser: null
    },
    methods:{
        getCurrentUser:function(){
            getCurrentUser(this);
        }
    },
    mounted:function(){
        this.getCurrentUser();
    },

})

$(function(){
    //退出登录
    $("#loginOut").on("click",function(){
        sessionStorage.setItem("token",null);
        window.location.href = "/";
    })
});