package s4.spring.td5.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.ALL})
    private Script scripts;

    private String date;

    private String content;

    private String comment;

    public History()
    {

    }

    public History(String date, String content, String comment)
    {
        this.date=date;
        this.content=content;
        this.comment=comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Script getScripts() {
        return scripts;
    }

    public void setScripts(Script scripts) {
        this.scripts = scripts;
    }
}
