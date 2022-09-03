package top.tom666.community.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-09-03
 */


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
