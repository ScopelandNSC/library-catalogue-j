/* 
 * Add Book
 * Calls the Add Book api endpoint
 * On Success: Clears down the inputs and adds a success popup
 * On Error: Validates inputs
 */
function addBook()
{
    clearAllErrorText();
    
    const headers = {
        'Content-Type': 'application/json'
    }
    axios.post(`/api/books`,
        getBookFromInputs("add"),
        {
            headers: headers
        }
    ).then((response) => {
        if(response.status === 200){
            showSuccess("Book Added")
            $(':input').val('');
        }
        
    }).catch(error => {
        var errors = error?.response?.data?.errors;
        if(errors){
            errors.forEach(error => {
                updateErrorText(error.field,error.defaultMessage);
            });
        }
    })
}





