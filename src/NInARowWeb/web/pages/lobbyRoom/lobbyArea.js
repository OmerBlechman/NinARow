var USER = buildUrlWithContextPath("users");
var LOBBY_CONTENT_URL = buildUrlWithContextPath("lobbyArea");
var ENTER_GAME_URL = buildUrlWithContextPath("EnterGame");
var CLEAR_UPLOAD_ERROR_URL = buildUrlWithContextPath("ClearUploadError");
var CLEAR_UPLOAD_FILE_URL = buildUrlWithContextPath("ClearUploadFile");
var VIEWER_ENTER_URL = buildUrlWithContextPath("ViewerEnter");


$(function () {
    $("#lobby-form").submit(function () {
        var file = this[0].files[0];
        var formData = new FormData();
        formData.append("uploadFile", file);

        $.ajax({
            method:'POST',
            data: formData,
            url: LOBBY_CONTENT_URL,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            success: function() {}
        });
        return false;
    });

    $.ajax({
        url:USER,
        dataType: 'json',
        success:function(data){
            $("#lobby-player-name").append("Hello " + data.PlayerName);
        }
    });
    setInterval(ajaxGetContent,1000);
});

function ajaxGetContent(){
    $.ajax({
        url: LOBBY_CONTENT_URL,
        dataType: 'json',
        success:function(data){
            var boards = $("#lobby-games");
            var title =$("tr")[0].cloneNode(true);
            $("#lobby-games").empty();
            $("#lobby-games").append(title);
            $.each(data.boards || [], createBoard);
            $("#lobby-userslist").empty();
            $.each(data.users || [], createUser);
            if(data.errorMessage) {
                $("#error-message").empty();
                $("#error-message").append(data.errorMessage);
                $.ajax({
                    method: "POST",
                    url: CLEAR_UPLOAD_ERROR_URL,
                    dataType: 'json',
                    success: function () {
                    }
                });
                setTimeout(clearMessage, 5000);
            }
            if(data.submitStatus)
                clearFileChooser();
        }
    })
}

function clearFileChooser(){
    $("#lobby-file-choicer")[0].value = "";
    $.ajax({
        method: "POST",
        url: CLEAR_UPLOAD_FILE_URL,
        dataType: 'json',
        success:function(){}
    })
}

function clearMessage(){
    $("#error-message").empty();
}

function createUser(index,dataJson) {
    $('<li>' + dataJson.m_Name + '</li>').appendTo($("#lobby-userslist"));
}

function enterViewer(gameForm){
    var currentGame = $('#lobby-games')[0].children[0].children[gameForm.getAttribute('index')].children[1].innerHTML;
    $.ajax({
        method: "POST",
        url: VIEWER_ENTER_URL,
        data: {gamename: currentGame},
        success: function(response){
                window.location = "/NinARow/pages/game/GameRoom.html";
        }
    })
}


function enterGame(gameForm){
    var currentGame = $('#lobby-games')[0].children[0].children[gameForm.getAttribute('index')].children[1].innerHTML;
    $.ajax({
        method: "POST",
        url: ENTER_GAME_URL,
        data: {gamename: currentGame},
        success: function(response){
            if(response)
            {
                $("#error-message").empty();
                $("#error-message").append(response);
                setTimeout(clearMessage, 5000);
            }
            else
                window.location = "/NinARow/pages/game/GameRoom.html";
        }
    })
}

function createBoard(index,dataJson){
    if(dataJson !== []){
        var gameForm = $("<td id='gameForm' onclick='enterGame(this)'>");
        var viewForm = $("<td id='gameForm' onclick='enterViewer(this)'>");
        gameForm[0].setAttribute("index",index + 1);
        viewForm[0].setAttribute("index",index + 1);
        var newBoard = $("<tr class = 'lobby-boards-game'>");
        newBoard.append($("<td class='lobby-col-title'>").append(index + 1));
        newBoard.append($("<td class='lobby-col-title'>").append(dataJson.GameName));
        newBoard.append($("<td class='lobby-col-title'>").append(dataJson.CreatedUserName));
        newBoard.append($("<td class='lobby-col-title'>").append(dataJson.RegisteredPlayersToResponse));
        newBoard.append($("<td class='lobby-col-title'>").append(dataJson.ViewersAmount));
        newBoard.append($("<td class='lobby-col-title'>").append(dataJson.BoardSize));
        newBoard.append($("<td class='lobby-col-title'>").append(dataJson.Target));
        newBoard.append($("<td class='lobby-col-title'>").append(dataJson.ActiveGame.toString()));
        var enterGameButton;
        var viewGameButton;
        if(dataJson.ActiveGame.toString() === "Yes")
            enterGameButton = $("<input id ='enter-game-button' value='Join' disabled type='submit'>");
        else
            enterGameButton = $("<input id ='enter-game-button' value='Join' type='submit'>");
        viewGameButton = $("<input id ='view-game-button' value='View' type='submit'>");
        viewForm.append(viewGameButton);
        gameForm.append(enterGameButton);
        newBoard.append(gameForm,viewForm);
        $("#lobby-games").append(newBoard);
    }
}

