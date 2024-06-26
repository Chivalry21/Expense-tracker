package com.chivalrycode.expensetracker.service;

import com.chivalrycode.expensetracker.asset.SaveCSV;
import com.chivalrycode.expensetracker.dto.ExpenseRequestDto;
import com.chivalrycode.expensetracker.dto.ExpenseResponseDto;
import com.chivalrycode.expensetracker.dto.ReportResponseDto;
import com.chivalrycode.expensetracker.exception.BadRequestException;
import com.chivalrycode.expensetracker.exception.CategoryNotFoundException;
import com.chivalrycode.expensetracker.mapper.ExpenseMapper;
import com.chivalrycode.expensetracker.model.Category;
import com.chivalrycode.expensetracker.model.Expense;
import com.chivalrycode.expensetracker.model.User;
import com.chivalrycode.expensetracker.repositories.CategoryRepository;
import com.chivalrycode.expensetracker.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService{
    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final SaveCSV saveCSV;

    @Override
    public List<ExpenseResponseDto> getAllExpense() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        List<Expense> expenses = expenseRepository.findAllByUser(user);
        return expenses.stream().map(expenseMapper::toExpenseResponseDto).collect(Collectors.toList());
    }
    @Override
    public ExpenseResponseDto getExpenseById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        Optional<Expense>expense = expenseRepository.findByIdAndUser(id, user);
        return expense.map(expenseMapper::toExpenseResponseDto).orElse(null);
    }
    @Override
    public ExpenseResponseDto saveExpense(ExpenseRequestDto exp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        Expense expense = expenseMapper.toExpense(exp);
        Optional<Category>category = categoryRepository.findById(exp.getCategoryId());
        expense.setCategory(category.get());
        expense.setUser(user);
        Expense expense1 = expenseRepository.save(expense);
        return expenseMapper.toExpenseResponseDto(expense1);
    }
    @Override
    public void deleteExpense(Long id) {
        expenseRepository.findById(id) ;
    }
    @Override
    public ExpenseResponseDto updateExpense(ExpenseRequestDto expense) {
        return expenseMapper.toExpenseResponseDto(expenseRepository.save(expenseMapper.toExpense(expense)));
    }

    @Override
    public List<ExpenseResponseDto> getByCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty())
            throw new CategoryNotFoundException("Category not found");
        return expenseRepository.findByCategory(category.get()).stream().map(expenseMapper::toExpenseResponseDto).collect(Collectors.toList());

    }
    @Override
    public ReportResponseDto generateReport(LocalDate startDt, LocalDate endDt, Long categoryId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        if(startDt == null && endDt == null && categoryId == null){
            throw new BadRequestException("you must provide at least a category or start Date");
        }
        Optional<Category> category1 = Optional.empty();
        if(categoryId!= null)
            category1 = categoryRepository.findById(categoryId);
        List<Expense> expenses = new ArrayList<>();
        String filename = "";
        LocalDate localDate = LocalDate.now();
        if(startDt == null && endDt == null){
            expenses = expenseRepository.findByUserAndCategory(user, category1.get());
            filename = "./src/main/resources/report_generated_for_"+ category1.get().getName()+"_generated_on_" + localDate+".csv";
        }
        if(startDt!= null ){
            if(endDt!= null){
                if(categoryId!= null){
                    expenses = expenseRepository.findByDateRangeAndCategory(startDt,endDt,categoryId, user.getId());
                    filename = "./src/main/resources/report_generated_for_"+ category1.get().getName()+"_from_"+startDt+"_to_"+endDt+"_generated_on_" + localDate+".csv";
                }else {
                    expenses = expenseRepository.findByDateRange(startDt,endDt, user.getId());
                    filename = "./src/main/resources/report_generated_for_all_categories_from_"+startDt+"_to_"+endDt+"_generated_on_" + localDate+".csv";
                }
            }else {
                if(categoryId!= null){
                    LocalDate now = LocalDate.now();
                    expenses = expenseRepository.findByDateRangeAndCategory(startDt,now,categoryId, user.getId());
                    filename = "./src/main/resources/report_generated_for_"+ category1.get().getName()+"_from_"+startDt+"_to_"+now+"_generated_on_" + localDate+".csv";
                }else {
                    LocalDate now = LocalDate.now();
                    expenses = expenseRepository.findByDateRange(startDt,now,user.getId());
                    filename = "./src/main/resources/report_generated_for_all_categories"+"_from_"+startDt+"_to_"+now+"_generated_on_" + localDate+".csv";
                }
            }
        }
        StringBuilder content = new StringBuilder();
        content.append("id,item name,category,amount,description\n");
        expenses.forEach(expense -> {
            content.append(expense.getId()).append(",").append(expense.getItemName()).append(",").append(expense.getCategory().getName()).append(",").append(expense.getAmount()).append(",").append(expense.getDescription()).append("\n");
        });
        saveCSV.createCSVFile(filename,content.toString());
        return new ReportResponseDto("Report generated successfully", filename);
    }
}
