
$("#return").click(function(){location.href = "././index.html";})

document.getElementById("submit").onclick = function () {
    let userName = $("#username").val();
    let interests = $("#inputInterest").val();
    let password1 = $("#inputPassword1").val();
    let password2 = $("#inputPassword2").val();
    let age = $("#age").val();
    let school = $("#inputSchool").val();
    let validation = true;

    if (password1 !== password2) {
        $("#errormsg").html("Password don't match");
        validation = false;
        return null
    }
    if (!userName.match(/[a-zA-Z][a-zA-Z0-9]*/)) {
        $("#errormsg").html("Please input a valid username");
        validation = false;
        return null
    }
    if (!school.match(/[a-zA-Z][a-zA-Z0-9]*/)) {
        $("#errormsg").html("Please input a valid university name");
        validation = false;
        return null
    }
    if (interests === "") {
        $("#errormsg").html("Please input a interets");
        validation = false;
        return null
    }
    if (age === "") {
        $("#errormsg").html("Please input your age");
        validation = false;
        return null
    }
    if (validation){
        //POST /register body: userID, interests, password
        $.post("/register", {userName: userName, interests: interests, password: password1, age: age, school: school}, function(data) {
            //console.log("here");
            //console.log("register: ", data);
            if (data != null) {
                sessionStorage.setItem("user", JSON.stringify({user: data.name}));
                sessionStorage.setItem("userId", JSON.stringify({userId: data.userId}));
                sessionStorage.setItem("pass", JSON.stringify({pass: password1}));
                location.href = "././chatroom.html";
            } else {
                $("#errormsg").html("The UserName has been used; please choose another one")
            }
        }, "json");

    }



};