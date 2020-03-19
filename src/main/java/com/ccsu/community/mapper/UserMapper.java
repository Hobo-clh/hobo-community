package com.ccsu.community.mapper;

import com.ccsu.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    @Insert("insert into user(login_name,account_id,token,gmt_create,gmt_modified) values(#{loginName},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insertUser(User user);
}
