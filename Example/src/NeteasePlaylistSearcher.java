
import java.net.*;
import java.io.*;
import java.util.*;
import org.json.*;
/*
 {"result":{
	 "playlists":[{
		 "id":71385702,
		 "name":"云音乐ACG音乐榜",
		 "coverImgUrl":"http://p1.music.126.net/RLM4r1_a9XUSQ0GO58QsXQ==/2924700931525267.jpg",
		 "creator":{
			 "nickname":"网易云音乐",
			 "userId":1,
			 "userType":3,
			 "authStatus":1
			 },
		"subscribed":false,
		"trackCount":88,
		"userId":1,
		"playCount":4338482,
		"bookCount":32567,
		"highQuality":false
	}],
	"playlistCount":300
},
"code":200}
*/

class PlaylistSearchResult{
	long id,trackCount,userId,playCount,bookCount;
	String name,coverImgUrl;
	boolean highQuality,subscribed;
	Creator creator;
	
	public static class Creator{
		String nickname;
		long userId,userType,authStatus;
	}
}

class PlaylistSearchResultCollection extends ArrayList<PlaylistSearchResult>{}

public class NeteasePlaylistSearcher
{
	private static PlaylistSearchResult Parse(String text)throws Exception{
		PlaylistSearchResult result=new PlaylistSearchResult();
		JSONObject jsonobj=new JSONObject(text);
		result.bookCount=(jsonobj.has("bookCount"))?(jsonobj.get("bookCount")):(-1);
		result.coverImgUrl=(jsonobj.has("coverImgUrl"))?(jsonobj.get("coverImgUrl").toString()):("");
		result.highQuality=(jsonobj.has("highQuality"))?(Boolean.parseBoolean(jsonobj.get("highQuality").toString())):(false);
		result.id=(jsonobj.has("id"))?(jsonobj.get("id")):(-1);
		result.name=(jsonobj.has("name"))?(jsonobj.get("name").toString()):("");
		result.playCount=(jsonobj.has("playCount"))?(jsonobj.get("playCount")):(-1);
		result.trackCount=(jsonobj.has("trackCount"))?(jsonobj.get("trackCount")):(-1);
		result.userId=(jsonobj.has("userId"))?(jsonobj.get("userId")):(-1);
		result.subscribed=(jsonobj.has("subscribed"))?(Boolean.parseBoolean(jsonobj.get("subscribed").toString())):(false);
		
		if(jsonobj.has("creator")){
			JSONObject jsonc=jsonobj.getJSONObject("creator");
			result.creator=new PlaylistSearchResult.Creator();
			result.creator.authStatus=(jsonc.has("authStatus"))?(jsonc.get("authStatus")):(-1);
			result.creator.nickname=(jsonc.has("nickname"))?(jsonc.get("nickname").toString()):("");
			result.creator.userId=(jsonc.has("userId"))?(jsonc.get("userId")):(-1);
			result.creator.userType=(jsonc.has("userType"))?(jsonc.get("userType")):(-1);
		}
		
		return result;
		}
	
	
	private static PlaylistSearchResultCollection ParseAll(String text)throws Exception{
		PlaylistSearchResultCollection collection=new PlaylistSearchResultCollection();
		JSONObject array=new JSONObject(text);
		String tmp=array.get(("result")).toString();
		array=new JSONObject(tmp);
		JSONArray t= array.getJSONArray("playlists");
		PlaylistSearchResult result=null;
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
	public static PlaylistSearchResultCollection Search(String text,int count,int offset){
		PlaylistSearchResultCollection res=null;
		try{
			String buf=GetDataFromNet(text,count,1000,offset);
			res=ParseAll(buf);
		}catch (Exception e){
			e.fillInStackTrace();
		}
		return res;
	}
}
