package com.pdp.taskmanagerapp.repo;

import com.pdp.taskmanagerapp.entity.Column;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ColumnRepoTest {

    @Autowired
    private ColumnRepo columnRepo;

    @BeforeEach
    void setUp() {
       // this.columnRepo = Mockito.mock(ColumnRepo.class);

    }
    @Test
    void findAllByOrderNumber() {
        // Arrange
        Column column1 = Column.builder()
                .name("Column 1")
                .orderNumber(1)
                .build();
        columnRepo.save(column1);

        Column column2 = Column.builder()
                .name("Column 2")
                .orderNumber(2)
                .build();
        columnRepo.save(column2);

        // Act
        List<Column> columns = columnRepo.findAllByOrderNumber();

        // Assert
        assertEquals(2, columns.size());
        assertEquals(1, columns.get(0).getOrderNumber());
        assertEquals(2, columns.get(1).getOrderNumber());
    }

    @Test
    void findByOrderNumber() {
        // Arrange
        Column column = Column.builder()
                .name("Column 1")
                .orderNumber(1)
                .build();
        columnRepo.save(column);


        Column foundColumn = columnRepo.findByOrderNumber(1);

        assertNotNull(foundColumn);
        assertEquals(1, foundColumn.getOrderNumber());
    }

    @Test
    void findGreatestOrderNumber() {
        // Arrange
        Column column1 = Column.builder()
                .name("Column 1")
                .orderNumber(1)
                .build();
        columnRepo.save(column1);

        Column column2 = Column.builder()
                .name("Column 2")
                .orderNumber(3)
                .build();
        columnRepo.save(column2);

        // Act
        Integer greatestOrderNumber = columnRepo.findGreatestOrderNumber();

        // Assert
        assertEquals(3, greatestOrderNumber);
    }

    @Test
    void findSmallestOrderNumber() {
        // Arrange
        Column column1 = Column.builder()
                .name("Column 1")
                .orderNumber(1)
                .build();
        columnRepo.save(column1);

        Column column2 = Column.builder()
                .name("Column 2")
                .orderNumber(3)
                .build();
        columnRepo.save(column2);

        // Act
        Integer smallestOrderNumber = columnRepo.findSmallestOrderNumber();

        // Assert
        assertEquals(1, smallestOrderNumber);
    }
}
