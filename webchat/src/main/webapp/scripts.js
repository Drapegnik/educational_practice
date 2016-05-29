/**
 * Created by Drapegnik on 03.03.16.
 */

var App = {
    name: "User",
    hostUrl: 'http://localhost:8080',
    mainUrl: 'http://localhost:8080/chat',
    id: 0,
    messageList: [],
    token: 'TN11EN',
    isPolling: true
};

function run() {
    App.name = document.getElementById("name").value;
    console.log(App.name);
    if (App.name == "") {
        loadUsername();
    }
    saveUsername(App.name);
    document.getElementById("name").value = App.name;

    loadMessages();
}

function send() {
    var message = document.getElementById("mes");

    if (message.value.trim().length == 0) {
        message.setAttribute("placeholder", "message can't be blank");
        message.classList.add("holdcol");
    }
    else {
        var mes = newMes(message.value);

        ajax('POST', App.mainUrl, JSON.stringify(mes), function () {
            loadMessages();
        });
    }
    message.value = "";
}

function deleteMes(id) {
    var mesToDelete = {id: App.messageList[id].id};

    ajax('DELETE', App.mainUrl, JSON.stringify(mesToDelete), function () {
        render(App.messageList, false);
    });

}

function editMes(id) {
    App.isPolling = false;
    var text = document.getElementById("text" + id);
    text.hidden = true;

    var input = document.getElementById("input" + id);
    input.setAttribute("value", text.textContent);
    input.hidden = false;
    input.focus();
    document.getElementById("id" + id).getElementsByTagName("a")[2].hidden = false;

}

function saveMes(inputId) {
    var input = document.getElementById(inputId);

    if (input.value.trim().length == 0) {
        input.setAttribute("placeholder", "message can't be blank");
        input.classList.add("holdcol");
    }
    else {
        var mesToPut = {
            id: App.messageList[inputId.substr(5)].id,
            text: input.value
        };

        ajax('PUT', App.mainUrl, JSON.stringify(mesToPut), function () {
            render(App.messageList, false);
        });

        input.hidden = true;
        input.nextElementSibling.firstElementChild.hidden = true;
        App.isPolling = true;
    }
}

function cancelMes(inputId) {
    var input = document.getElementById(inputId);
    input.value = App.messageList[inputId.substr(5)].text;

    input.hidden = true;
    input.nextElementSibling.firstElementChild.hidden = true;

    render(App.messageList, false);
    App.isPolling = true;
}

function enter(e) {
    if (e.keyCode == 13)
        if (e.srcElement.id == "mes")
            send();
        else
            saveMes(e.srcElement.id);
}

document.onkeydown = function (e) {
    if (e.srcElement.name == "edit")
        if (e.keyCode == 27)
            cancelMes(e.srcElement.id);
};

function render(messages, isRelocate) {
    document.getElementById("chat").innerHTML = "";
    for (var i = 0; i < messages.length; i++) {
        messages[i].locId = i;
        messages[i].time = new Date(messages[i].timestamp).toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
        renderMes(messages[i]);
    }
    if (isRelocate)
        document.location.href = "#id" + App.id;
}

function renderMes(mes) {
    var li = document.createElement("li");
    li.setAttribute("id", "id" + mes.locId);
    li.innerHTML = "<a href='#' onclick='deleteMes(\"" + mes.locId + "\")'><i class='fa fa-trash'></i></a> " +
        "<a href='#' onclick='editMes(\"" + mes.locId + "\")'><i class='fa fa-pencil'></i></a>";

    var div = document.createElement("div");
    div.classList.add("text");
    div.setAttribute("id", "text" + mes.locId);
    div.textContent = mes.text;

    var input = document.createElement("input");
    input.setAttribute("type", "text");
    input.setAttribute("id", "input" + mes.locId);
    input.setAttribute("name", "edit");
    input.classList.add("editInput");
    input.hidden = true;
    input.onkeypress = function () {
        enter(event)
    };
    input.addEventListener("focusout", function () {
        cancelMes(input.id);
    });

    var p_sign = document.createElement("p");
    p_sign.textContent = mes.author;
    p_sign.classList.add("sign", "myname");

    var p_time = document.createElement("p");
    p_time.textContent = mes.time;
    p_time.setAttribute("id", "time" + mes.locId);


    if (mes.author == App.name) {
        li.classList.add("message", "in");
        p_time.classList.add("time", "in");
    }
    else {
        li.classList.add("message", "out");
        p_time.classList.add("time", "out");
    }

    var cancel = document.createElement("span");
    cancel.innerHTML = "<a href='#' style='color: #d9534f;' onclick='cancelMes(\"" + mes.locId + "\")'><i class='fa fa-times'></i></a>";
    cancel.firstElementChild.hidden = true;

    li.appendChild(input);
    li.appendChild(cancel);
    li.appendChild(div);
    li.appendChild(p_sign);

    renderMesState(li, p_time, mes);

    document.getElementById("chat").appendChild(li);
    document.getElementById("chat").appendChild(p_time);
}


