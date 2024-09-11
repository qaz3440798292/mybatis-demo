package cn.xumob.service;

import cn.xumob.entity.Emp;
import cn.xumob.mapper.DeptMapper;
import cn.xumob.mapper.EmpMapper;
import cn.xumob.utils.MapperFactory;

import java.util.List;

public class EmpServiceImpl {
    private final EmpMapper empMapper = MapperFactory.getMapper(EmpMapper.class);
    private final DeptMapper deptMapper = MapperFactory.getMapper(DeptMapper.class);

    public void insertEmp(Emp emp) {
        deptMapper.insertDept(emp.getDept());
        empMapper.insertEmp(emp);
    }

    public List<Emp> listEmpWithDept() {
        EmpMapper empMapper = MapperFactory.getMapper(EmpMapper.class);
        return empMapper.listEmpWithDept();
    }

    public Emp getById(Integer id) {
        return empMapper.getById(id);
    }
}
