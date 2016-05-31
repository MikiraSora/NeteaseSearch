
import java.util.*;
import org.json.*;
import java.net.*;
import java.io.*;
/*
 {"result":{
	 "albums":[{
		 "songs":[],
		 "tags":"",
		 "alias":["beatmania IIDX 19: Lincle Original Soundtrack"],
		 "publishTime":1325347200004,
		 "company":"",
		 "artists":[{
			 "img1v1Id":0,
			 "alias":[],
			 "albumSize":0,
			 "img1v1Url":"http://p4.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg"
			 ,"trans":"",
			 "musicSize":0,
			 "picId":0,
			 "briefDesc":"",
			 "picUrl":"http://p4.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg",
			 "name":"beatmania","
			 id":159403
			 }],
		"status":1,
		"picId":836728348761057,
		"briefDesc":"",
		"artist":{
			"img1v1Id":0,
			"alias":["ビートマニア"],
			"albumSize":27,
			"img1v1Url":"http://p3.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg",
			"trans":"",
			"musicSize":1251,
			"picId":5992338371878768,
			"briefDesc":"",
			"picUrl":"http://p4.music.126.net/qbUOBrdHvlWEFOf1fBbBYQ==/5992338371878768.jpg",
			"name":"beatmania",
			"id":159403
			},
	"picUrl":"http://p3.music.126.net/dOioWfK7nMKX4DB5mwPdtQ==/836728348761057.jpg",
			"copyrightId":0,
			"commentThreadId":"R_AL_3_2078140",
			"description":"",
			"blurPicUrl":"http://p3.music.126.net/dOioWfK7nMKX4DB5mwPdtQ==/836728348761057.jpg",
			"companyId":0,
			"pic":836728348761057,
			"name":"beatmania IIDX 19: Lincle O.S.T",
			"id":2078140,
			"type":"专辑",
			"size":65,
			"transNames":["狂热节拍"]
		}],
		"albumCount":33,
		"queryCorrected":["ida cox"]
},
"code":200}

 
*/

class AlbumSearchResult{
	String[] songs,alias;
	String tags,company,briefDesc;
	long publishTime,status,picId,copyrightId,companyId,pic,id,size;
	String commentThreadId,description,name,picUrl,type,transName;
	Artist artist,artists[];
	
	public static class Artist{
		long img1v1Id,albumSize,musicSize,picId,id,copyrightId;
		String[] alias;
		String img1v1Url,trans,briefDesc,picUrl,name;
	}
	
	public String getName(){
		String val=null;
		if(this.name!=null)
			if(this.name.length()!=0)
				val=this.name;
		else
			if(this.alias!=null){
				if(this.alias[0].length()!=0)
					val=this.alias[0];
			}
		return val==null?"unknown":val;
	}
	
	public String getArtist(){
		String val=null;
		if(this.artist!=null)
			val=this.artist.name;
		else
			if(this.artists!=null){
				if(this.artists[0].name.length()!=0){
					val=this.artists[0].name;
				}
				else
					if(this.artists[0].alias!=null){
						if(this.artists[0].alias[0].length()!=0){
							val=this.artists[0].alias[0];
						}
					}
			}
		return val==null?"unknown":val;
	}
}

class AlbumSearchResultCollection extends ArrayList<AlbumSearchResult>{}

