package cn.xumob.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emp implements Serializable {

    private Integer id;

    private String name;

    private BigDecimal salary;

    private Boolean gender;

    private LocalDate birthday;

    private Integer deptId;

    private Dept dept;
}
