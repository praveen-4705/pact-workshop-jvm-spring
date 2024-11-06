package com.pactworkshop.provider;

import java.util.Objects;

public class Product {

    private String uin;
    private String datatype;
    private String countryCode;
    private int applicationId;
    private String currencyCode;
    private String applicationRecordId;
    private String organizationId;
    private String version;

    public Product() {
    }

    public Product(String uin,
                   String datatype,
                   String countryCode,
                   int applicationId, String currencyCode, String applicationRecordId, String organizationId, String version) {
        this.uin = uin;
        this.datatype = datatype;
        this.countryCode = countryCode;
        this.applicationRecordId = applicationRecordId;
        this.applicationId = applicationId;
        this.currencyCode = currencyCode;
        this.organizationId = organizationId;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(uin, product.uin) &&
                Objects.equals(datatype, product.datatype) &&
                Objects.equals(countryCode, product.countryCode) &&
                Objects.equals(applicationId, product.applicationId) &&
                Objects.equals(currencyCode, product.currencyCode) &&
                Objects.equals(applicationRecordId, product.applicationRecordId) &&
                Objects.equals(organizationId, product.organizationId)  &&
                Objects.equals(version, product.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uin, datatype, countryCode, applicationId, currencyCode, applicationRecordId, organizationId, version);
    }

    @Override
    public String toString() {
        return "Product{" +
                "uin='" + uin + '\'' +
                ", datatype='" + datatype + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", applicationRecordId='" + applicationRecordId + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getApplicationRecordId() {
        return applicationRecordId;
    }

    public void setApplicationRecordId(String applicationRecordId) {
        this.applicationRecordId = applicationRecordId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
