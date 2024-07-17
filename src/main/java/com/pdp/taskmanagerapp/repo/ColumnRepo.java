package com.pdp.taskmanagerapp.repo;

import com.pdp.taskmanagerapp.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColumnRepo extends JpaRepository<Column, Integer> {
    @Query(value = "SELECT * FROM columns ORDER BY order_number ", nativeQuery = true)
    List<Column> findAllByOrderNumber();

    Column findByOrderNumber(Integer orderNumber);

    @Query(nativeQuery = true,value = """

            select  columns.order_number from columns
            order by columns.order_number desc
            LIMIT 1
            """)
    Integer findGreatestOrderNumber();

    @Query(nativeQuery = true,value = """

            select  columns.order_number from columns
            order by columns.order_number
            LIMIT 1
           """)
    Integer findSmallestOrderNumber();
}
