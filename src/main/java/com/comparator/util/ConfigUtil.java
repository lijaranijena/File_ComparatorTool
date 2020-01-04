package com.comparator.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigUtil {

	@JsonProperty("response_path")
	private String responsePath;
	@JsonProperty("first_input_file")
	private String firstInputFile;
	@JsonProperty("second_inpit_file")
	private String secondInputFile;
	@JsonProperty("file_extension")
	private String fileExtension;

	public String getResponsePath() {
		return responsePath;
	}

	public void setResponsePath(String responsePath) {
		this.responsePath = responsePath;
	}

	public String getFirstInputFile() {
		return firstInputFile;
	}

	public void setFirstInputFile(String firstInputFile) {
		this.firstInputFile = firstInputFile;
	}

	public String getSecondInputFile() {
		return secondInputFile;
	}

	public void setSecondInputFile(String secondInputFile) {
		this.secondInputFile = secondInputFile;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

}
