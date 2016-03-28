/**
 * Created by Drapegnik on 03.03.16.
 */

var name = "User";
var id = 0;

var messageList = [];

function run() {
    messageList = loadMessages() || [
            newMes('Hi')
        ];
    id = messageList.length;
    name = loadUsername() || "User";
    document.getElementById("name").value = name;
    render(messageList, true);
}

function changeName() {
    var inputName = document.getElementById("name");

    function swapUsers(mes, time, classRemove, classAdd, isHidden) {
        mes.classList.remove(classRemove);
        mes.classList.add(classAdd);
        if (!mes.getElementsByClassName("text")[0].classList.contains("delete")) {
            mes.childNodes[0].hidden = isHidden;
            mes.childNodes[2].hidden = isHidden;
        }
        time.classList.remove(classRemove);
        time.classList.add(classAdd);

    }

    if (inputName.value.trim().length == 0) {
        inputName.setAttribute("placeholder", "name can't be blank");
        inputName.classList.add("holdcol");
    }
    else if (name != inputName.value.trim()) {
        name = inputName.value.trim();
        saveUsername(name);

        var messages = document.getElementsByTagName("li");
        var times = document.getElementsByClassName("time");

        for (var i = 0; i < messages.length; i++) {
            if (messages[i].getElementsByClassName("myname")[0].textContent != name)
                swapUsers(messages[i], times[i], "in", "out", true);
            else
                swapUsers(messages[i], times[i], "out", "in", false);
        }
    }
}

function send() {
    var message = document.getElementById("mes");

    if (message.value.trim().length == 0) {
        message.setAttribute("placeholder", "message can't be blank");
        message.classList.add("holdcol");
    }
    else {
        messageList.push(newMes(message.value));
        render(messageList, true);
    }
    message.value = "";
}

function deleteMes(id) {
    messageList[id - 1].isDelete = true;
    messageList[id - 1].time = new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
    render(messageList, false);

}

function editMes(id) {
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
        messageList[inputId.substr(5) - 1].text = input.value;
        messageList[inputId.substr(5) - 1].time = new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
        messageList[inputId.substr(5) - 1].isEdit = true;
        input.hidden = true;
        input.nextElementSibling.firstElementChild.hidden = true;

        render(messageList, false);
    }
}

function cancelMes(inputId) {
    var input = document.getElementById(inputId);
    input.value = messageList[inputId.substr(5) - 1].text;

    input.hidden = true;
    input.nextElementSibling.firstElementChild.hidden = true;

    render(messageList, false);
}

function enter(e) {
    if (e.keyCode == 13)
        if (e.srcElement.id == "mes")
            send();
        else if (e.srcElement.id == "name")
            changeName();
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
    for (var i = 0; i < messages.length; i++)
        renderMes(messages[i]);
    if (isRelocate)
        document.location.href = "#id" + id;
    saveMessages(messages);
}

function renderMes(mes) {
    var li = document.createElement("li");
    li.setAttribute("id", "id" + mes.id);
    li.innerHTML = "<a href='#' onclick='deleteMes(\"" + mes.id + "\")'><i class='fa fa-trash'></i></a> " +
        "<a href='#' onclick='editMes(\"" + mes.id + "\")'><i class='fa fa-pencil'></i></a>";

    var div = document.createElement("div");
    div.classList.add("text");
    div.setAttribute("id", "text" + mes.id);
    div.textContent = mes.text;

    var input = document.createElement("input");
    input.setAttribute("type", "text");
    input.setAttribute("id", "input" + mes.id);
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
    p_sign.textContent = mes.user;
    p_sign.classList.add("sign", "myname");

    var p_time = document.createElement("p");
    p_time.textContent = mes.time;
    p_time.setAttribute("id", "time" + mes.id);


    if (mes.user == name) {
        li.classList.add("message", "in");
        p_time.classList.add("time", "in");
    }
    else {
        li.classList.add("message", "out");
        p_time.classList.add("time", "out");
    }

    var cancel = document.createElement("span");
    cancel.innerHTML = "<a href='#' style='color: #d9534f;' onclick='cancelMes(\"" + mes.id + "\")'><i class='fa fa-times'></i></a>";
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
    if (mes.isDelete || mes.user != name) {
        if (mes.isDelete) {
            var text = li.getElementsByClassName("text")[0];
            text.classList.add("delete");
            text.textContent = "message was delete";
            p_time.textContent = "delete on " + mes.time;
        }

        li.childNodes[0].hidden = true;
        li.childNodes[2].hidden = true;
    } else if (mes.isEdit)
        p_time.textContent = "edit on " + mes.time;
}


function newMes(text) {
    id++;
    return {
        user: name,
        id: id,
        text: text,
        time: new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1"),
        isDelete: false,
        isEdit: false
    };
}

function saveMessages(listToSave) {
    if (!isLocStorOk())
        return;

    localStorage.setItem("Chat History", JSON.stringify(listToSave));
}

function saveUsername(name) {
    if (!isLocStorOk())
        return;

    console.log(name);
    localStorage.setItem("Username", name);
}

function loadMessages() {
    if (!isLocStorOk())
        return;

    var item = localStorage.getItem("Chat History");

    return item && JSON.parse(item);
}

function loadUsername() {
    if (!isLocStorOk())
        return;

    var item = localStorage.getItem("Username");
    console.log(item);
    return item;
}

function isLocStorOk() {
    if (typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return false;
    }
    else
        return true;
}