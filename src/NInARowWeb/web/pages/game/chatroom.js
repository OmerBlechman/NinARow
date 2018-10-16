var chatVersion = 0;
var refreshRate = 2000; //mili seconds
var CHAT_LIST_URL = buildUrlWithContextPath("Chat");
var SEND_CHAT_URL = buildUrlWithContextPath("SendChat");

//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]

//entries = the added chat strings represented as a single string
function appendToChatArea(entries) {
    $.each(entries || [], appendChatEntry);
    var scroller = $("#chatarea");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({ scrollTop: height }, "slow");
}

function appendChatEntry(index, entry){
    var entryElement = createChatEntry(entry);
    $("#chatarea").append(entryElement).append("<br>");
}

function createChatEntry (entry){
    return $("<span class=\"success\">").append(entry.username + ": " + entry.chatString);
}


function ajaxChatContent() {
    $.ajax({
        url: CHAT_LIST_URL,
        data: "chatversion=" + chatVersion,
        dataType: 'json',
        success: function(data) {
            /*
             data is of the next form:
             {
                "entries": [
                    {
                        "chatString":"Hi",
                        "username":"bbb",
                        "time":1485548397514
                    },
                    {
                        "chatString":"Hello",
                        "username":"bbb",
                        "time":1485548397514
                    }
                ],
                "version":1
             }
             */
            if (data.version !== chatVersion) {
                chatVersion = data.version;
                appendToChatArea(data.entries);
            }
            triggerAjaxChatContent();
        },
        error: function(error) {
            triggerAjaxChatContent();
        }
    });
}

//add a method to the button in order to make that form use AJAX
//and not actually submit the form
$(function() { // onload...do
    $("#chatform").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: SEND_CHAT_URL,
            timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {}
        });

        $("#userstring").val("");
        return false;
    });
});

function triggerAjaxChatContent() {
    setTimeout(ajaxChatContent, refreshRate);
}

$(function() {
    $.ajaxSetup({cache: false});
    triggerAjaxChatContent();
});