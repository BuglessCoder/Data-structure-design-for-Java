package sjjgzhsj;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class WebCrawler {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input a URL:");
		String url = sc.nextLine();
		crawler(url);

	}
	
	public static void crawler(String startingURL){
		ArrayList<String> listOfPengdingURLs = new ArrayList<>();
		ArrayList<String> listOfTraversedURLs = new ArrayList<>();
		
		listOfPengdingURLs.add(startingURL);
		while(!listOfPengdingURLs.isEmpty() && listOfTraversedURLs.size()<=100){
			String urlString = listOfPengdingURLs.remove(0);
			if(!listOfTraversedURLs.contains(urlString));{
				listOfTraversedURLs.add(urlString);
				System.out.println("Crawl " + urlString);
				for(String s: getSubURLs(urlString)){
					if(!listOfTraversedURLs.contains(s)){
						listOfTraversedURLs.add(s);
					}
				}
			}
		}
	}

	public static ArrayList<String> getSubURLs(String urlString){
		ArrayList<String> list = new ArrayList<>();
		
		try {
			URL url = new URL(urlString);
			Scanner sc = new Scanner(url.openStream());
			int current = 0;
			while(sc.hasNext()){
				String line = sc.nextLine();
				current  = line.indexOf("http:", current);
				while(current>0){
					int endIndex = line.indexOf("\"", current);
					if(endIndex>0){
						list.add(line.substring(current, endIndex));
						current = line.indexOf("http:", endIndex);
					}
					else{
						current = -1;
					}
				}
			}
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		return list;
		
	}
	
}
