package database.columns;

import database.annotations.Column;
import database.annotations.KeyColumn;
import database.annotations.Table;

/**
 * Created by nikita on 20.09.16.
 */
@Table(table = "Tags")
public class Tag {
    @KeyColumn
    @Column(column = "ID")
    private Integer id;

    @Column(column = "tag")
    private String tag;

    public Tag(Integer id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public Tag(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
