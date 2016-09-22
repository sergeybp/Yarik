package database.columns;

import database.annotations.Column;
import database.annotations.KeyColumn;
import database.annotations.Table;

/**
 * Created by nikita on 20.09.16.
 */
@Table(table = "Users")
public class User {
    @KeyColumn
    @Column(column = "ID")
    private Integer id;

    @Column(column = "name")
    private String name;

    @Column(column = "surname")
    private String surname;

    @Column(column = "rate")
    private Integer rate;

    @Column(column = "published_msg_ids")
    private String publishedMsgIds;

    @Column(column = "watched_msg_ids")
    private String watchedMsgIds;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String name, String surname, Integer rate, String publishedMsgIds, String watchedMsgIds) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.rate = rate;
        this.publishedMsgIds = publishedMsgIds;
        this.watchedMsgIds = watchedMsgIds;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getPublishedMsgIds() {
        return publishedMsgIds;
    }

    public void setPublishedMsgIds(String publishedMsgIds) {
        this.publishedMsgIds = publishedMsgIds;
    }

    public String getWatchedMsgIds() {
        return watchedMsgIds;
    }

    public void setWatchedMsgIds(String watchedMsgIds) {
        this.watchedMsgIds = watchedMsgIds;
    }
}
