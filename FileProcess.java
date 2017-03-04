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
		
		FileProcess fp = new FileProcess();			//实例化FileProcess
		
		//将文件a.txt读入
		String fileContent = fp.readToString("/Users/lidawei/Desktop/a.txt", "UTF-8");		
		//调用readToString方法，将a.txt中的内容存储到字符串fileContent中
		
		System.out.println(fileContent);			//输出fileConten到控制台
		
		
		
		//调用writeStringContentToFile方法，将从控制台输入的内容写入文件a.txt
		Scanner sc = new Scanner(System.in);
		fp.writeStringContentToFile("/Users/lidawei/Desktop/a.txt",sc.nextLine());
		
		
		
		//以空格作为分隔符分割字符串并放到字符串数组中，并将分割后的结果打印出来
		try {
			fp.readFile2ArrayList("/Users/lidawei/Desktop/a.txt");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
	}
	
	public String readToString(String filePath, String encoding){
		File file = new File(filePath);			//在所给路径filePath下创建一个新的文件f
		Long fileLength = file.length();		//用fileLength存储文件file的长度（字节数）
		byte[] fileContent = new byte[fileLength.intValue()];		//将文件的长度存储到byte数组中
		try{
			FileInputStream in = new FileInputStream(file);			//为file创建一个FileInputStream
			in.read(fileContent);									//read方法（从输入流中读取下一个字节的数据）
			in.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		try{
			return new String(fileContent, encoding);			//通过指定的Charest（编码格式）解码指定的byte子数组，从而返回一个新的字符串
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			System.err.println("本系统不支持文件格式：" + encoding);
			return null;
		}	
	}
	
	//将指定内容写入指定文件的方法
	public void writeStringContentToFile(String filePath, String fileContent){
		File f = new File(filePath);		//在所给路径filePath下创建一个新的文件f
		FileWriter fw;						
		try{
			fw = new FileWriter(f);			//为文件f创建一个FileWriter
			fw.write(fileContent);			//将指定内容写入文件f
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
			
		}
		
	}
	
	public ArrayList readFile2ArrayList(String filePath) throws IOException{
		ArrayList wordList = new ArrayList();			//创建一个ArrayList
		File inputfile = new File(filePath);			//在所给路径下创建一个新的文件inputfile
		FileReader fr = new FileReader(inputfile);		//为inputfile创建一个FileReader fr
		BufferedReader br = new BufferedReader(fr);		//为fr创建一个BufferedReader br
		String s;
		while((s = br.readLine()) != null){				//在当前文本行非空的情况下，将该行文本存入字符串s中
			String[] words = s.split(" ");				//将字符串s以空格作为分隔符分割，将得到的子字符串存入字符串数组words中
			for(int i=0;i<words.length;i++){			
				if(!wordList.contains(words[i])){		//如果wordList中不含有该字符串，则将其加入到worList中
					wordList.add(words[i]);
				}
			}
		}
		fr.close();
		for(int i=0;i<wordList.size();i++){
			System.out.println(wordList.get(i).toString());		//打印wordList中的内容
		}
		return wordList;
	
	}
	
	
	
	
	

}
