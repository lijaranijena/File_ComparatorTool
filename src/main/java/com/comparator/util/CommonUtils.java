package com.comparator.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.comparator.annotation.Index;
import com.comparator.domain.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class CommonUtils {
	
	private static ConfigUtil config;
	
	static {
		try {
			ObjectMapper objectMapper=new ObjectMapper();
			InputStream inputStream=CommonUtils.class.getResourceAsStream("/configuration.json");
			//byte[] readAllBytes = Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("./configuration.json").toURI()));
			config = objectMapper.readValue(inputStream, ConfigUtil.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// T type parameter
	public static <T> List<T> getDataList(String path, Class<T> clazz)
			throws Exception {

		List<T> dataList = new ArrayList<T>();

		// URI absolutePathUri =
		// ClassLoader.getSystemResource(relativePath).toURI();
		Workbook workbook = WorkbookFactory.create(new File(path));
		Sheet sheet = workbook.getSheetAt(0);

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		// skip heading 0
		for (int i = 1; i <= rowCount; i++) {
			T t = clazz.newInstance();
			Row row = sheet.getRow(i);
			Field[] declaredFields = t.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				if (field.isAnnotationPresent(Index.class)) {
					int index = field.getAnnotation(Index.class).value();
					field.setAccessible(true);
					Cell cell = row.getCell(index);
					cell.setCellType(CellType.STRING);
					field.set(t, cell.getStringCellValue());
				}
			}
			dataList.add(t);
		}

		return dataList;
	}

	private static String encrypt(String value) {
		return new String(Base64.getEncoder().encode(value.getBytes()));

	}

	public static void trigger(Account account, String fileName) {
		try {
			String url = (account.getEndpoint() + encrypt(account.getAccNo()) + account
					.getResource()).trim();
			HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
			
			writeToFile(config.getResponsePath(), fileName, jsonResponse.getBody().toString().getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void triggerEndpoint(List<Account> accountList,String prefix, String suffix )
			throws Exception {
		accountList.parallelStream().forEach(acc-> trigger(acc, getFileName(acc.getAccNo(), prefix, suffix)));
		
	}

	private static String getFileName(String accountNumber, String prefix, String suffix){
		return (accountNumber + suffix).replaceFirst(accountNumber.substring(0, 2), prefix);
		
	}

	public static void writeToFile(String responsePath,String fileName, byte[] content) throws IOException{
		Files.createDirectories(Paths.get(responsePath));
		Path path = Paths.get(responsePath+"\\"+fileName);
	    Files.write(path, content);
	}
	
	public static ConfigUtil getConfig() throws Exception{	
		return config;
	} 
}
