package com.pdp.taskmanagerapp.controller;

import com.pdp.taskmanagerapp.entity.Column;
import com.pdp.taskmanagerapp.repo.ColumnRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("columns")
@Controller
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnRepo columnRepo;



    @PostMapping("/add")
    public String add(@RequestParam String name) {
        int orderNumber = 1;
        Integer greatestOrderNumber = columnRepo.findGreatestOrderNumber();

        if (greatestOrderNumber != null) {
            orderNumber = greatestOrderNumber + 1;
        }

        columnRepo.save(Column.builder()
                .name(name)
                .orderNumber(orderNumber)
                .build());

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        columnRepo.deleteById(id);
        return  "redirect:/";
    }

    @GetMapping("changeF/{order}")
    public String changeF(@PathVariable Integer order) {
        Column currentCol = columnRepo.findByOrderNumber(order);
        Column nextCol = columnRepo.findByOrderNumber(order + 1);

        if (nextCol != null) {
            nextCol.setOrderNumber(order);
            columnRepo.save(nextCol);
        }

        currentCol.setOrderNumber(order + 1);
        columnRepo.save(currentCol);

        return "redirect:/";
    }

    @GetMapping("changeB/{order}")
    public String changeB(@PathVariable Integer order) {
        Column currentCol = columnRepo.findByOrderNumber(order);
        Column prevCol = columnRepo.findByOrderNumber(order - 1);

        if (prevCol != null) {
            prevCol.setOrderNumber(order);
            columnRepo.save(prevCol);
        }

        currentCol.setOrderNumber(order - 1);
        columnRepo.save(currentCol);

        return "redirect:/";
    }


}
