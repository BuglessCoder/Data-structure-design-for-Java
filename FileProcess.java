package sjjgzhsj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileProcess {

	public static void main(String[] args){
		// TODO 自动生成的方法存根
		
		FileProcess fp = new FileProcess();
		
		//将文件a.txt读入
		String fileContent = fp.readToString("/Users/lidawei/Desktop/a.txt", "UTF-8");
		System.out.println(fileContent);
		
		//写入文件a.txt
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		fp.writeStringContentToFile("/Users/lidawei/Desktop/a.txt",str);
		
		//以空格作为分隔符分割字符串并放到字符串数组中
		try {
			fp.readFile2ArrayList("/Users/lidawei/Desktop/a.txt");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	public String readToString(String filePath, String encoding){
		File file = new File(filePath);
		Long fileLength = file.length();
		byte[] fileContent = new byte[fileLength.intValue()];
		try{
			FileInputStream in = new FileInputStream(file);
			in.read(fileContent);
			in.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		try{
			return new String(fileContent, encoding);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			System.err.println("本系统不支持文件格式：" + encoding);
			return null;
		}	
	}
	
	public void writeStringContentToFile(String filePath, String fileContent){
		File f = new File(filePath);
		FileWriter fw;
		try{
			fw = new FileWriter(f);
			fw.write(fileContent);
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
			
		}
		
	}
	
	public ArrayList readFile2ArrayList(String filePath) throws IOException{
		ArrayList wordList = new ArrayList();
		File inputfile = new File(filePath);
		FileReader fr = new FileReader(inputfile);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while((s = br.readLine()) != null){
			String[] words = s.split(" ");
			for(int i=0;i<words.length;i++){
				if(!wordList.contains(words[i])){
					wordList.add(words[i]);
				}
			}
		}
		fr.close();
		for(int i=0;i<wordList.size();i++){
			System.out.println(wordList.get(i).toString());
		}
		return wordList;
		
	}
	
	

}
