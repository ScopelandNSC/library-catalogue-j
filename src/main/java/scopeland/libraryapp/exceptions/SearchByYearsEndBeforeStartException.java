package scopeland.libraryapp.exceptions;

/*
 * An exception to throw if end year is before start year during search
 */
public class SearchByYearsEndBeforeStartException extends RuntimeException {
    public SearchByYearsEndBeforeStartException(Integer startDate, Integer endDate) {
        super("End date (" + endDate.toString() + ") needs to be before start date (" + startDate.toString() + ")");
    }
}