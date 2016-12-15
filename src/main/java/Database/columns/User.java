package Database.columns;

import Database.annotations.Column;
import Database.annotations.KeyColumn;
import Database.annotations.Table;

/**
 * Created by nikita on 20.09.16.
 */
@Table(table = "users")
public class User {

    @KeyColumn
    @Column(column = "user_id")
    private Integer id;

    @Column(column = "name")
    private String name;

    @Column(column = "info")
    private String info;

    @Column(column = "rate")
    private Integer rate = 0;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String name, String info, Integer rate, String tags, String publishedMsgIds, String watchedMsgIds) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return String.format("User:\tid: %d\n\t\tname: %s\n\t\tinfo: %s\n\t\trate: %d", id, name, info, rate);
    }
}
