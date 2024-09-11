package cn.xumob.mapper;

import cn.xumob.entity.Emp;

import java.util.List;

public interface EmpMapper {

    void insertEmp(Emp emp);

    List<Emp> listEmpWithDept();

    Emp getById(Integer id);
}
