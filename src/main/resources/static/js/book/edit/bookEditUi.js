// Handles isbn validation when it is a path variable
function handleIsbnPathError (error, replaceString){
    if(error?.response?.data && (typeof error?.response?.data) === 'string'){
        updateErrorText("isbn", error?.response?.data)
    } else if(error?.response?.data?.message){
        var errorText = error?.response?.data?.message.replaceAll(replaceString, " - ");
        updateErrorText("isbn", errorText);
    }
    setEditButtonsAreDisabled(true);
}

// Allows us to enable/disable edit booking buttons
function setEditButtonsAreDisabled(disabled){
    $('#delete-book-button').prop('disabled', disabled)
    $('#update-book-button').prop('disabled', disabled)
    $('#update-status-button').prop('disabled', disabled)
}

// Clears the edit form inputs and disables and edit buttons
function clearEditForm(){
    $(':input').val('');
    setEditButtonsAreDisabled(true);
    $('#update-bookStatus').text("N/A");
}