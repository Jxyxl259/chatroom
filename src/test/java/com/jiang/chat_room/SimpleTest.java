package com.jiang.chat_room;

import com.google.common.collect.Iterables;
import com.jiang.chatroom.entity.User;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author:
 * @create: 2019-03-06 16:23
 */
public class SimpleTest {

    public static void main(String[] args) {

        List<User> list = new ArrayList<>();
        list.add(new User(1L,"j"));
        list.add(new User(2L,"ji"));
        list.add(new User(3L,"jia"));
        list.add(new User(4L,null));
        list.add(new User(5L,"jiang"));

        Iterables.removeIf(list, (u) -> StringUtils.isEmpty(u.getUserName()));

        list.forEach( System.out::println);
    }

}
