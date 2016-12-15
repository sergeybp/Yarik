package Database.columns;

import Database.annotations.Column;
import Database.annotations.KeyColumn;
import Database.annotations.Table;

/**
 * Created by nikita on 20.09.16.
 */
@Table(table = "tags")
public class Tag {

    @KeyColumn
    @Column(column = "tag")
    private String tag;

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
