let layer;
layui.use(['layer','form'], function () {
    layer = layui.layer;
});

function IncludeJS() {
    this.includeJS = function (jsList) {
        let arr = [].concat(jsList==null?[]:jsList);
        for (let i = 0; arr.length > i; i++){
            let script1=document.createElement('script');//创建script标签节点
            script1.setAttribute('type','text/javascript');//设置script类型
            script1.setAttribute('src',arr[i]);//设置js地址
            document.body.appendChild(script1);//将js追加为body的子标签
            let flag = false;
            script1.onload=script1.onreadystatechange=function(e){
                console.log(arr[i]+"加载完成！")
            }
        }


    }
}
//全局变量
let IncludeJSObject = new IncludeJS();
let jsArr = null;
const token = sessionStorage.getItem("token");
if(token==null && window.location.href.indexOf("/login") < 0){
    window.location.href = "/login";
}
//加载js
window.onload = function(){
    if(jsArr != null)
        IncludeJSObject.includeJS(jsArr);
}
