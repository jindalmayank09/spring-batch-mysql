package com.springbatch.batch.config;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.springbatch.batch.StudentRepository;
import com.springbatch.batch.model.Student;
import com.springbatch.batch.request.StudentBean;
import com.springbatch.batch.processor.StudentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class BatchConfig {

    final JobRepository jobRepository;
    final PlatformTransactionManager batchTransactionManager;

    @Autowired
    StudentRepository studentRepository;
    public static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);
    private static final int BATCH_SIZE = 10;


    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager batchTransactionManager) {
        this.jobRepository = jobRepository;
        this.batchTransactionManager = batchTransactionManager;
    }

    /**
     * Job which contains multiple steps
     */
    @Bean
    public Job firstJob() {
        return new JobBuilder("first job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .build();
    }

    @Bean
    public Step taskletStep() {
        return new StepBuilder("first step", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    logger.info("This is first tasklet step");
                    logger.info("SEC = {}", chunkContext.getStepContext().getStepExecutionContext());
                    return RepeatStatus.FINISHED;
                }, batchTransactionManager).build();
    }


    @Bean
    public ItemReader<StudentBean> reader() {
        List<StudentBean> studentBeans = readingContentsFromCsvFile();
        return new ListItemReader<>(studentBeans);
    }

    @Bean
    public ItemWriter<StudentBean> writer() {
        List<Student> students = new ArrayList<Student>();
        return items -> {
            for (StudentBean item : items) {
                Student student = new Student();
                BeanUtils.copyProperties(item,student);
                students.add(student);
                logger.info("Writing  Student data item: into db::::::>{}", item);
            }
            studentRepository.saveAllAndFlush(students);
            logger.info("------------ BATCH_SIZE: All documents written. ------------");
        };
    }

    @Bean
    public StudentProcessor processor() {
        return new StudentProcessor();
    }

    @Bean
    public Step chunkStep() {
        return new StepBuilder("first step", jobRepository)
                .<StudentBean, StudentBean>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    public List<StudentBean> readingContentsFromCsvFile() {

        ArrayList<StudentBean> studentBeans = null;
        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("Student_Details.csv");

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            List<String[]> allData = csvReader.readAll();

            studentBeans = new ArrayList<StudentBean>();
            // print Data
            for (String[] row : allData) {
                StudentBean studentBean = new StudentBean();
                studentBean.setName(row[0]);
                studentBean.setRollNo(row[1]);
                studentBean.setDepartment(row[2]);
                studentBean.setResult(row[3]);
                studentBean.setCgpa(Double.valueOf(row[4]));
                studentBean.setDistinction(row[5]);
                studentBeans.add(studentBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentBeans;
    }
}