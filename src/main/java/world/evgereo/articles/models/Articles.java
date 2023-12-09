package world.evgereo.articles.models;

public class Articles {
    private int article_id;
    private int author_id;
    private String article_name;
    private String article_text;
    private String posting_date;

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getArticle_name() {
        return article_name;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    public String getArticle_text() {
        return article_text;
    }

    public void setArticle_text(String article_text) {
        this.article_text = article_text;
    }

    public String getPosting_date() {
        return posting_date;
    }

    public void setPosting_date(String posting_date) {
        this.posting_date = posting_date;
    }
}
