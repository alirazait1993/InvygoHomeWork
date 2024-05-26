package org.automation.search;

import org.automation.Setup;
import org.automation.views.ProductSearch;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchTests extends Setup {

    String make = "Lamborghini";
    String model = "Urus";
    String type = "Suv";

    @Test(priority = 0)
    public void ViewAllByMonthly() {
        ProductSearch search = new ProductSearch(driver);
        Assert.assertTrue(search.viewAllMonthlyCars(), "Monthly cars page loaded successfully");
    }

    @Test(priority = 0)
    public void ViewAllByWeekly() {
        ProductSearch search = new ProductSearch(driver);
        Assert.assertTrue(search.viewAllWeeklyCars(), "Weekly cars page loaded successfully");
    }

    @Test(priority = 0)
    public void SearchByCarName() {
        ProductSearch search = new ProductSearch(driver);
        search.carSearchByName(make,model);
        Assert.assertEquals(search.carSearchVerify(make,model), make+" "+model);
        Assert.assertTrue(search.carSearchVerify(make,model).matches(make+" "+model), "Expected car found.");
    }

    @Test(priority = 0)
    public void SearchByFilters() {
        ProductSearch search = new ProductSearch(driver);
        search.carSearchByFilter(make, type);
        Assert.assertEquals(search.carSearchVerify(make,model), make+" "+model);
        Assert.assertTrue(search.carSearchVerify(make,model).matches(make+" "+model), "Expected car found.");
    }
}
