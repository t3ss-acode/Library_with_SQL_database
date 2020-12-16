package model.objects;

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
        this.category = category;
    }

    public void addAuthors(ArrayList<Author> authors){
        this.authorArrayList.addAll(authors);
    }

    public ArrayList<Author> getAuthorArrayList() {
        return authorArrayList;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category.toString();
    }

    public int getNrOfPages() {
        return nrOfPages;
    }

    public int getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title + ", " + isbn +", " + rating + "/5, " + category.toString().toLowerCase() + "\n";
    }

}
