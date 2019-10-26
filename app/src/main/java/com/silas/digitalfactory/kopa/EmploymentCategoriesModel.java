package com.silas.digitalfactory.kopa;

public class EmploymentCategoriesModel {
    String EmploymentCategoryId,CompanyId,CategoryDescription;

    public EmploymentCategoriesModel(String EmploymentCategoryId,String CompanyId,String CategoryDescription) {
        this.EmploymentCategoryId = EmploymentCategoryId;
        this.CompanyId = CompanyId;
        this.CategoryDescription = CategoryDescription;
    }

    public String getEmploymentCategoryId() {
        return EmploymentCategoryId;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public String getCategoryDescription() {
        return CategoryDescription;
    }
}
