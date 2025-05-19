package com.HopeConnect.HC.models.Donation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hopeconnect.fees")
public class FeeConfig {
    private double transactionFeePercentage;
    private double minimumFee;
    private double maximumFee;

    // Getters and setters
    public double getTransactionFeePercentage() {
        return transactionFeePercentage;
    }

    public void setTransactionFeePercentage(double transactionFeePercentage) {
        this.transactionFeePercentage = transactionFeePercentage;
    }

    public double getMinimumFee() {
        return minimumFee;
    }

    public void setMinimumFee(double minimumFee) {
        this.minimumFee = minimumFee;
    }

    public double getMaximumFee() {
        return maximumFee;
    }

    public void setMaximumFee(double maximumFee) {
        this.maximumFee = maximumFee;
    }
}