package world.evgereo.articles.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Articles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int articleId;

    @Column(name = "author_id", insertable=false, nullable = false, updatable=false)
    private int authorId;

    private String articleName;

    private String articleText;

    @CreationTimestamp
    @Column(name = "posting_date", nullable = false, updatable = false)
    private String postingDate;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id", nullable = false)
    private Users author;
}
