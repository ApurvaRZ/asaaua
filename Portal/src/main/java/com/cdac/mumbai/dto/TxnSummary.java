package com.cdac.mumbai.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class TxnSummary {
private String yearmonth;

private BigInteger txncount;
private String environment;
private String txnType;

}
