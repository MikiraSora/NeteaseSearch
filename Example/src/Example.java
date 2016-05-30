import java.util.*;
//import info.debatty.java.stringsimilarity.*;
import java.net.*;
import java.io.*;
//import com.eclipsesource.json.*;
import org.json.*;
//import org.apache.commons.codec.*;


public class Example
{
	public static void main(String[] args)throws Exception
	{
		String name="Mendes";
		
		SearchResultCollection co;
		co=NeteaseMusicSearcher.Search(name,10,1,0);
		
		System.out.println(String.format("搜索 : %s",name));
		System.out.println(String.format("已找到 %s 个结果",co.size()));
		
		for(SearchResult result: co){
			System.out.println(result.toSongString());
		}
		
		System.out.println("end");
		
	}
}
