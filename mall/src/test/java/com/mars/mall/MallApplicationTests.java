package com.mars.mall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallApplicationTests {

    @Test
    public void load(){
        //加上这个方法是为了在mvn打包测试时不会报出项目初始化失败的错误
    }
}
