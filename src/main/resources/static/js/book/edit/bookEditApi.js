/* 
 * Get Book
 * Calls the Get Book api endpoint
 * On Success: Loads book into inputs, enables other buttons
 * On Error: Validates inputs, disables other buttons
 */
function getBook()
{
    var isbn = document.getElementById("input-isbn").value;

    axios
    .get(`/api/books/${isbn}`)
    .then((response) => {
      const book = response.data;
            if(book.isbn !== undefined){
                setEditButtonsAreDisabled(false)
                
                $('#input-title').val(book.title);
                $('#input-author').val(book.author);
                $('#input-publicationYear').val(book.publicationYear);
                $('#update-bookStatus').text(book.bookStatus)
                
                $('#update-status-button').text(book.bookStatus === "AVAILABLE" ? "Borrow Book" : "Return Book")
                $("#update-status-button").attr("onclick",book.bookStatus ===  "AVAILABLE" ? "takeBook()" : "returnBook()");
            } else {
                setEditButtonsAreDisabled(true)
            }

    }).catch(error => {
        handleIsbnPathError(error,"getByIsbn.isbn:");
    })
}

/* 
 * Delete Book
 * Calls the Delete Book api endpoint
 * On Success: Clears inputs, disables other buttons, shows success popup
 * On Error: Validates isbn input, disables other buttons
 */
function deleteBook()
{
    var isbn = document.getElementById("input-isbn").value;

    clearAllErrorText()

    axios.delete(`/api/books/${isbn}`)
    .then((response) => {
        if(response.status === 200){
            showSuccess("Book Deleted")
            clearEditForm();
        }
    }).catch(error => {
        handleIsbnPathError(error,"deleteBookByIsbn.isbn:")
    })
}

/* 
 * Update Book
 * Calls the Update Book api endpoint
 * On Success: Clears inputs, disables other buttons, shows success popup
 * On Error: Validates inputs, disables other buttons if isbn invalid
 */
function updateBook()
{
    var isbn = document.getElementById("input-isbn").value;

    clearAllErrorText()

    const headers = {
        'Content-Type': 'application/json'
    }

    axios.put(`/api/books/${isbn}`,
        getBookFromInputs("update"),
        {
            headers: headers
        }
    ).then((response) => {
        if(response.status === 200){
            showSuccess("Book Updated")
            clearEditForm();
        }
    }).catch(error => {
        var errors = error?.response?.data?.errors;
        if(errors){
            errors.forEach(error => {
                $(`#error-${error.field}`).text(error.defaultMessage);
            });
        } else {
            handleIsbnPathError(error, "updateBook.isbn:");
        }
    })
}


/* 
 * Update Book Status
 * Calls the Update Book Status api endpoint
 * On Success: Clears inputs, disables other buttons, shows success popup
 * On Error: Validates inputs, disables other buttons if isbn invalid
 */
function updateBookStatus(status)
{    
    var isbn = document.getElementById("input-isbn").value;

    clearAllErrorText()

    const headers = {
        'Content-Type': 'application/json'
    }

    axios.put(`/api/books/${isbn}/updateStatus/${status}`, null, {
        headers: headers
    }).then((response) => {
        if(response.status === 200){
            showSuccess(`Book ${status}`);
            clearEditForm();
        }
    }).catch(error => {
        handleIsbnPathError(error, "updateBookStatus.isbn:");
    });

}


// Calls updateBookStatus but makes book available
function returnBook()
{    
    updateBookStatus("AVAILABLE");
}

// Calls updateBookStatus but makes book borrowed
function takeBook()
{    
    updateBookStatus("BORROWED")
}





