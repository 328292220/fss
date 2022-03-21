function getRootPath() {
    var loc = window.location,
        host = loc.hostname,
        protocol = loc.protocol,
        port = loc.port ? (':' + loc.port) : '';
    var path = location.pathname;

    if (path.indexOf('/') === 0) {
        path = path.substring(1);
    }

    var mypath = '/' + path.split('/')[0];
    path = (mypath != undefined ? mypath : ('/' + loc.pathname.split('/')[1])) + '/';

    var rootPath = protocol + '//' + host + port + path;
    return rootPath;
}

function getRootPath2() {
    var loc = window.location,
        host = loc.hostname,
        protocol = loc.protocol,
        port = loc.port ? (':' + loc.port) : '';
    var path = location.pathname;

    if (path.indexOf('/') === 0) {
        path = path.substring(1);
    }

    var mypath = '/' + path.split('/')[0];
    path = (mypath != undefined ? mypath : ('/' + loc.pathname.split('/')[1])) + '/';

    var rootPath = protocol + '//' + host + port;
    return rootPath;
}

//获取URL参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var Url = window.location.href;

    if (Url.indexOf("?") > 0)
        Url = "&" + Url.substr(Url.indexOf("?") + 1);

    var r = Url.match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]);
    return ''; //返回参数值
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}

function JsonAjax(JsonData, Url, SucessFunctionName) {
    if (Url.indexOf("?") < 0)
        Url = Url + "?t=" + Math.random();
    else
        Url = Url + "&t=" + Math.random();

    $.ajax({
        url: Url,
        type: "POST",
        headers: getheadinfo(),
        data: JsonData,
        dataType: 'json',
        complete: function() {},
        success: SucessFunctionName,
        error: function(XMLHttpRequest, textStatus, errorThrown) {

        }
    });
}

function getheadinfo() {
    return {
        "Content-Type": "application/json;charset=uft-8"
    };
}


