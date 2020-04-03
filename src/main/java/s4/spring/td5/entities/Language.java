package s4.spring.td5.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
public class Language
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "language")
    private List<Script> scripts;

    private String name;

    public Language()
    {

    }
    public Language(String name)
    {
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
