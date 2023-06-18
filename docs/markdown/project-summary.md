# Summary

Below is a quick summary of a lot fo the stuff I did not have time to do.

I want to look into as I am not a java developer and I did not have the most time, but also wanted to make sure I was actually learning what I was doing and not just rushing something that I could 100% copy online and worked.
So I had to try and find a balance.

There is a lot still to be done here but hopefully I have done enough to give you an idea of what I could do if I had more time to learn java properly and also just ahve more time to spend on it in general

---

## Improvements

There are a list of improvements that could worked on

### Home Controller

I created it to manage all the endpoints that lead to html pages but it should be split up

### Proper ISBN Usage

We have a [custom validator](../../src/main/java/scopeland/libraryapp/validation/books/validator/IsbnValidator.java) that validates an ISBN is valid

This is a very simple ISBN Validator.
Since I am not a Java developer I though it would be good experience to write a custom validator
ISBNs are not this simple, they have check digits and you could apply a very strict regex to them
They should only have spaces or dashes, a maximum of 3 and not a mix of both
If I was to actually use an ISBN in a production app I would just use the pre-built validator:
https://docs.jboss.org/hibernate/stable/validator/api/org/hibernate/validator/constraints/ISBN.html

### Pagination of Lists

Obviously on a much larger scale the list view would been to be paginated. I had a look at it and it looks like springboot has the functionality to deal with it.

- [Sprintboot Pagination](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Pageable.html)
- [Thymeleaf Pagination](https://www.bezkoder.com/thymeleaf-pagination/)

### Authentication Process

I should also highlight a lot of the user stuff (especially the UI) was just created and then tweaked from a tutorial

#### Obscure Password

I have noticed that the password in the register api call gets exposed and is readable, need to look into obscuring that.

### Frontend UI

Since the backend seemed the slightly more important part from the brief I focused on it slightly more.

#### Error Modal

Most of the error feedback was handled by valdiators, if I had a bit more time I would use the success modal to give error feedback as well.

#### React

Looking back the frontend work would have been easier if I just hosted and proxied a mini react app, but using raw html and some thymeleaf was a good learning experience, very similar to knockout except not quite the same level of observability.

#### Typescript

Javascript is terrifying, I would rather use and bundle typescript which is easy enough in react but didn't think it was worth the time for this small demo.

#### Better html isolation

I split out the html and functions a bit but all the imports are required in the main html file as the thymeleaf fragment does not include the head or scripts.
I imagine it is possible to import them all into the related file and bundle them a bit better using directories and I would like to spend more time looking into that.

#### UI Testing

If I had more time i would look in to end to end UI testing tools such as selenium, datadog or cypress.
Plus some actual js unit tests.

### Java Testing

I was a bit tight for time so focused on one area for testing.
The main concern would be running tests in parallel to reduce the time taken.

#### Database Loader

I took advantage of the database loader to put in some default data for the tests.
If this was a real work production project I would be unlikely to include this and manage our database through an external tool such as liquibase.

I would therefore create a helper that was designed for inserting seed data into the database and add it to the BeforeEach section of the tests

#### Split out Tests

All the tests are under the book controller tests at the moment.
I would like to split them out more, in reality the book controller tests should mock the book service and validator and only test the endpoints are getting reached correctly and calling the service as expected.
Any logic for actual access code should be moved to the service and it would be good if we could directly test the validator.

#### Grouping Related Tests

It would be good to group tests in a better way that just wrapping them in regions.

#### Jsoup Tests

I also need to add specific Unit tests for testing the impact of jsoups cleaning methods add/update book methods.

---

### Bonus Task

I did a quick version of the bonus task at the end.

On the edit book page I added the status which is an enum in the backend.
It defaults to "Available"

The UI only lets you return it if its borrowed and only borrow it if its available.

The backend does throw an exception if you try to borrow it and it is already borrowed.

The only bit left to do was to make note of the user that borrowed it.

This could be done by making a note of the current user when the borrow button is pressed.
Then make it so that the return button is only pressable if you are the user that borrowed it.

I would store the link between User and Book in a separate table.
It would probably be okay in the same table if the project stays small scaled.

But looking at the project I would expect it to reach the point where we have an amount in the book table to determine how many are in library (not just 1).
And we could have the count for how many books are borrowed overall.
But if we put a direc tlink to the user in the book table at this point it will just cause more problems if we need to track multiple borrowers of the same book moving forward.
