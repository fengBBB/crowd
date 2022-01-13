import com.feng.crowd.entity.Admin;
import com.feng.crowd.mapper.AdminMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml"})
public class test {
    @Autowired
    private DataSource dataSource;

    @Autowired
    AdminMapper adminMapper;

    @Test
    public void test1(){
        System.out.println(dataSource);
    }

    @Test
    public void test2(){
        Admin admin= new Admin(null,"feng","123123","feng","hello@hello.com",null);
        adminMapper.insert(admin);
    }
}
