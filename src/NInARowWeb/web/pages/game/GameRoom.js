var USER = buildUrlWithContextPath("users");
var BOARD_GAME_URL = buildUrlWithContextPath("BoardGame");
var STATISTICS_URL = buildUrlWithContextPath("Statistics");
var HISTORY_URL = buildUrlWithContextPath("History");
var PLAYERS_DETAILS_URL = buildUrlWithContextPath("PlayersDetails");
var START_GAME_URL = buildUrlWithContextPath("StartGame");
var PLAYER_MOVE_URL = buildUrlWithContextPath("PlayerMove");
var COMPUTER_MOVE_URL = buildUrlWithContextPath("ComputerMove");
var VIEWERS_LIST_URL = buildUrlWithContextPath("viewers");
var CLEAR_PLAYER_RETIRED_URL = buildUrlWithContextPath("ClearPlayerRetired");
var historyIndex = 0;

$(function () {
    $.ajax({
        url:USER,
        dataType: 'json',
        success:function(data){
            $("#player-session-name").append("Hello " + data.PlayerName);
            if(data.Viewer !== undefined){
                $("#Chat").remove();
            }
        }
    });
    setInterval(getContent,1000);
});

function getContent(){
    getBoardContent();
    getStatisticsContent();
    getPlayersDetailsContent();
    getHistoryContent();
    getViewersListContent();
}

function getViewersListContent(){
    $.ajax({
        url: VIEWERS_LIST_URL,
        dataType: 'json',
        success:function(data){
            $("#viwers-userslist").empty();
            $.each(data.Viewers || [], createUser);
        }
    })
}

function getStatisticsContent(){
    $.ajax({
        url: STATISTICS_URL,
        dataType: 'json',
        success:function(data){
            $("#Target").empty();
            $("#Target").append(data.Target);//ToDO: check on children attribute
            $("#Varient").empty();
            $("#Varient").append(data.Varient);
            $("#Waiting-message").empty();
            if(data.GameActive === "PRE_GAME"){
                var str = data.WaitingMessage.replace("\n","<br>");
                $("#Waiting-message").append(str);
//                $("#Waiting-message")[0].innerText.replace("\n","<br>");
            }
            if(data.GameActive === "GAMING"){
                $("#Waiting-message").empty();
                $("#Current-name-turn").empty();
                $("#Current-name-turn").append(data.PlayerNameTurn);
                $("#Time-elapsed").empty();
                $("#Time-elapsed").append(data.Time);
                $("#Next-Player").empty();
                $("#Next-Player").append(data.NextPlayerName);
                if($(".game-label")[0].style.visibility !== "visible") {
                    $.each($(".game-label") || [], setVisible);
                    $.each($(".title-label") || [], setVisible);
                }
                if(data.ClearError)
                    clearErrorMessage();
                if(data.ComputerTurn === "Yes")
                    setTimeout(ComputerMove,1000);
            }
            else if(data.GameActive === "END_GAME"){
                $("#Current-name-turn").empty();
                $("#Time-elapsed").empty();
                $("#Next-Player").empty();
                if($(".game-label")[0].style.visibility === "visible")
                    $.each($(".game-label") || [], setHidden)
            }
            $("#Winners-Message").empty();
            $("#Winners-Message").append(data.Winners);

        }
    })
}

function createUser(index,dataJson) {
    $('<li id = "viewer">'+ (index + 1) + ". " + dataJson.m_Name + '</li>').appendTo($("#viwers-userslist"));
}

function setHidden(index,data){
    data.style.visibility = "hidden";
}

function setVisible(index,data){
    data.style.visibility = "visible";
}

function getPlayersDetailsContent() {
    $.ajax({
        url: PLAYERS_DETAILS_URL,
        dataType: 'json',
        success: function (data) {
            if (data.GameActive !== "PRE_GAME") {
                $("#playersDetails").empty();
                $("#playersDetails").css({"visibility":"visible"});
                for (var i = 0; i < data.Players.length; ++i) {
                    var player = $("<div class = 'playerDetails'>");
                    var name = $("<label class = 'playerName'>").append("Name: " + data.Players[i].PlayerName).append($("<br>"));
                    var type = $("<label class = 'playerType'>").append("Type: " +data.Players[i].Type).append($("<br>"));
                    var colorOnBoard = $("<label class = 'playerColor'>").append("Color: " +data.Players[i].Color).append($("<br>"));
                    var turnsPlayed = $("<label class = 'playerTurns'>").append("Turns Played: " +data.Players[i].Turns).append($("<br>"));
                    var seperator = $("<hr>");
                    player.append(name, type, colorOnBoard, turnsPlayed,seperator);
                    $("#playersDetails").append(player);
                }
            }
        }
    })
}

