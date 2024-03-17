package world.evgereo.articles.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    private int parentId;

    @Column(name = "article_id")
    private int articleId;

    @OneToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private User author;

    private int toUserId;

    private String content;
}
