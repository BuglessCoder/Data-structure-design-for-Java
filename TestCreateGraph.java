package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TestCreateGraph extends Application {

	//两个文本框，分别用来输入词和显示生成的Json文件的路径
	private TextField t1 = new TextField();
	private TextField t2 = new TextField();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO 自动生成的方法存根
		
		//面板整体使用GridPane
		GridPane g = new GridPane();
		g.add(new Label("Please input a word:"),0,0);
		g.add(t1,1,0);
	    g.add(new Label("The path of the HTML file is :    "),0,1);
	    g.add(t2,1,1);
	    
	    Button b1 = new Button("Get the HTML file");
	    b1.setOnAction(e -> {
			 String word = t1.getText();
			 jsonFile(word);		//调用jsonFile函数	 
			 t2.setText("/Users/lidawei/Downloads/template2.json"); 	                               
		 });
		 g.add(b1,1,2);	
		 
		 Scene scene = new Scene(g,400,80);
	     primaryStage.setScene(scene);
	     primaryStage.setTitle("Get the Net");
	     primaryStage.show();		 
	}
	
	//Main方法
	public static void main(String[] args){
        Application.launch(args);
      }
	
	//jsonFile函数
	public void jsonFile(String word){		
		TestCreateGraph cGraph = new TestCreateGraph();		//实例化
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/Users/lidawei/Desktop/a.txt"));		//读取该词典文件
			String[][] strings = new String[66190][2];		//创建一个二维字符串数组用来存储词和对应的义原
			int count = 0;									//用计数器count代表文本的行数，也是二维数组的行数
			
			//开始读入
			while ((line = reader.readLine())!= null) {				
				line = line.trim().replaceAll("\\s+", " ");	//所有空白段均用空格代替		
				strings[count][0] = line.split(" ")[0];		//放入每行的词
				strings[count][1] = line.split(" ")[2];		//放入该词的所有义原（用逗号相连，看做一个字符串）		
				count++;							
			}
			reader.close();					
			
			String string = "";		//初始化字符串string，拟用来存储义原
			
			//遍历二维数组的第一列，如果有词与用户输入的词相等，则把它的义原（多个义原已经用逗号连在一起了）放入字符串string中
			for(int i=0;i<strings.length;i++){
				if(strings[i][0] != null){
					if(strings[i][0].equals(word)){
						string = strings[i][1];		//string里面存放的就是用逗号连在一起的多个义原
						break;
					}
				}
				
			}
			
			String[] primitive = string.split(",");		//将找到的string按逗号分隔成多个义原并放入primitive数组
			String[] jsonfile = new String[30000];		//利用字符串数组jsonfile来生成json文件
			jsonfile[0] = "{  \"nodes\":[";				//写入第一行
			
			//把这几个义原先放入（group为0）
			for(int i=1;i<=primitive.length;i++){
				jsonfile[i] = "{\"name\":\"" + primitive[i-1] + "\",\"group\":0},";
			}
			
			int cnt = primitive.length+1;						//cnt表示当前jsonfile数组的行号
			
			
			/**
			 * 以下采用将义原按照个数分类的方法（个数从1到5）
			 * 在这里只对义原为1和2的算法做注释，其余同理
			 */
			
			//一个义原
			if(primitive.length == 1){			
				//遍历每个义原，找含有该义原（因为此时只有一个义原）的词，然后把它写入jsonfile
				for(int i=0;i<primitive.length;i++){							
					for(int j=0;j<strings.length;j++){
						if(strings[j][0] != null){
							if(strings[j][1].contains(primitive[i])){
								jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
								cnt++;
							}
						}
					}
				}
				
				int len = cnt-1;		//len表示含有该义原的词的个数
				
				//json文件的格式处理
				jsonfile[cnt-1] = jsonfile[cnt-1].substring(0, jsonfile[cnt-1].length()-1);
				jsonfile[cnt-1]+="],";
				jsonfile[cnt] = "\"links\":[";
				
				//将这些词（source为0）写入jsonfile
				for(int i=0;i<len;i++){
					cnt++;
					jsonfile[cnt] = "{\"source\":0,\"target\":"  + i +",\"value\":1},";
				}
			}
			
			//两个义原
			else if(primitive.length == 2){
				SeqList<Integer> list = new SeqList<Integer>();		//list用来存储每个义原对应词的最后一个词的位置
				
				SeqList<Integer> list1 = new SeqList<Integer>();	//list1用来存储与多个义原都有联系的词的位置	
				
				for(int i=0;i<primitive.length;i++){				//遍历每个义原，找词									
					//判断是第几个义原
					if(i == 0){						
						//遍历每个义原，找含有该义原（因为此时只有一个义原）的词，然后把它写入jsonfile
						for(int j=0;j<strings.length;j++){
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){
									jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
									cnt++;
								}
							}
						}
					}				
					else{
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int flag = 0;		//flag用来判断某个词是否与多个义原都相关
									for(int k=0;k<jsonfile.length;k++){								
										//判断：如果数组中之前已经放入过某个词，则记下这个词在数组中的位置（它就是与多个义原都相关的词）否则，插入这个词到list1中																						
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												flag = 1;
												break;
											}	
										}
									}
									if(flag == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}
								}
							}
						}				
					}
					list.insert(cnt-1);		//存储每个义原对应词的最后一个词的位置				
				}			
				
				//json文件的格式处理
				jsonfile[cnt-1] = jsonfile[cnt-1].substring(0, jsonfile[cnt-1].length()-1);
				jsonfile[cnt-1]+="],";
				jsonfile[cnt] = "\"links\":[";
				
				int flag = 0;	//此处的flag用来计数
				for(int j=0;j<primitive.length;j++){
					int len = list.get(j)-primitive.length;
					int i;
					
					//在含有第一个义原的词里找，如果这些词当中有含有第二个义原的，则把它以source=1写入
					for(i=flag;i<len;i++){
						for(int h=0;h<list1.size();h++){
							if(i == list1.get(h)){
								cnt++;
								jsonfile[cnt] = "{\"source\":1,\"target\":"  + (i+2) +",\"value\":1},";
							}
						}
						
						//不管怎样，所有词都要以source=0写入
						cnt++;
						jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+2) +",\"value\":1},";
					}
					flag = i;			
				}
			}
			
			//三个义原
			if(primitive.length == 3){
				SeqList<Integer> list = new SeqList<Integer>();	
				
				SeqList<Integer> list1 = new SeqList<Integer>();	
				SeqList<Integer> list2 = new SeqList<Integer>();
										
				int flag0 = 0;
				int flag1 = 0;
				
				for(int i=0;i<primitive.length;i++){												
					if(i == 0){
						for(int j=0;j<strings.length;j++){
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){
									jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
									cnt++;
								}
							}
						}	
						flag0 = cnt;
					}				
					else if(i == 1){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int flag = 0;
									for(int k=0;k<jsonfile.length;k++){																					
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												flag = 1;
												break;
											}	
										}
									}
									if(flag == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}
								}
							}
						}
						flag1 = cnt;
					}
					else if(i == 2){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int f1 = 0;
									int f2 = 0;
									for(int k=0;k<flag0;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												f1 = 1;
												break;
											}	
										}
									}
									for(int k=flag0;k<flag1;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list2.insert(k);
												f2 = 1;
												break;
											}	
										}
									}									
									if(f1 == 0 && f2 == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}								
								}
							}
						}
					}					
					list.insert(cnt-1);					
				}			
				
				jsonfile[cnt-1] = jsonfile[cnt-1].substring(0, jsonfile[cnt-1].length()-1);
				jsonfile[cnt-1]+="],";
				jsonfile[cnt] = "\"links\":[";
				
				for(int j=0;j<primitive.length;j++){
					int len0 = list.get(0)-primitive.length;
					int len1 = list.get(1)-primitive.length;
					
					int i=0;
					if(j == 0){						
						for(i=0;i<len0;i++){
							for(int h=0;h<list1.size();h++){
								if(i == list1.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":1,\"target\":"  + (i+3) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+3) +",\"value\":1},";
						}
					}
					else if(j == 1){
						for(i=len0;i<len1;i++){
							for(int h=0;h<list2.size();h++){
								if(i == list2.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":2,\"target\":"  + (i+3) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+3) +",\"value\":1},";
						}						
					}						
					
				}		
			}
			
			//四个义原
			else if(primitive.length == 4){
				SeqList<Integer> list = new SeqList<Integer>();	
				
				SeqList<Integer> list1 = new SeqList<Integer>();	
				SeqList<Integer> list2 = new SeqList<Integer>();
				SeqList<Integer> list3 = new SeqList<Integer>();
										
				int flag0 = 0;
				int flag1 = 0;
				int flag2 = 0;
				
				for(int i=0;i<primitive.length;i++){												
					if(i == 0){
						for(int j=0;j<strings.length;j++){
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){
									jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
									cnt++;
								}
							}
						}	
						flag0 = cnt;
					}				
					
					else if(i == 1){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int flag = 0;
									for(int k=0;k<jsonfile.length;k++){																									
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												flag = 1;
												break;
											}	
										}
									}
									if(flag == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}
								}
							}
						}
						flag1 = cnt;
					}
					
					else if(i == 2){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int f1 = 0;
									int f2 = 0;
									for(int k=0;k<flag0;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												f1 = 1;
												break;
											}	
										}
									}
									for(int k=flag0;k<flag1;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list2.insert(k);
												f2 = 1;
												break;
											}	
										}
									}									
									if(f1 == 0 && f2 == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}								
								}
							}
						}
						flag2 = cnt;
					}
					
					else if(i == 3){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int f1 = 0;
									int f2 = 0;
									int f3 = 0;
									for(int k=0;k<flag0;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												f1 = 1;
												break;
											}	
										}
									}
									for(int k=flag0;k<flag1;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list2.insert(k);
												f2 = 1;
												break;
											}	
										}
									}
									for(int k=flag1;k<flag2;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list3.insert(k);
												f3 = 1;
												break;
											}	
										}
									}
									if(f1 == 0 && f2 == 0 && f3 == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}								
								}
							}
						}
					}
					list.insert(cnt-1);						
				}
				
				jsonfile[cnt-1] = jsonfile[cnt-1].substring(0, jsonfile[cnt-1].length()-1);
				jsonfile[cnt-1]+="],";
				jsonfile[cnt] = "\"links\":[";
				
				for(int j=0;j<primitive.length;j++){
					int len0 = list.get(0)-primitive.length;
					int len1 = list.get(1)-primitive.length;
					int len2 = list.get(2)-primitive.length;
					
					int i=0;
					if(j == 0){						
						for(i=0;i<len0;i++){
							for(int h=0;h<list1.size();h++){
								if(i == list1.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":1,\"target\":"  + (i+4) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+4) +",\"value\":1},";
						}
					}
					else if(j == 1){
						for(i=len0;i<len1;i++){
							for(int h=0;h<list2.size();h++){
								if(i == list2.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":2,\"target\":"  + (i+4) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+4) +",\"value\":1},";
						}						
					}
					else if(j == 2){
						for(i=len1;i<len2;i++){
							for(int h=0;h<list3.size();h++){
								if(i == list3.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":3,\"target\":"  + (i+4) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+4) +",\"value\":1},";
						}						
					}					
				}		
			}
			
			//五个义原
			else if(primitive.length == 5){
				SeqList<Integer> list = new SeqList<Integer>();	
				
				SeqList<Integer> list1 = new SeqList<Integer>();	
				SeqList<Integer> list2 = new SeqList<Integer>();
				SeqList<Integer> list3 = new SeqList<Integer>();
				SeqList<Integer> list4 = new SeqList<Integer>();
										
				int flag0 = 0;
				int flag1 = 0;
				int flag2 = 0;
				int flag3 = 0;
								
				for(int i=0;i<primitive.length;i++){																	
					if(i == 0){
						for(int j=0;j<strings.length;j++){
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){
									jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
									cnt++;
								}
							}
						}	
						flag0 = cnt;
					}				
					
					else if(i == 1){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int flag = 0;
									for(int k=0;k<jsonfile.length;k++){																														
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												flag = 1;
												break;
											}	
										}
									}
									if(flag == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}
								}
							}
						}
						flag1 = cnt;
					}
					
					else if(i == 2){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int f1 = 0;
									int f2 = 0;
									for(int k=0;k<flag0;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												f1 = 1;
												break;
											}	
										}
									}
									for(int k=flag0;k<flag1;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list2.insert(k);
												f2 = 1;
												break;
											}	
										}
									}									
									if(f1 == 0 && f2 == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}								
								}
							}
						}
						flag2 = cnt;
					}
					
					else if(i == 3){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int f1 = 0;
									int f2 = 0;
									int f3 = 0;
									for(int k=0;k<flag0;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												f1 = 1;
												break;
											}	
										}
									}
									for(int k=flag0;k<flag1;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list2.insert(k);
												f2 = 1;
												break;
											}	
										}
									}
									for(int k=flag1;k<flag2;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list3.insert(k);
												f3 = 1;
												break;
											}	
										}
									}
									if(f1 == 0 && f2 == 0 && f3 == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}								
								}
							}
						}
						flag3 = cnt;
					}
					
					else if(i == 4){
						for(int j=0;j<strings.length;j++){				
							if(strings[j][0] != null){
								if(strings[j][1].contains(primitive[i])){	
									int f1 = 0;
									int f2 = 0;
									int f3 = 0;
									int f4 = 0;
									for(int k=0;k<flag0;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list1.insert(k);
												f1 = 1;
												break;
											}	
										}
									}
									for(int k=flag0;k<flag1;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list2.insert(k);
												f2 = 1;
												break;
											}	
										}
									}
									for(int k=flag1;k<flag2;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list3.insert(k);
												f3 = 1;
												break;
											}	
										}
									}
									for(int k=flag2;k<flag3;k++){																																				
										if(jsonfile[k] != null){																							
											if(jsonfile[k].contains(strings[j][0])){
												list4.insert(k);
												f4 = 1;
												break;
											}	
										}
									}
									if(f1 == 0 && f2 == 0 && f3 == 0 && f4 == 0){
										jsonfile[cnt] = "{\"name\":\"" + strings[j][0] + "\",\"group\":1},";
										cnt++;
									}								
								}
							}
						}
					}
					list.insert(cnt-1);						
				}
				
				jsonfile[cnt-1] = jsonfile[cnt-1].substring(0, jsonfile[cnt-1].length()-1);
				jsonfile[cnt-1]+="],";
				jsonfile[cnt] = "\"links\":[";
				
				for(int j=0;j<primitive.length;j++){
					int len0 = list.get(0)-primitive.length;
					int len1 = list.get(1)-primitive.length;
					int len2 = list.get(2)-primitive.length;
					int len3 = list.get(3)-primitive.length;
					
					int i=0;
					if(j == 0){						
						for(i=0;i<len0;i++){
							for(int h=0;h<list1.size();h++){
								if(i == list1.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":1,\"target\":"  + (i+5) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+5) +",\"value\":1},";
						}
					}
					else if(j == 1){
						for(i=len0;i<len1;i++){
							for(int h=0;h<list2.size();h++){
								if(i == list2.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":2,\"target\":"  + (i+5) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+5) +",\"value\":1},";
						}						
					}
					else if(j == 2){
						for(i=len1;i<len2;i++){
							for(int h=0;h<list3.size();h++){
								if(i == list3.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":3,\"target\":"  + (i+5) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+5) +",\"value\":1},";
						}						
					}
					else if(j == 3){
						for(i=len2;i<len3;i++){
							for(int h=0;h<list4.size();h++){
								if(i == list4.get(h)){
									cnt++;
									jsonfile[cnt] = "{\"source\":4,\"target\":"  + (i+5) +",\"value\":1},";
								}
							}
							cnt++;
							jsonfile[cnt] = "{\"source\":" + j + ",\"target\":"  + (i+5) +",\"value\":1},";
						}						
					}
				}
			}
												
			//json文件的尾部处理（五种情况可共用）
			jsonfile[cnt] = jsonfile[cnt].substring(0, jsonfile[cnt].length()-1);
			
			String json = jsonfile[0];
			for(int i=1;i<jsonfile.length;i++){
				if(jsonfile[i] != null)
					json+="\n" + jsonfile[i];
			}			
			json+="\n" + "]}";
			
			//将最终的字符串json写入到template2.json中
			cGraph.writeStringContentToFile("/Users/lidawei/Downloads/template2.json", json);			
			
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	
	//将字符串写入文件的方法
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

}
