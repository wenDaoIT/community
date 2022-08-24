package top.tom666.community.entity;

/**
 * @author: liujisen
 * @date： 2022-08-24
 */

import lombok.Data;

/**
 * 封装分页查询的组件
 */
//@Data
public class Page {
    //当前页码
    private int current = 1;
    //每页限制查询条数
    private int limit = 10;
    //数据总数
    private int count;

    @Override
    public String toString() {
        return "Page{" +
                "current=" + current +
                ", limit=" + limit +
                ", count=" + count +
                ", path='" + path + '\'' +
                '}';
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current>=1){
        this.current = current;
        }
    }

    public int getLimit() {
            return limit;
    }

    public void setLimit(int limit) {
        if (limit>=1 && limit<=100){
            this.limit = limit;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //查询路径
    private String path;

    /**
     * @return 获取当前页的起始行
     */
    public int getOffset(){
        return (current-1) * limit;
    }

    /**
     * @return 获取总页数
     */
    public int getTotal(){
        //是否可以被整除
        if (count % limit == 0 ){
            return count / limit;
        }
        return (count / limit) +1;
    }

    /**
     * @return 获取起始页
     */
    public int getFrom(){
        int from = current-1;
        return from < 1 ? 1 : from;
    }

    /**
     * @return 获取结束页码
     */
    public int getTo(){
        int to = current +2;
        return to > getTotal() ? getTotal() : to;
    }
}
