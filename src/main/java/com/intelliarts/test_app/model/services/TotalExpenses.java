package com.intelliarts.test_app.model.services;

import com.google.gson.Gson;
import com.intelliarts.test_app.dao.DateDAO;
import com.intelliarts.test_app.dao.impl.HibernateDateDAO;
import com.intelliarts.test_app.entity.CurrencyType;
import com.intelliarts.test_app.entity.Date;
import com.intelliarts.test_app.entity.ExchangeRates;
import com.intelliarts.test_app.entity.Expense;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;

public class TotalExpenses {
    private static final String FIXER_URL = "http://api.fixer.io/latest?base=";
    private static final Logger logger = Logger.getLogger(HibernateDateDAO.class);

    private DateDAO dateDAO = new HibernateDateDAO();
    private ExchangeRates exchangeRates;
    private BigDecimal totalAmount = new BigDecimal("0.00");

    public void execute(String currencyTypeStr) {
        exchangeRates = getExchangeRates(currencyTypeStr);
        List<Date> dateList = dateDAO.readAll();

        for (Date dateFromList : dateList) {
            totalAmount = totalAmount.add(getTotalAmountPerDay(dateFromList));
        }
        System.out.println(totalAmount + " " + currencyTypeStr);
    }

    private ExchangeRates getExchangeRates(String currencyTypeStr) {
        ExchangeRates exchangeRates = new ExchangeRates();
        Gson gson = new Gson();
        String URL = FIXER_URL + currencyTypeStr;

        try {
            String json = readUrl(URL);
            exchangeRates = gson.fromJson(json, exchangeRates.getClass());
        } catch (Exception e) {
            logger.error(e);
        }
        return exchangeRates;
    }

    private BigDecimal getTotalAmountPerDay(Date date) {
        BigDecimal amountPerDay = new BigDecimal("0.00");

        for (Expense expenseFromSet : date.getExpenseSet()) {
            amountPerDay = amountPerDay.add(convertToCurrentCurrency(expenseFromSet.getExpenseAmount(), expenseFromSet.getExpenseCurrency()));
        }
        return amountPerDay;
    }

    private BigDecimal convertToCurrentCurrency(BigDecimal amount, CurrencyType currencyType) {
        if (exchangeRates.getBase().equals(currencyType.getAbbreviation())) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        } else {
            BigDecimal currentRate = new BigDecimal(exchangeRates.getRates().get(currencyType.getAbbreviation()));
            BigDecimal result = (new BigDecimal("1.00").divide(currentRate, RoundingMode.HALF_UP)).multiply(amount);
            result = result.setScale(2, RoundingMode.HALF_UP);
            return result;
        }
    }

    private String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (reader != null)
                reader.close();
        }
        return "";
    }
}