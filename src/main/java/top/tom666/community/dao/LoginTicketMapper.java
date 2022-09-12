package top.tom666.community.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import top.tom666.community.entity.LoginTicket;

/**
 * @author: liujisen
 * @date： 2022-08-27
 */
@Repository
@Deprecated
public interface LoginTicketMapper {


    /**
     * @param ticket
     * @return 通过ticket获取userid和过期时间
     */
    @Select({"select id,user_id,ticket,status,expired from login_ticket ",
        "where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Insert({"insert into login_ticket (user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"})
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);


    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket}",
            "<if test = \" ticket != null\">",
            "and 1=1",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket,int status);
}