function renderMesState(li, p_time, mes) {
    if (mes.status == "delete" || mes.author != App.name) {
        if (mes.status == "delete") {
            var text = li.getElementsByClassName("text")[0];
            text.classList.add("delete");
            text.textContent = "message was delete";
            p_time.textContent = "delete on " + mes.time;
        }

        li.childNodes[0].hidden = true;
        li.childNodes[2].hidden = true;
    } else if (mes.status == "edit")
        p_time.textContent = "edit on " + mes.time;
}


function newMes(text) {
    App.id++;
    return {
        author: App.name,
        locId: App.id,
        text: text,
        timestamp: new Date().getTime(),
        status: "default"
    };
}

function loadMessages() {
    console.log("load");
    var url = App.mainUrl + '?token=' + App.token;

    ajax('GET', url, null, function (responseText) {
        var response = JSON.parse(responseText);
        for (var i = 0; i < response.messages.length; i++) {
            if (response.messages[i].status == "default")
                App.messageList.push(response.messages[i]);
            else {
                for (var j = 0; j < App.messageList.length; j++)
                    if (App.messageList[j].id == response.messages[i].id)
                        App.messageList[j] = response.messages[i]
            }
        }
        if (App.token != response.token) {
            App.token = response.token;
            App.id = App.messageList.length - 1;
            render(App.messageList, true);
        }
    });
}

setInterval(function () {
    if (App.isPolling)
        loadMessages();
}, 500);

function serverStatus(isOk) {
    document.getElementById("ok").hidden = !isOk;
    document.getElementById("err").hidden = isOk;
}

function defaultErrorHandler(message) {
    serverStatus(false);
    console.error(message);
}

function isError(text) {
    if (text == "")
        return false;

    try {
        var obj = JSON.parse(text);
    } catch (ex) {
        return true;
    }

    return !!obj.error;
}

function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandler;
    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {
        if (xhr.readyState !== 4)
            return;

        if (xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }

        if (isError(xhr.responseText)) {
            continueWithError('Error on the server side, response ' + xhr.responseText);
            return;
        }

        continueWith(xhr.responseText);
        serverStatus(true);
    };

    xhr.ontimeout = function () {
        continueWithError('Server timed out !');
    };

    xhr.onerror = function (e) {
        var errMsg = 'Server connection error !\n' +
            '\n' +
            'Check if \n' +
            '- server is active\n' +
            '- server sends header "Access-Control-Allow-Origin:*"\n' +
            '- server sends header "Access-Control-Allow-Methods: PUT, DELETE, POST, GET, OPTIONS"\n';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

function saveUsername(name) {
    if (!isLocStorOk())
        return;

    localStorage.setItem("Username", name);
}

function loadUsername() {
    console.log("lodUser");
    if (!isLocStorOk())
        return;
    console.log(localStorage.getItem("Username"));
    App.name = localStorage.getItem("Username");
    console.log(App.name);
    document.getElementById("locname").innerText = App.name;
}

function isLocStorOk() {
    if (typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return false;
    }
    else
        return true;
}

function setCookie(name, value, options) {
    options = options || {};

    var expires = options.expires;

    if (typeof expires == "number" && expires) {
        var d = new Date();
        d.setTime(d.getTime() + expires * 1000);
        expires = options.expires = d;
    }
    if (expires && expires.toUTCString) {
        options.expires = expires.toUTCString();
    }

    value = encodeURIComponent(value);

    var updatedCookie = name + "=" + value;

    for (var propName in options) {
        updatedCookie += "; " + propName;
        var propValue = options[propName];
        if (propValue !== true) {
            updatedCookie += "=" + propValue;
        }
    }

    document.cookie = updatedCookie;
}

function deleteCookie(name) {
    setCookie(name, "", {
        expires: -1
    })
}

function logOut() {
    deleteCookie('uid');
    window.location = App.hostUrl + '/login.jsp';
}
