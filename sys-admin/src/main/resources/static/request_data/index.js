
function post(Obj,keyName,url,param){
    console.log(keyName+",url="+url);
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
                console.log(Obj[keyName]);
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
        dataType: "json",
        contentType:'application/json'
    });
}
//获取用户
const url_currentUser = "/account/user/currentUser";
//获取资源菜单
const url_menus = "/account/menu/getMenu";
//获取目录和文件
const url_business_query = "/business/common/query";
new Vue({
    el: '#indexPage',
    data :{
        currentUser: null,
        menu: null,
        dir_file: null,
    },
    methods:{
        getAllFiles:function(){
            let param = {
                "data":{"parentId":null, "orderByType":"DESC"}
            };
            post(this,"dir_file",url_business_query,JSON.stringify(param));

            let arr = this.dir_file.fileList;
            for(let i=0;i<arr.length;i++) {
                let p = "<p><a>" + arr[i].fileName + "</a></p>"
                $("#files").append(p);
            }

        },
    },
    mounted:function(){
        // this.getCurrentUser();
        // post(this,"currentUser",url_currentUser,null);
        // post(this,"menu",url_menus,null);
        this.getAllFiles();
    },

})

$(function(){
    //退出登录
    $("#loginOut").on("click",function(){
        sessionStorage.setItem("token",null);
        window.location.href = "/";
    })
});