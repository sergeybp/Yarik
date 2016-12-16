package ifmo.database.columns;

import ifmo.database.annotations.Column;
import ifmo.database.annotations.KeyColumn;
import ifmo.database.annotations.Table;

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
