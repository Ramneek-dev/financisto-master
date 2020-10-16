package ru.orangesoftware.financemanagementapp.report;

import java.util.List;
import java.util.Map;

import ru.orangesoftware.financemanagementapp.db.AbstractDbTest;
import ru.orangesoftware.financemanagementapp.filter.WhereFilter;
import ru.orangesoftware.financemanagementapp.graph.GraphUnit;
import ru.orangesoftware.financemanagementapp.model.Account;
import ru.orangesoftware.financemanagementapp.model.Category;
import ru.orangesoftware.financemanagementapp.model.Currency;
import ru.orangesoftware.financemanagementapp.test.AccountBuilder;
import ru.orangesoftware.financemanagementapp.test.CategoryBuilder;
import ru.orangesoftware.financemanagementapp.test.CurrencyBuilder;
import ru.orangesoftware.financemanagementapp.utils.CurrencyCache;

import static org.junit.Assert.*;

public abstract class AbstractReportTest extends AbstractDbTest {

    Currency c1;
    Currency c2;
    Account a1;
    Account a2;
    Account a3;
    Report report;
    Map<String, Category> categories;
    WhereFilter filter = WhereFilter.empty();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        c1 = CurrencyBuilder.withDb(db).name("USD").title("Dollar").symbol("$").makeDefault().create();
        c2 = CurrencyBuilder.withDb(db).name("SGD").title("Singapore Dollar").symbol("S$").create();
        a1 = AccountBuilder.createDefault(db, c1);
        a2 = AccountBuilder.createDefault(db, c1);
        a3 = AccountBuilder.createDefault(db, c2);
        categories = CategoryBuilder.createDefaultHierarchy(db);
        report = createReport();
        CurrencyCache.initialize(db);
    }

    protected abstract Report createReport();

    List<GraphUnit> assertReportReturnsData() {
        return assertReportReturnsData(IncomeExpense.BOTH);
    }

    List<GraphUnit> assertReportReturnsData(IncomeExpense incomeExpense) {
        report.setIncomeExpense(incomeExpense);
        ReportData data = report.getReport(db, filter);
        assertNotNull(data);
        List<GraphUnit> units = data.units;
        assertNotNull(units);
        return units;
    }

    void assertName(GraphUnit unit, String name) {
        assertEquals(name, unit.name);
    }

    void assertIncome(GraphUnit u, long amount) {
        assertEquals(amount, u.getIncomeExpense().income.longValue());
    }

    void assertExpense(GraphUnit u, long amount) {
        assertEquals(amount, u.getIncomeExpense().expense.longValue());
    }

}
