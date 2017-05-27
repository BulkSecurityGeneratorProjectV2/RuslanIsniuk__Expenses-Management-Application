package com.intelliarts.test_app.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "expenses_id", unique = true, nullable = false)
    private int expenseID;

    @Column(name = "expenses_date", nullable = false)
    private Date expenseDate;

    @Column(name = "expenses_amount", nullable = false)
    private BigDecimal expenseAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "expenses_currency", nullable = false)
    private CurrencyType expenseCurrency;

    @Column(name = "expenses_description", nullable = false)
    private String expenseDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        if (expenseID != expense.expenseID) return false;
        if (expenseDate != null ? !expenseDate.equals(expense.expenseDate) : expense.expenseDate != null) return false;
        if (expenseAmount != null ? !expenseAmount.equals(expense.expenseAmount) : expense.expenseAmount != null)
            return false;
        if (expenseCurrency != expense.expenseCurrency) return false;
        return expenseDescription != null ? expenseDescription.equals(expense.expenseDescription) : expense.expenseDescription == null;
    }

    @Override
    public int hashCode() {
        int result = expenseID;
        result = 31 * result + (expenseDate != null ? expenseDate.hashCode() : 0);
        result = 31 * result + (expenseAmount != null ? expenseAmount.hashCode() : 0);
        result = 31 * result + (expenseCurrency != null ? expenseCurrency.hashCode() : 0);
        result = 31 * result + (expenseDescription != null ? expenseDescription.hashCode() : 0);
        return result;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setExpenseDateUsingStr(String expenseDateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {

            java.util.Date utilDate = formatter.parse(expenseDateStr);
            java.sql.Date mySQLDate = new java.sql.Date(utilDate.getTime());

            this.expenseDate = mySQLDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public CurrencyType getExpenseCurrency() {
        return expenseCurrency;
    }

    public void setExpenseCurrency(CurrencyType expenseCurrency) {
        this.expenseCurrency = expenseCurrency;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }
}