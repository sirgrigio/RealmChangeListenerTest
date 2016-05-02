package example.realmchangelistenertest;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created on 02/05/2016.
 *
 * @author Matteo Zaccagnino
 */
public class Post extends RealmObject {

    @PrimaryKey
    private long id;
    private String title;

    public Post() {
    }

    public Post(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
