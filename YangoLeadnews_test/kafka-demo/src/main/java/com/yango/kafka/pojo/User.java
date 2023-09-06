package com.yango.kafka.pojo;

/**
 * ClassName: User
 * Package: com.yango.kafka.pojo
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/31-14:23
 */
public class User {

    private String username;
    private Integer age;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
