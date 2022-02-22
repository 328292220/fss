
function post(Obj,keyName,url,param){
    $.ajax({
        type: 'POST',
        url: ctx + url,
        data: param,
        beforeSend: function(request) {
            console.log("token:"+token);
            request.setRequestHeader("Authorization",token);
        },
        success: function (data) {
            console.log(data);
            if (data.code == 200) {
                Obj[keyName] = data.data;
            }else if(data.code == 11003){
                layer.msg(data.msg, {icon: 2,time: 2000}, function () {
                    window.location.href = "/";
                });
            } else {
                layer.msg(data.msg, {icon: 2,time: 5000}, function () {});
            }
        },
        error: function (data) {
            console.log("error:"+JSON.stringify(data));
            JSON.stringify(data)
            layer.msg(data.msg, {icon: 2,time: 10000}, function () {});
        },
        dataType: "json"
    });
}
//获取用户
const url_currentUser = "/account/user/currentUser";
//获取资源菜单
const url_menus = "/account/menu/getMenu";
new Vue({
    el: '#indexPage',
    data :{
        currentUser: null,
        menus: null,
    },
    methods:{
        // getCurrentUser:function(){
        //     getCurrentUser(this);
        // },
    },
    mounted:function(){
        // this.getCurrentUser();
        post(this,"currentUser",url_currentUser,null);
        post(this,"menus",url_menus,null);
    },

})

$(function(){
    //退出登录
    $("#loginOut").on("click",function(){
        sessionStorage.setItem("token",null);
        window.location.href = "/";
    })
});