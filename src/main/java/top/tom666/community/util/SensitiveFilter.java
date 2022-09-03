package top.tom666.community.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: liujisen
 * @date： 2022-09-01
 */
@Component
@Slf4j
public class SensitiveFilter {
    private static final String REPLACE = "*";

    //构造根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try (
                //获取字节流
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //字节流再转换成缓冲流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ){
            String keyword;

            while ((keyword = bufferedReader.readLine()) != null){

                this.addkeyWord(keyword);
            }
        } catch (IOException e) {
            log.error("加载敏感词初始化失败",e.getMessage());
        }
    }

    /**将字符加入
     * @param
     */
    private void addkeyWord(String keword){
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keword.length(); i++) {
            char c = keword.charAt(i);
             TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null){
                //如果不存在，新建一个子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            tempNode = subNode;
            if ( i == keword.length() -1){
                //设置结束标识
                tempNode.setKeyword(true);
            }
        }

    }

    /**
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }
        //指针1
        TrieNode temp  = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;

        //存放结果
        StringBuilder sb = new StringBuilder();

        while (begin<text.length() -1){
            char c =text.charAt(position);
            if (isSymbol(c)){
                //若指针1处于根节点
                if (temp == rootNode){
                    sb.append(c);
                    begin++;
                }
                position++;
            }
            temp=temp.getSubNode(c);
            if (temp == null){
                //以begin开头的不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个节点
                position = ++begin;
                temp = rootNode;
            }else if (temp.isKeyword()){
                //发现敏感词
                sb.append(REPLACE);
                begin = ++ position;
            }else {
                if (position < text.length() -1){
                    position++;
                }
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }
    //判断是否为符号
    private boolean isSymbol(Character c){
        //同时过滤东亚文字
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c>0x9FFF);
    }


    //前缀树
    private class TrieNode{
        private boolean isKeyword = false;

        //子节点（key是字符，value是下级节点）
        private Map<Character,TrieNode> subNode = new HashMap<>();

        public void addSubNode(Character c, TrieNode trieNode){
            subNode.put(c,trieNode);
        }

        public TrieNode getSubNode(Character c){
            return subNode.get(c);
        }

        public boolean isKeyword(){
            return isKeyword;
        }
        public void setKeyword(boolean isKeywordend){
            isKeyword = isKeywordend;
        }
    }

}
