package model.objects;

/*
    Vi kan lägga till checkValidData för book i denna klass.
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Book {
    private String title;
    private ArrayList<Author> authorArrayList;
    private String isbn;
    private String description;
    private int nrOfPages;
    private int rating;
    private Category category;

    public Book(String title, String isbn, String description, int nrOfPages, int rating, Category category) {
        this.title = title;
        this.authorArrayList = new ArrayList<>();
        this.isbn = isbn;
        this.description = description;
        this.nrOfPages = nrOfPages;
        this.rating = rating;
        this.category = category; }

    public void addAuthors(ArrayList<Author> authors){
        this.authorArrayList.addAll(authors);
    }

    public ArrayList<Author> getAuthorArrayList() {
        return authorArrayList;
    }
    /**
     * Method to access the Isbn  value
     * @return the books ISBN number
     */
    public String getIsbn() { return isbn; }

    /**
     * returns the category the book is
     * @return the category
     */
    public String getCategory() { return category.toString(); }

    /**
     * returns the books number of pages
     * @return the nrOfPages
     */
    public int getNrOfPages() { return nrOfPages; }

    /**
     * returns the books rating
     * @return the rating
     */
    public int getRating() { return rating; }

    /**
     * returns the books description
     * @return the description
     */
    public String getDescription() { return description; }

    /**
     * returns the books title
     * @return the title
     */
    public String getTitle() { return title; }

    /**
     * Custom toString
     *
     * @return return the books title, isbn-number, rating and category in string format
     */

    static public boolean checkValidTitle(String title){
        return !title.trim().isEmpty() && title.length() < 31;
    }

    /**
     * Checks if the given ISBN is a valid ISBN-number
     * @param ISBN
     * @return boolean value for if it's valid
     */
    static public boolean checkValidISBN(String ISBN){
        return !ISBN.trim().isEmpty();
    }

    /**
     * Checks if the given pages-value is valid
     * @param pages
     * @return boolean value for if it's valid
     */
    static public boolean checkValidPages(String pages){
        if(pages.trim().isEmpty())
            return false;
        return Integer.parseInt(pages) > 0;
    }

    static public boolean checkValidRating(String rating){
        if(rating.trim().isEmpty())
            return false;
        else
            return (Integer.parseInt(rating) > 0 && Integer.parseInt(rating) < 5);
    }

    @Override
    public String toString() {
        return title + ", " + isbn +", " + rating + "/5, " + category.toString().toLowerCase() +  authorArrayList.toString() + "\n";
    }

}
