
import java.util.*;
import java.net.*;
import java.io.*;
import org.json.*;
/*
 {"result":
	 {"userprofiles":
 		[{"backgroundUrl":"http://p2.music.126.net/OtT9it9nXt5CCsipp04EWw==/3398590442903931.jpg"
		,"defaultAvatar":false,
		"followed":false,
		"detailDescription":"",
		"djStatus":0,
		"mutual":false,
		"userId":67789185,
		"nickname":"恶俗小黑",
		"avatarUrl":"http://p4.music.126.net/HS_O2UQLjSEj8GO3TIhyuw==/2944492140421425.jpg",
		"vipType":0,
		"expertTags":null,
		"avatarImgId":2944492140421425,
		"backgroundImgId":3398590442903931,
		"province":450000,
		"city":450700,
		"birthday":-2209017600000,
		"gender":0,
		"accountStatus":0,
		"userType":0,
		"authStatus":0,
		"description":"",
		"signature":"",
		"authority":0,
		"followeds":4,
		"follows":4,
		"eventCount":0,
		"playlistCount":26,
		"playlistBeSubscribedCount":1
		}],
"userprofileCount":1
},"code":200}
*/

class UserSearchResult
{
	String backgroundUrl,detailDescription,nickname,avatarUrl,description,signature;
	boolean defaultAvatar,followed,mutual;
	long djStatus,userId,vipType,province,city,birthday,gender,accountStatus,userType,authority,followeds,follows,eventCounts,playlistCount,playlistBeSubscribedCount;
	
	public String getName(){
		return nickname;
	}
	
	public long getId(){
		return userId;
	}
}

class UserSearchResultCollection extends ArrayList<UserSearchResult>{}

class NeteaseUserSearcher{
	
	private static UserSearchResult Parse(String text)throws Exception{
		UserSearchResult result=new UserSearchResult();
		JSONObject jsonobj=new JSONObject(text);
		result.accountStatus=(jsonobj.has("accountStatus"))?(jsonobj.get("accountStatus")):(-1);
		result.authority=(jsonobj.has("authority"))?(jsonobj.get("authority")):(-1);
		result.backgroundUrl=(jsonobj.has("backgroundUrl"))?(jsonobj.get("backgroundUrl").toString()):("");
		result.avatarUrl=(jsonobj.has("avatarUrl"))?(jsonobj.get("avatarUrl").toString()):("");
		result.birthday=(jsonobj.has("birthday"))?((long)jsonobj.get("birthday")):(-1);
		result.city=(jsonobj.has("city"))?(jsonobj.get("city")):(-1);
		result.defaultAvatar=(jsonobj.has("defaultAvatar"))?(Boolean.parseBoolean( jsonobj.get("defaultAvatar").toString())):(false);
		result.description=(jsonobj.has("description"))?(jsonobj.get("description").toString()):("");
		result.detailDescription=(jsonobj.has("detailDescription"))?(jsonobj.get("detailDescription").toString()):("");
		result.djStatus=(jsonobj.has("djStatus"))?(jsonobj.get("djStatus")):(-1);
		result.eventCounts=(jsonobj.has("eventCounts"))?(jsonobj.get("eventCounts")):(-1);
		result.followeds=(jsonobj.has("followeds"))?(jsonobj.get("followeds")):(-1);
		result.gender=(jsonobj.has("gender"))?(jsonobj.get("gender")):(-1);
		result.nickname=(jsonobj.has("nickname"))?(jsonobj.get("nickname").toString()):("");
		result.playlistBeSubscribedCount=(jsonobj.has("playlistBeSubscribedCount"))?(jsonobj.get("playlistBeSubscribedCount")):(-1);
		result.province=(jsonobj.has("province"))?(jsonobj.get("province")):(-1);
		result.signature=(jsonobj.has("signature"))?(jsonobj.get("signature").toString()):("");
		result.userId=(jsonobj.has("userId"))?(jsonobj.get("userId")):(-1);
		result.userType=(jsonobj.has("userType"))?(jsonobj.get("userType")):(-1);
		result.vipType=(jsonobj.has("vipType"))?(jsonobj.get("vipType")):(-1);
		result.mutual=(jsonobj.has("mutual"))?(Boolean.parseBoolean( jsonobj.get("mutual").toString())):(false);
		result.followed=(jsonobj.has("followed"))?(Boolean.parseBoolean( jsonobj.get("followed").toString())):(false);
		return result;
	}
	
	private static UserSearchResultCollection ParseAll(String text)throws Exception{
		UserSearchResultCollection collection=new UserSearchResultCollection();
		JSONObject array=new JSONObject(text);
		String tmp=array.get(("result")).toString();
		array=new JSONObject(tmp);
		JSONArray t= array.getJSONArray("userprofiles");
		UserSearchResult result=null;
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
	
	public static UserSearchResultCollection Search(String text,int count,int offset){
		UserSearchResultCollection res=null;
		try{
			String buf=GetDataFromNet(text,count,1002,offset);
			res=ParseAll(buf);
		}catch (Exception e){
			e.fillInStackTrace();
		}
		return res;
	}
}


