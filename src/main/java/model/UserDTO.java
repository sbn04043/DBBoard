package model;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String username;
    private String password;
    private String nickname;

    //이제 자바 내부에서 같은지 확인 안 하고
    //데이터베이스에서 확인을 하기 때문에
    //equals를 안 만들어도 된다.
//    @Override
//    public boolean equals(Object o) {
//        if (o == this) return true;
//        if (o instanceof  UserDTO u) {
//            return u.id == id;
//        }
//        return false;
//    }
}
