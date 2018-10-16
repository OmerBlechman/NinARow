
$(function () {
    setInterval(getErrorResponse,1000);

});

function getErrorResponse() {
    $.ajax({
        method: 'GET',
        url: "Login",
        timeout: 200,
        success: function (response) {
            if(response !== "null"){
                $("#error-name").empty();
                $("#error-name").append(response);
            }
        },
    });
}