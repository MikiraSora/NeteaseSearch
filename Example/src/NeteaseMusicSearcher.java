
import java.util.*;
import java.net.*;
import java.io.*;
import org.apache.commons.codec.*;
import org.w3c.dom.*;
import org.json.*;
/*
 "album": {
 "status": 1, 
 "copyrightId": 0, 
 "name": "\u795e\u7684\u6e38\u620f", 
 "artist": {
 "alias": [], 
 "picUrl": null, 
 "id": 0, 
 "name": ""
 }, 
 "publishTime": 1344528000000, 
 "id": 32311, 
 "size": 10
 }, 
 "status": 1, 
 "copyrightId": 0, 
 "name": "\u73ab\u7470\u8272\u7684\u4f60", 
 "mvid": 5102, 
 "alias": [], 
 "artists": [
 {
 "alias": [], 
 "picUrl": null, 
 "id": 10557, 
 "name": "\u5f20\u60ac"
 }
 ], 
 "duration": 297927, 
 "id": 3266
*/

class SearchResult{
	String name;
	String[] alias;
	public static class Artist{
		String[] alias;
		String picUrl,name;
		int id;
	}
	Artist[] artist;
	Album album;
	int status,copyrightId,mvid,duration,id;
	public static class Album{
		int status,copyrightId,id,size;
		long publishTime;
		String name;
		public static class Album_Artist{
			String[] alias;
			String name,picUrl;
			int id;
		}
		Album_Artist artist;
	}
	
	public String getSongTittle(){
		String val=this.name;
		return val==null?"null":val;
	}
	
	public String getSongArtist(){
		String val=null;
		if(this.artist.length>0){
			val=this.artist[0].name;
		}
		return val==null?"null":val;
	}
	
	public String getSongAlbum(){
		String val=null;
		if(this.album!=null){
			val=this.album.name;
		}
		return val==null?"null":val;
	}
	
	public long getSongId(){
		return this.id;
	}
	
	public String toSongString()
	{
		return String.format("(%d) %s - %s (%s)",getSongId(),getSongArtist(),getSongTittle(),getSongAlbum());
	}
}

class SearchResultCollection extends ArrayList<SearchResult>{}

class NeteaseMusicSearcher{
	private NeteaseMusicSearcher(){}
	
	public enum SearchType{
		Song,
		Album,
		Singer,
		PlayList,
		UserName,
	}
	
	private static String GetDataFromNet(String text,int count,int type,int offset)throws Exception{
		URL url=new URL("http://music.163.com/api/search/get/");
		HttpURLConnection con=(HttpURLConnection)url.openConnection();
		con.addRequestProperty("appver","2.0.2");
		con.addRequestProperty("referer","http://music.163.com");
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		OutputStreamWriter writer=new OutputStreamWriter(con.getOutputStream());
		String data=String.format("s=%s&limit=%d&type=%d&offset=%d",text,count,type,offset);
		writer.append(data);
		writer.flush();

		InputStream is=con.getInputStream();
		InputStreamReader reader=new InputStreamReader(is);
		int c=-1;
		String buf=new String();
		while((c=reader.read())!=-1){
			buf+=(char)c;
		}
		return buf;
	}
	
	private static SearchResult.Artist ParseArtist(String text)throws Exception{
		SearchResult.Artist artist=new SearchResult.Artist();
		JSONObject jsonobj=new JSONObject(text);
		artist.id=(jsonobj.has("id"))?(jsonobj.get("id")):(-1);
		artist.picUrl=(jsonobj.has("picUrl"))?(jsonobj.get("picUrl").toString()):(null);
		artist.name=(jsonobj.has("name"))?(jsonobj.get("name").toString()):("unknown");
		if(jsonobj.has("alias")){
			JSONArray array=jsonobj.getJSONArray("alias");
			int len=array.length();
			artist.alias=new String[len];
			for(int i=0;i<array.length();i++){
				artist.alias[i]=array.get((i)).toString();
			}
		}
		return artist;
	}
	
	private static SearchResult.Album ParseAlbum(String text)throws Exception{
		JSONObject jsonobj=new JSONObject(text);
		SearchResult.Album album=new SearchResult.Album();
		
		album.copyrightId=(jsonobj.has("copyrightId"))?(jsonobj.get("copyrightId")):(-1);
		album.id=(jsonobj.has("id"))?(jsonobj.get("id")):(-1);
		album.status=(jsonobj.has("status"))?(jsonobj.get("status")):(-1);
		album.publishTime=(jsonobj.has("publishTime"))?((long)jsonobj.get("publishTime")):(-1);
		album.size=(jsonobj.has("size"))?(jsonobj.get("size")):(-1);
		album.name=(jsonobj.has("name"))?(jsonobj.get("name").toString()):("unknown");
		album.copyrightId=(jsonobj.has("copyrightId"))?(jsonobj.get("copyrightId")):(-1);
		
		return album;
	}
	
	private static SearchResult Parse(String text)throws Exception{
		//Parse Base Info
		SearchResult result=new SearchResult();
		JSONObject jsonobj=new JSONObject(text);
		result.mvid=(jsonobj.has("mvid"))?(jsonobj.get("mvid")):(-1);
		result.id=(jsonobj.has("id"))?(jsonobj.get("id")):(-1);
		result.copyrightId=(jsonobj.has("copyrightId"))?(jsonobj.get("copyrightId")):(-1);
		//result.alias=(jsonobj.has("alias"))?((String[])jsonobj.get("alias")):(null);
		result.duration=(jsonobj.has("duration"))?(jsonobj.get("duration")):(-1);
		result.name=(jsonobj.has("name"))?(jsonobj.get("name").toString()):("unknown");
		result.status=(jsonobj.has("status"))?(jsonobj.get("status")):(-1);
		
		if(jsonobj.has("alias")){
			JSONArray array=jsonobj.getJSONArray("alias");
			int len=array.length();
			result.alias=new String[len];
			for(int i=0;i<array.length();i++){
				result.alias[i]=array.get(i).toString();
			}
		}
		
		if(jsonobj.has("album"))
		    result.album=ParseAlbum(jsonobj.get("album").toString());
		if(jsonobj.has("artists")){
			JSONArray array=jsonobj.getJSONArray("artists");
			int len=array.length();
			result.artist=new SearchResult.Artist[len];
			for(int i=0;i<array.length();i++){
				result.artist[i]=ParseArtist(array.get(i).toString());
			}
		}
			
			//result.artist=ParseArtist(jsonobj.get("artist")).toString();
		return result;
	}
	private static SearchResultCollection ParseAll(String buf,boolean is_Full)throws Exception{
		JSONObject array=new JSONObject(buf);
		SearchResultCollection collection=new SearchResultCollection();
		String tmp=array.get(("result")).toString();
		array=new JSONObject(tmp);
		JSONArray t= array.getJSONArray("songs");
		SearchResult result=null;
		for(int i=0;i<t.length();i++,result=null){
			result=Parse(t.get(i).toString());
			if(result==null)
				continue;
			collection.add(result);
		}
		return collection;
	}
	public static SearchResultCollection Search(String text,int count,int type,int offset){
		SearchResultCollection res=null;
		try{
			String buf=GetDataFromNet(text,count,type,offset);
			res=ParseAll((buf),false);
		}catch (Exception e){
			e.fillInStackTrace();
		}
		return res;
	}
}
