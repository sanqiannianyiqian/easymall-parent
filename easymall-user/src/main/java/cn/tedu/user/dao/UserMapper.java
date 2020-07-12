package cn.tedu.user.dao;

import com.jt.common.pojo.User;

public interface UserMapper {
     int selectCountByUserName(String userName);

    void insertUser(User user);

     User selectUserByNameAndPw(User user);
}
