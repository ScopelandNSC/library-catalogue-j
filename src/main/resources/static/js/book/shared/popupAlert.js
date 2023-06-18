// Update and display popup for a success message
function showSuccess(message) {

    $("#popup-title").css({"background-color": "green"});
    $("#popup-body").css({"background-color": "lightgreen"});

    $("#popup-title").text("Success");
    $("#popup-body").text(message);


    $("#popup-alert").css({"opacity": 1, "display": "inline-block"});
    $("#popup-alert").fadeOut(2000);
}