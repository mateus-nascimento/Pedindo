package ledare.com.br.pedindo.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String id;
    public String username;
    public String email;
    public String image;
    public boolean active;

    public User () {

    }
}
