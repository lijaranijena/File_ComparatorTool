package com.comparator.tool;

import java.util.List;

import com.comparator.domain.Account;
import com.comparator.util.CommonUtils;
import com.comparator.util.ConfigUtil;


public class ToolApplication {

	public static void main(String[] args) throws Exception {
		
		ConfigUtil config = CommonUtils.getConfig();
		
		List<Account> poa_accountList = CommonUtils.getDataList(
				"E:\\LIJA'S OFFICE WORK\\tool\\tool\\src\\main\\resources\\templates\\POA.csv",
				Account.class);
		CommonUtils.triggerEndpoint(poa_accountList,config.getFirstInputFile(), config.getFileExtension());
		
		List<Account> pod_accountList =  CommonUtils.getDataList(
				"E:\\LIJA'S OFFICE WORK\\tool\\tool\\src\\main\\resources\\templates\\POA.csv",
				Account.class);
		CommonUtils.triggerEndpoint(pod_accountList,config.getSecondInputFile(), config.getFileExtension());
	}

}
