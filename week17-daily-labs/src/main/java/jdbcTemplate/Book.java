package jdbcTemplate;

public class Book {
    private Long id;
    private String writer;
    private String title;
    private int price;
    private int pieces;

    public Book(Long id, String writer, String title, int price, int pieces) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.price = price;
        this.pieces = pieces;
    }

    public Long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getPieces() {
        return pieces;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", pieces=" + pieces +
                '\n';
    }
}
