package qna.user;

public class UserFixture {
    public static User createJAVAJIGI(){
        return new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    public static User createSANJIGI(){
        return new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    }
}
