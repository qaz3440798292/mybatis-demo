import cn.xumob.entity.Dept;
import cn.xumob.entity.Emp;
import cn.xumob.mapper.EmpMapper;
import cn.xumob.service.EmpServiceImpl;
import cn.xumob.utils.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MybatisTest {

    @Test
    public void cacheTest() {
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession(true);
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp1 = mapper.getById(1);
        System.out.println(emp1);
        SqlSession sqlSession1 = SqlSessionFactoryUtil.openSqlSession(true);
        EmpMapper mapper1 = sqlSession1.getMapper(EmpMapper.class);
        Emp emp2 = mapper1.getById(1);
        System.out.println(emp2);
    }

    @Test
    public void selectEmpWithDeptTest2() {
        EmpServiceImpl empService = new EmpServiceImpl();
        Emp emp = empService.getById(1);
        System.out.println(emp);
    }

    @Test
    public void selectEmpWithDeptTest() {
        EmpServiceImpl empService = new EmpServiceImpl();
        List<Emp> emps = empService.listEmpWithDept();
        emps.forEach(System.out::println);
    }

    @Test
    public void insertEmpWithDeptTest() {

        Dept dept = new Dept();
        dept.setName("管理部");

        Emp emp = new Emp();
        emp.setName("李四");
        emp.setSalary(BigDecimal.valueOf(6000));
        emp.setGender(false);
        emp.setBirthday(LocalDate.parse("2024-02-01"));
        emp.setDept(dept);
        emp.setDeptId(1);

        EmpServiceImpl empService = new EmpServiceImpl();
        empService.insertEmp(emp);
    }
}
