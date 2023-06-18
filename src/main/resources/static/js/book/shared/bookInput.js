// Update a validation text fields error message
function updateErrorText (field, message){
    $(`#error-${field}`).text(message);
}

// Clear all validation text fields error message
function clearAllErrorText(){
    $('.book-input-error').text(null);
}

// Get the isbn from the isbn input field
function getIsbnFromInput(){
    return document.getElementById("input-isbn").value;
}

// Construct a book object based off of the input fields
function getBookFromInputs(action){

    
    var isbn = document.getElementById("input-isbn").value;
    var author = document.getElementById("input-author").value;
    var title = document.getElementById("input-title").value;
    var publicationYear = document.getElementById("input-publicationYear").value;
    var bookStatus = "AVAILABLE";

    switch (action){
        case "update":
            bookStatus = document.getElementById("update-bookStatus").textContent;
            break;
        case "add":
        default:
    }

    return JSON.stringify({ isbn: isbn, author: author, title: title, publicationYear: publicationYear, bookStatus:bookStatus})
}