public class NeteaseAlbumSearcher
{
	private static AlbumSearchResult.Artist ParseArtist(String text)throws Exception{
		AlbumSearchResult.Artist result=new AlbumSearchResult.Artist();
		JSONObject jsonobj=new JSONObject(text);
		result.albumSize=(jsonobj.has("albumSize"))?(jsonobj.get("albumSize")):(-1);
		result.briefDesc=(jsonobj.has("briefDesc"))?(jsonobj.get("briefDesc").toString()):("");
		result.copyrightId=(jsonobj.has("copyrightId"))?(jsonobj.get("copyrightId")):(-1);
		result.id=(jsonobj.has("id"))?(jsonobj.get("id")):(-1);
		result.img1v1Id=(jsonobj.has("img1v2Id"))?(jsonobj.get("img1v1Id")):(-1);
		result.img1v1Url=(jsonobj.has("img1v1Url"))?(jsonobj.get("img1v1Url").toString()):("");
		result.musicSize=(jsonobj.has("musicSize"))?(jsonobj.get("musicSize")):(-1);
		result.name=(jsonobj.has("name"))?(jsonobj.get("name").toString()):("");
		result.picId=(jsonobj.has("picId"))?(jsonobj.get("picId")):(-1);
		result.picUrl=(jsonobj.has("picUrl"))?(jsonobj.get("picUrl").toString()):("");
		result.trans=(jsonobj.has("trans"))?(jsonobj.get("trans").toString()):("");
		
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
	
	private static AlbumSearchResult Parse(String text)throws Exception{
		AlbumSearchResult result=new AlbumSearchResult();
		JSONObject jsonobj=new JSONObject(text);
		result.briefDesc=(jsonobj.has("briefDesc"))?(jsonobj.get("briefDesc").toString()):("");
		result.commentThreadId=(jsonobj.has("commentThreadId"))?(jsonobj.get("commentThreadId").toString()):("");
		result.company=(jsonobj.has("company"))?(jsonobj.get("company").toString()):("");
		result.companyId=(jsonobj.has("companyId"))?(jsonobj.get("companyId")):(-1);
		result.copyrightId=(jsonobj.has("copyrightId"))?(jsonobj.get("copyrightId")):(-1);
		result.description=(jsonobj.has("description"))?(jsonobj.get("description").toString()):("");
		result.id=(jsonobj.has("id"))?(jsonobj.get("id")):(-1);
		result.name=(jsonobj.has("name"))?(jsonobj.get("name").toString()):("");
		result.pic=(jsonobj.has("pic"))?((long)jsonobj.get("pic")):(-1);
		result.picId=(jsonobj.has("picId"))?((long)jsonobj.get("picId")):(-1);
		result.picUrl=(jsonobj.has("picUrl"))?(jsonobj.get("picUrl").toString()):("");
		result.publishTime=(jsonobj.has("publishTime"))?((long)jsonobj.get("publishTime")):(-1);
		result.size=(jsonobj.has("size"))?(jsonobj.get("size")):(-1);
		result.status=(jsonobj.has("status"))?(jsonobj.get("status")):(-1);
		result.tags=(jsonobj.has("tags"))?(jsonobj.get("tags").toString()):("");
		result.transName=(jsonobj.has("transName"))?(jsonobj.get("transName").toString()):("");
		result.type=(jsonobj.has("type"))?(jsonobj.get("type").toString()):("");
		//result.=(jsonobj.has("accountStatus"))?(jsonobj.get("accountStatus")):(-1);
		
		if(jsonobj.has("alias")){
			JSONArray array=jsonobj.getJSONArray("alias");
			int len=array.length();
			result.alias=new String[len];
			for(int i=0;i<len;i++){
				result.alias[i]=array.get(i).toString();
			}
		}
		
		if(jsonobj.has("artists")){
			JSONArray array=jsonobj.getJSONArray("artists");
			int len=array.length();
			result.artists=new AlbumSearchResult.Artist[len];
			for(int i=0;i<len;i++){
				result.artists[i]=ParseArtist(array.get(i).toString());
			}
		}
		
		return result;
		}
		
	private static AlbumSearchResultCollection ParseAll(String text)throws Exception{
		AlbumSearchResultCollection collection=new AlbumSearchResultCollection();
		JSONObject array=new JSONObject(text);
		String tmp=array.get(("result")).toString();
		array=new JSONObject(tmp);
		JSONArray t= array.getJSONArray("albums");
		AlbumSearchResult result=null;
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

	public static AlbumSearchResultCollection Search(String text,int count,int offset){
		AlbumSearchResultCollection res=null;
		try{
			String buf=GetDataFromNet(text,count,10,offset);
			res=ParseAll(buf);
		}catch (Exception e){
			e.fillInStackTrace();
		}
		return res;
	}
}
