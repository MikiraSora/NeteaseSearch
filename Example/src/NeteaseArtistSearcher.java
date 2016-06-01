
import java.util.*;
import org.json.*;
import java.net.*;
import java.io.*;

/*
 {"result":{
	 "artistCount":1,
	 "artists":[{
		 "id":159403,
		 "name":"beatmania",
		 "picUrl":"http://p3.music.126.net/qbUOBrdHvlWEFOf1fBbBYQ==/5992338371878768.jpg",
		 "alias":["ビートマニア"],
		 "albumSize":27,
		 "picId":5992338371878768,
		 "img1v1Url":"http://p4.music.126.net/jUo3WrxK32oq2ceq8z8GDw==/5959353022966921.jpg",
		 "img1v1":5959353022966921,
		 "alias":["ビートマニア"],
		 "mvSize":0,
		 "followed":false,
		 "trans":null
		 }]
},"code":200}
*/

class ArtistSearchResult{
	Long id,albumSize,picId,img1v1,mvSize;
	String name,picUrl,alias[],img1v1Url,trans;
	boolean followed;
	
	public String getName(){
		if(name!=null){
			if(name.length()>0)
				return name;
		}
		
		if(alias!=null){
			if(alias[0].length()>0)
				return alias[0];
		}
		
		return "unknown";
	}
	
	public long getId(){
		return id;
	}
}

class ArtistSearchResultCollection extends ArrayList<ArtistSearchResult>{}

public class NeteaseArtistSearcher
{
	
	public static ArtistSearchResult Parse(String text)throws Exception{
		ArtistSearchResult result=new ArtistSearchResult();
		JSONObject jsonobj=new JSONObject(text);
		result.albumSize=(jsonobj.has("albumSize"))?(Long.parseLong(jsonobj.get("albumSize").toString())):(-1);
		result.picId=(jsonobj.has("picId"))?(Long.parseLong(jsonobj.get("picId").toString())):(-1);
		result.followed=(jsonobj.has("followed"))?(Boolean.parseBoolean(jsonobj.get("albumSize").toString())):(false);
		result.id=(jsonobj.has("id"))?(Long.parseLong( jsonobj.get("id").toString())):(-1);
		result.img1v1Url=(jsonobj.has("img1v1Url"))?(jsonobj.get("img1v1Url").toString()):("");
		result.img1v1=(jsonobj.has("img1v1"))?(Long.parseLong(jsonobj.get("img1v1").toString())):(-1);
		result.mvSize=(jsonobj.has("mvSize"))?(Long.parseLong(jsonobj.get("mvSize").toString())):(-1);
		result.name=(jsonobj.has("name"))?(jsonobj.get("name").toString()):("");
		result.trans=(jsonobj.has("trans"))?(jsonobj.get("trans").toString()):("");
		//result.=(jsonobj.has("albumSize"))?(jsonobj.get("albumSize")):(-1);
		
		if(jsonobj.has("alias")){
			JSONArray array=jsonobj.getJSONArray("alias");
			int len=array.length();
			result.alias=new String[len];
			for(int i=0;i<len;i++){
				result.alias[i]=array.get(i).toString();
			}
		}
		
		return result;
	}
	
	private static ArtistSearchResultCollection ParseAll(String text)throws Exception{
		ArtistSearchResultCollection collection=new ArtistSearchResultCollection();
		JSONObject array=new JSONObject(text);
		String tmp=array.get(("result")).toString();
		array=new JSONObject(tmp);
		JSONArray t= array.getJSONArray("artists");
		ArtistSearchResult result=null;
		for(int i=0;i<t.length();i++,result=null){
			result=Parse(t.get(i).toString());
			if(result==null)
				continue;
			collection.add(result);
		}
		return collection;
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

	public static ArtistSearchResultCollection Search(String text,int count,int offset){
		ArtistSearchResultCollection res=null;
		try{
			String buf=GetDataFromNet(text,count,100,offset);
			res=ParseAll(buf);
		}catch (Exception e){
			e.fillInStackTrace();
			//return null;
		}
		return res;
	}
}
