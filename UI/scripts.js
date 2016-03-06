/**
 * Created by Drapegnik on 03.03.16.
 */

var name = "User";
var id = 0;

function changeName() {
    var inputName = document.getElementById("name");

    if (inputName.value.trim().length == 0) {
        inputName.setAttribute("placeholder", "name can't be blank");
        inputName.classList.add("holdcol");
    }
    else {
        name = inputName.value;

        var nameContainer = document.getElementsByClassName("myname");

        for (var i = 0; i < nameContainer.length; i++)
            nameContainer[i].textContent = name;
    }
}

function send() {
    var message = document.getElementById("mes");

    if (message.value.trim().length == 0) {
        message.setAttribute("placeholder", "message can't be blank");
        message.classList.add("holdcol");
    }
    else {
        var time = new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
        var li = document.createElement("li");
        li.setAttribute("id", "id" + id);
        li.classList.add("message", "in");
        li.innerHTML = "<a href='#' onclick='deleteMes(\"" + id + "\")'><i class='fa fa-trash'></i></a> " +
            "<a href='#' onclick='editMes(\"" + id + "\")'><i class='fa fa-pencil'></i></a>";

        var div = document.createElement("div");
        div.classList.add("text");
        div.setAttribute("id", "text" + id);
        div.textContent = message.value;

        var input = document.createElement("input");
        input.setAttribute("type", "text");
        input.setAttribute("id", "input" + id);
        input.classList.add("editInput");
        input.hidden = true;
        input.onkeypress = function () {
            enter(event)
        };


        var p_sign = document.createElement("p");
        p_sign.textContent = name;
        p_sign.classList.add("sign", "myname");

        var p_time = document.createElement("p");
        p_time.textContent = time;
        p_time.setAttribute("id", "time" + id);
        p_time.classList.add("time");

        li.appendChild(input);
        li.appendChild(div);
        li.appendChild(p_sign);

        document.getElementById("chat").appendChild(li);
        document.getElementById("chat").appendChild(p_time);
        document.location.href = "#id" + id;
        id++;
    }
    message.value = "";
}

function deleteMes(id) {
    var text = document.getElementById("text" + id);
    text.classList.add("delete");
    text.textContent = "message was delete";

    var childs = document.getElementById("id" + id).childNodes;
    childs[0].hidden = true;
    childs[2].hidden = true;

    var time = document.getElementById("time" + id);
    time.textContent = "delete on " + new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
}

function editMes(id) {
    var text = document.getElementById("text" + id);
    text.hidden = true;

    var input = document.getElementById("input" + id);
    input.setAttribute("value", text.textContent);
    input.hidden = false;
    input.focus();

}

function saveMes(inputId) {
    var input = document.getElementById(inputId);

    if (input.value.trim().length == 0) {
        input.setAttribute("placeholder", "message can't be blank");
        input.classList.add("holdcol");
    }
    else {
        var text = document.getElementById("text" + inputId.substr(5));
        var time = document.getElementById("time" + inputId.substr(5));

        text.textContent = input.value;
        input.hidden = true;
        text.hidden = false;

        time.textContent = "edit on " + new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
    }
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