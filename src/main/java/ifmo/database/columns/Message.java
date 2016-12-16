package ifmo.database.columns;

import ifmo.database.annotations.Column;
import ifmo.database.annotations.KeyColumn;
import ifmo.database.annotations.Table;

/**
 * Created by nikita on 20.09.16.
 */
@Table(table = "messages")
public class Message implements Comparable<Message> {

    @KeyColumn
    @Column(column = "message_id")
    private Integer id;

    @Column(column = "text")
    private String text;

    @Column(column = "user_id")
    private Integer userId;

    @Column(column = "rate")
    private Integer rate = 0;

    public Message() {};

    public Message(String text, Integer userId, Integer rate) {
        this.text = text;
        this.userId = userId;
        this.rate = rate;
    }

    public Message(Integer id, String text, Integer userId, Integer rate) {
        this.id = id;
        this.text = text;
        this.userId = userId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public int compareTo(Message o) {
        return this.rate.compareTo(o.getRate());
    }
}
