package com.example.utils;

import com.example.demos.utils.SnowFlakeGenerateIDUtils;
import org.junit.jupiter.api.Test;

public class SnowFlakeGenerateTest {

    @Test
    void test(){
        SnowFlakeGenerateIDUtils worker = new SnowFlakeGenerateIDUtils(1,1,1);
        for (int i = 0; i < 22; i++) {
           /* try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            System.out.println("生成的第"+(i+1) + "个ID:\t" +worker.nextId());
        }
    }

}
