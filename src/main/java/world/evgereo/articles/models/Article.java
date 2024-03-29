package world.evgereo.articles.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int articleId;

    private String articleName;

    private String articleText;

    @CreationTimestamp
    @Column(name = "posting_date", nullable = false, updatable = false)
    private String postingDate;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    @JsonManagedReference
    private User author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "article_id", updatable = false)
    private List<Comment> comments;
}
