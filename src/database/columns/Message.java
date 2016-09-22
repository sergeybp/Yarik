package database.columns;

import database.annotations.Column;
import database.annotations.KeyColumn;
import database.annotations.Table;

/**
 * Created by nikita on 20.09.16.
 */
@Table(table = "Messages")
public class Message {
    @KeyColumn
    @Column(column = "ID")
    private Integer id;

    @Column(column = "tag_id")
    private Integer tagId;

    @Column(column = "text")
    private String text;

    @Column(column = "publisher_id")
    private Integer publisherId;

    @Column(column = "rate")
    private Integer rate;

    public Message(Integer id, Integer tagId, String text, Integer publisherId, Integer rate) {
        this.id = id;
        this.tagId = tagId;
        this.text = text;
        this.publisherId = publisherId;
        this.rate = rate;
    }

    public Message(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
