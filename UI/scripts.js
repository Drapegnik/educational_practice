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
            nameContainer[i].innerHTML = name;
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
        li.classList.add("message");
        li.classList.add("in");
        li.innerHTML = "<a href='#' onclick='deleteMes(\"" + id + "\")'><i class='fa fa-trash'></i></a> " +
            "<a href='#' onclick='editMes(\"id" + id + "\")'><i class='fa fa-pencil'></i></a>";
        var div = document.createElement("div");
        div.classList.add("text");
        div.setAttribute("id", "text" + id);
        div.textContent = message.value;

        var p_sign = document.createElement("p");
        p_sign.textContent = name;
        p_sign.classList.add("sign");
        p_sign.classList.add("myname");

        var p_time = document.createElement("p");
        p_time.textContent = time;
        p_time.classList.add("time");

        //li.appendChild(p_time);
        li.appendChild(div);
        li.appendChild(p_sign);

        document.getElementById("chat").appendChild(li);
        document.getElementById("chat").appendChild(p_time);

        id++;
    }
    message.value = "";
}

function deleteMes(id) {
    var text = document.getElementById("text" + id);
    text.classList.add("delete");
    text.textContent = "message was delete";
}

function enter(e) {
    if (e.keyCode == 13)
        if (e.srcElement.id == "mes")
            send();
        else
            changeName();
}