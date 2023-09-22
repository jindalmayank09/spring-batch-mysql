package com.springbatch.batch.processor;

import com.springbatch.batch.request.StudentBean;
import org.springframework.batch.item.ItemProcessor;

import java.util.Objects;

public class StudentProcessor implements ItemProcessor<StudentBean, StudentBean> {
    @Override
    public StudentBean process(StudentBean item) throws Exception {
        if(Objects.nonNull(item))
        if (item.getCgpa() >= 7.5) {
            item.setDistinction("yes");
        }else{
            item.setDistinction("no");
        }
        return item;
    }

}
