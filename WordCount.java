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
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;  
  
public class WordCount {  
    public static void main(String[] args) {  
    	
    	WordCount wordCount = new WordCount();
    	
        //用Hashtable存放<单词:词频>的映射关系  
        Hashtable<String, Integer> hashtable=new Hashtable<String, Integer>();  
        try {  
            /**
             * 将目标文件夹下的所有文件都写入b.txt中
             */ 
        	
        	String filePath = "/Users/lidawei/Desktop/腾讯教育新闻2015POS";
    		File file = new File(filePath);
    		File[] f = file.listFiles();
    		for(int i=1;i<f.length;i++){
    			String fileContent = wordCount.readToString(f[i], "UTF-8");
    			File f1 = new File("/Users/lidawei/Desktop/b.txt");
    			try {
    				FileWriter fileWriter = new FileWriter(f1,true);
    				fileWriter.write(fileContent);
    				fileWriter.close();
    			} catch (IOException e) {
    				// TODO 自动生成的 catch 块
    				e.printStackTrace();
    			}
    		}
        	
    		//读取要处理的b.txt
    		BufferedReader br = new BufferedReader(new FileReader(new File("/Users/lidawei/Desktop/b.txt")));
            
    		String string;  
    		String[] words = null;
            while((string = br.readLine())!=null){
            	words = string.split(" ");			//分词
            	
            	//构造哈希表（完成统计）
            	for(int i=0;i<words.length;i++){
            		if(!hashtable.containsKey(words[i])){
            			hashtable.put(words[i], new Integer(1));
            		}
            		else{
            			hashtable.put(words[i], hashtable.get(words[i]).intValue()+1);
            		}
            	}
            }
            
            
            //将map.entrySet()转换成list  
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(hashtable.entrySet());  
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {  
                //降序排序  
                public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {               	
                    
                	//升序：return o1.getValue().compareTo(o2.getValue());  
                    return o2.getValue().compareTo(o1.getValue());  
                }				
            });  
            
            //输出结果
            for (Map.Entry<String, Integer> mapping : list) {  
                System.out.println(mapping.getKey() + ":\t" + mapping.getValue());  
            }       
            
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        
    }
    
    
    //readToString方法
    public String readToString(File file, String encoding){
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
			return null;
		}	
	}
    
}  