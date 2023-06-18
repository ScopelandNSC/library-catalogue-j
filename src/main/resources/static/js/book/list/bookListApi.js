/* 
 * List Books
 * Calls the List books api endpoint
 * On Success: Updates Book, List shows success popup
 */
function listBooks()
{
    clearSearchErrorText();
    axios.get("/api/books")
    .then((response) => {
        buildBookList(response.data);
        if(response.status === 200){
            showSuccess(`Books Loaded`);
        }
    }).catch(error => {
        var message = error.response.data;
    })
}

/* 
 * Search by Author
 * Calls the search author api endpoint
 * On Success: Updates Book List, shows success popup
 */
function searchAuthor()
{
    clearSearchErrorText();
    var author = document.getElementById("search-author").value;
    author = author.replace(" ", "+");
    axios.get((`/api/books/search/author/${author}`))
    .then((response) => {
        buildBookList(response.data);
        if(response.status === 200){
            showSuccess(`Search Complete`);
        }
    }).catch(error => {
        var message = error.response.data;
    })
}

/* 
 * Search by Publication Year Range
 * Calls the Search by Publication Year Range api endpoint
 * On Success: Updates Book List, shows success popup
 */
function searchPublicationYearRange()
{
    clearSearchErrorText();

    var startYear = document.getElementById("search-year-start").value;
    var endYear = document.getElementById("search-year-end").value;

    axios.get(`/api/books/search/publicationYear/range/${startYear}/${endYear}`)
    .then((response) => {
        buildBookList(response.data);
        if(response.status === 200){
            showSuccess(`Search Complete`);
        }
    }).catch(error => {
        updateSearchErrorText("search-year", error?.response?.data);
    })
}