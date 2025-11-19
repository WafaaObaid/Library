
package mysmartlibrary;


public class Book {
    // (id, title, author, status)
    private int id;
    private String title;
    private String author;
    private  String status;
    private int numberOfSales;

    public Book(int id, String title, String author, String status, int numberOfSales){
        this.id=id;
        this.title=title;
        this.author=author;
        this.status=status;
        this.numberOfSales= numberOfSales;
        
        
    }
   
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getTitle() {
        return title;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

   
    public String getAuthor() {
        return author;
    }

    
    public void setAuthor(String author) {
        this.author = author;
    }

   
    public String getStatus() {
        return status;
    }

   
    public void setStatus(String status) {
        this.status = status;
        
        
       
    }

    
    public int getNumberOfSales() {
        return numberOfSales;
    }

  
    public void setNumberOfSales(int numberOfSales) {
        this.numberOfSales = numberOfSales;
    }
}
