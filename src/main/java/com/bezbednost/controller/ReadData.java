package com.bezbednost.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadData {
	
	public static ArrayList<String> files = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		getDirectoryData();
	}
	
	public static void getDirectoryData() throws IOException {
		
		System.out.println("Input the path of directory: ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String path = reader.readLine();
		
		File[] directory = new File(path).listFiles();

		for (File file: directory) {
		  if (file.isFile()) {
			  files.add(file.getName());
			  //primer jednog Path-a za uneti(u nasem projektu se nalazi): ./data
		  } 
		}
	}

}
