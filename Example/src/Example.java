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
		String name="mendes";
		
		SongSearchResultCollection co;
		co=NeteaseSongSearcher.Search(name,4,0);
		
		System.out.println(String.format("搜索 : %s ,数量 %d",name,4));
		System.out.println(String.format("已找到 %s 个结果",co.size()));
		
		for(SongSearchResult result: co){
			System.out.println(result.toString());
		}
		
		name="DJ";
		UserSearchResultCollection uco=NeteaseUserSearcher.Search(name,4,0);
		System.out.println(String.format("搜索用户 : %s ,数量 %d",name,4));
		System.out.println(String.format("已找到 %s 个结果",uco.size()));
		for(UserSearchResult res:uco)
			System.out.println(String.format("%s (%d)",res.nickname,res.userId));
		System.out.println("end");
		
		name="电音";
		PlaylistSearchResultCollection pco;
		pco=NeteasePlaylistSearcher.Search(name,4,0);
		System.out.println(String.format("搜索歌单 : %s ,数量 %d",name,4));
		System.out.println(String.format("已找到 %s 个结果",uco.size()));
		for(PlaylistSearchResult res:pco)
			System.out.println(String.format("%s (created by %s)",res.name,res.creator.nickname));
			
		System.out.println();
		name="dj taka";
		AlbumSearchResultCollection aco=NeteaseAlbumSearcher.Search(name,3,0);
		System.out.println(String.format("搜索专辑 : %s ,数量 %d",name,3));
		System.out.println(String.format("已找到 %s 个结果",uco.size()));
		for(AlbumSearchResult res:aco)
			System.out.println(String.format("%s - %s",res.getName(),res.getArtist()));
		
		System.out.println();
		name="Dragon";
		ArtistSearchResultCollection atco=NeteaseArtistSearcher.Search(name,3,0);
		System.out.println(String.format("搜索艺术家 : %s ,数量 %d",name,3));
		System.out.println(String.format("已找到 %s 个结果",uco.size()));
		for(ArtistSearchResult res:atco)
			System.out.println(String.format("%s - %d",res.getName(),res.id));
		
		System.out.println("end");
		
		/*
		URL url=new URL("http://music.163.com/api/search/get/");
		HttpURLConnection con=(HttpURLConnection)url.openConnection();
		con.addRequestProperty("appver","2.0.2");
		con.addRequestProperty("referer","http://music.163.com");
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		OutputStreamWriter writer=new OutputStreamWriter(con.getOutputStream());
		String data=String.format("s=%s&limit=%d&type=%d&offset=%d","beatmania",1,100,0);
		writer.append(data);
		writer.flush();

		InputStream is=con.getInputStream();
		InputStreamReader reader=new InputStreamReader(is);
		int c=-1;
		String buf=new String();
		while((c=reader.read())!=-1){
			buf+=(char)c;
		}
		OutputStreamWriter w=new OutputStreamWriter(new FileOutputStream("/sdcard/pl.txt"));
		w.write(buf);
		w.flush();
		System.out.println(buf);*/
	}
}
