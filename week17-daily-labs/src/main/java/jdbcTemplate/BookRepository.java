package jdbcTemplate;


import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class BookRepository {

    private JdbcTemplate jdbcTemplate;

    public BookRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insertBook(String writer,String title,int price, int pieces){
        jdbcTemplate.update("insert into book(writer,title,price,pieces) values (?,?,?,?)",writer,title,price,pieces);
    }

    public List<Book> findBooksByWriter(String prefix){
        return jdbcTemplate.query("select * from book where writer like ?",
                (rs,i)-> new Book(rs.getLong("id"),rs.getString("writer"),rs.getString("title"),rs.getInt("price"),rs.getInt("pieces")),
                prefix+"%");
    }

    public void updatePieces(Long id,int pieces){
        jdbcTemplate.update("update book set pieces=pieces+? where id=?",pieces,id);
    }
}

