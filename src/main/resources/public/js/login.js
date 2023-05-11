document.getElementById("register").onclick = function () {
    location.href = "././register.html";
};
document.getElementById("submit").onclick = function () {
    let value = $("#inputUsername").val();
    let pwd = $("#inputPassword1").val();
    let json = {user: value};

    $.post("/login", {userName: value, password: pwd}, function(data) {
        //console.log("here");
        //console.log("register: ", data);
        if (data == null) {
            $("#errormsg").html("Login failed; please check username or password")
        } else {
            sessionStorage.setItem("user", JSON.stringify({user: data.name}));
            sessionStorage.setItem("userId", JSON.stringify({userId: data.userId}));
            sessionStorage.setItem("pass", JSON.stringify({pass: data.pwd}));
            location.href = "././chatroom.html";
        }


    }, "json");
   // sessionStorage.setItem("user", JSON.stringify(json));
   // location.href = "././chatroom.html";
};