function getHistoryContent(){
    $.ajax({
        url: HISTORY_URL,
        data: "Index=" + historyIndex,
        dataType: 'json',
        success:function(data){
            if(data.GameActive !== "PRE_GAME" && data.HistoryMoves && data.index !== historyIndex){
                $("#History").css({"visibility":"visible"});
                $.each(data.HistoryMoves || [], appendHistoryContent);
                historyIndex = data.index;
            }
        }
    })
}

function appendHistoryContent(index,data) {
    var newHistoryItem = $("<li class ='History-Item'>")
    newHistoryItem[0].innerHTML = data;
    $("#History-List").append(newHistoryItem);
}

function getBoardContent(){
    $.ajax({
        url: BOARD_GAME_URL,
        dataType: 'json',
        success:function(data){
            var newBoard = $("#board");
            newBoard.empty();
            var row;
            var col;
            for (row = 0; row < data.Rows; row++) {
                for(col = 0; col < data.Cols; col++){
                    var button;
                    if(data.GameStatus === "GAMING" && data.ComputerPlayer === "false" && data.Viewer === undefined) {
                        if (row === 0) {
                            button = $("<button id='board-cell-button' onclick='humanPlayerMove(this,false)'>");
                            button[0].setAttribute("Col",col);
                        }
                        else if (row === (data.Rows - 1) && data.Varient === "POPOUT") {
                            button = $("<button id ='board-cell-button' onclick='humanPlayerMove(this,true)' >");
                            button[0].setAttribute("Col",col);
                        }
                        else {
                            button = $("<button id ='board-cell-button' type='submit' disabled>")
                        }
                    }
                    else{
                            button = $("<button id ='board-cell-button' type='submit' disabled>")
                    }
                    if(data.Board[row][col]){
                        button[0].style.backgroundImage = 'url(../../resources/' + data.Board[row][col].m_Sign +'.png)';
                    }
                    else
                    {
                        button[0].style.backgroundImage = 'url(' + '../../resources/background.png' + ')';
                    }
                    button.css({"background-size":"cover"});
                    newBoard.append(button);
                }
                newBoard.append($("<br>"));
            }
            if(data.GameStatus === "END_GAME")
                setWinnersAnimation(data);
            if(data.GameStatus === "PRE_GAME" && data.ActiveGame === "Yes"){
                startGame();
            }
            if(data.PlayerRetired){
                $("#Player-Retired-message").empty();
                $("#Player-Retired-message").append(data.PlayerRetired);
                setTimeout(clearPlayerRetiredMessage, 5000);
            }
        }
    })
}

function clearPlayerRetiredMessage(){
    $("#Player-Retired-message").empty();
    $.ajax({
        url: CLEAR_PLAYER_RETIRED_URL,
        method: "POST",
        success: function () {
        }
    })
}

function setWinnersAnimation(data){
    var Board = $("#board");
    for(var i = 0; i< data.WinnersPoints.length;++i){
        Board[0].children[(data.Cols + 1)*data.WinnersPoints[i].y + data.WinnersPoints[i].x].setAttribute("id","Winner-Button");
    }
}

function humanPlayerMove(button,popout){
    var col = button.getAttribute("Col");
    $.ajax({
        url: PLAYER_MOVE_URL,
        data: {
            "Col" : col,
            "Popout" : popout,
        },
        method: "POST",
        success:function(data){
            if(data !== "null"){
                $("#error-move-message").empty();
                $("#error-move-message").append(data);
                setTimeout(clearErrorMessage,10000);
            }
            else{
                $("#error-move-message").empty();
            }
        }
    })
}

function ComputerMove(){
    $.ajax({
        url: COMPUTER_MOVE_URL,
        method: "POST",
        success:function(data){
            /*if(data !== "null"){
                setTimeout(ComputerMove(),1000);
            }*/
        }
    })
}

function clearErrorMessage(){
    $("#error-move-message").empty();
}

function startGame(){
    $.ajax({
        url: START_GAME_URL,
        method: "POST",
        success:function(data){}
    })
}