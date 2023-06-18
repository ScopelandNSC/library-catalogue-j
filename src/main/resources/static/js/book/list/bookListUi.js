// Builds Book list ui using jquery
function buildBookList(books){
    $('#books').empty();
    $('#books').append($(getBookListTitleRow()));
    books.forEach(book => {
        console.log(book);
        $('#books').append($(getBookListEntryRow(book)));
    });
}

// Builds the book list title row UI
function getBookListTitleRow() {
    return `<tr class="book-list-row book-list-title-row"> 
                <td class="book-list-column book-list-title-column">ISBN</td>
                <td class="book-list-column book-list-title-column">Title</td>
                <td class="book-list-column book-list-title-column">Author</td>
                <td class="book-list-column book-list-title-column">Publication Year</td>
                <td class="book-list-column book-list-title-column">Status</td>
            </tr>`;
}

// Builds the book list entry row UI
function getBookListEntryRow(book) {
    return `<tr class="book-list-row"> 
                <td class="book-list-column">${book.isbn}</td>
                <td class="book-list-column">${book.title}</td>
                <td class="book-list-column">${book.author}</td>
                <td class="book-list-column">${book.publicationYear.toString()}</td>
                <td class="book-list-column">${book.bookStatus}</td>
            </tr>`;
}



function updateSearchErrorText (field, message){
    $(`#error-${field}`).text(message);
}

// Clear all validation text fields error message
function clearSearchErrorText(){
    $('.search-input-error').text(null);
}