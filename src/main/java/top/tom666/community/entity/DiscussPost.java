package top.tom666.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */
@Data
public class DiscussPost {
    private int id;

    private Integer userId;

    private String title;

    private String content;

    private Integer type;

    private Integer status;

    private Date createTime;

    private Integer commentCount;

    private Double score;
}
