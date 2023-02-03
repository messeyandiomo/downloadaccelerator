package extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import decryption.Common;
import decryption.MyJSONObject;

public class Youtube {
	
	private String pageUrl = null;
	private String tempDirectory = null;
	private String imagesDirectory = null;
	private String videoId = null;

	public Youtube(String pageurl, String tempdirectory, String imagesdirectory) {
		// TODO Auto-generated constructor stub
		this.setPageUrl(pageurl);
		this.setTempDirectory(tempdirectory);
		this.setImagesDirectory(imagesdirectory);
		this.setVideoId(pageurl);
	}
	
	
	private String getVideoId() {
		
		return this.videoId;
	}
	
	private void setVideoId(String pageurl) {
		
		String workouturl = pageurl;
		
		if(Common.getDomainName(pageurl).contains("youtube.com")){
            if (pageurl.contains("embed")) {
            	String[] urlcomponents = pageurl.split("/");
            	workouturl = "https://www.youtube.com/watch?v=" + urlcomponents[urlcomponents.length - 1];
            }
            if(workouturl.contains("watch")) {
            	try {
					URL url = new URL(workouturl);
					String[] params = url.getQuery().split("&");
					Map<String, String> map = new HashMap<String, String>();

	                for (String param : params) {  
	                    String name = param.split("=")[0];  
	                    String value = param.split("=")[1];  
	                    map.put(name, value);  
	                }
	                this.videoId = map.get("v");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		}
	}
	
	
	/** get url of the player **/
	String getPlayerUrl(String webpage) {
		
		String result = null;
		int startIdx = 0;
		int endIdx = 0;
		MyJSONObject playerField;
		
		startIdx = webpage.indexOf("PLAYER_JS_URL");
		if(startIdx == -1)
			startIdx = webpage.indexOf("jsUrl");
		endIdx = webpage.indexOf(",", startIdx);
		try {
			playerField = MyJSONObject.parse("{" + webpage.substring(startIdx - 1, endIdx) + "}");
			ArrayList<String> playerFieldKeys = new ArrayList<String>(playerField.keySet());
			result = playerField.get(playerFieldKeys.get(playerFieldKeys.size() - 1)).getStr();
			if(result.startsWith("/"))
				result = "https://www.youtube.com" + result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
		
		return result;
	}
	
	
	@SuppressWarnings("resource")
	String getPoster(String videoid, String tempdirectory, String imagesdirectory) {
		
		String result = null;
		
		if(new File(tempdirectory + videoid + ".jpg").exists()) {
			result = tempdirectory + videoid + ".jpg";
		}
		else {
			String posterurl = "https://i.ytimg.com/vi/" + videoid + "/default.jpg";
			try {
				URL url = new URL(posterurl);
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				result = tempdirectory + videoid + ".jpg";
				FileOutputStream fos = new FileOutputStream(result);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				result = imagesdirectory + "defaultposter.jpg";
				//e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	void updateListOfMedias(ArrayList<JsonNode> listofmedias, String title, String videoid, String playerurl, String tempdirectory, String imagesdirectoty) {
		
		for (int index = 0; index < listofmedias.size(); index++){
            JsonNode media = listofmedias.get(index);
            ((ObjectNode) media).put("title", title);
            ((ObjectNode) media).put("poster", getPoster(videoid, tempdirectory, imagesdirectoty));
            
            if (media.get("signatureCipher") != null) {
            	try {
					((ObjectNode) media).put("signatureCipher", URLDecoder.decode(media.get("signatureCipher").asText(), StandardCharsets.UTF_8.name()));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                ((ObjectNode) media).put("videoId", videoid);
                ((ObjectNode) media).put("playerUrl", playerurl);
            }
        }
	}
	
	
	ArrayList<JsonNode> extractMedias(ArrayList<JsonNode> listofmedias, String type){
		
		ArrayList<JsonNode> result = new ArrayList<>();
        int idx = 0;
        int len = listofmedias.size();

        while (idx < len) {
            if (listofmedias.get(idx).get("mimeType").asText().contains(type))
                result.add(listofmedias.get(idx));
            idx++;
        }
        
		return result;
	}
	
	
	ArrayList<JsonNode> combineVideoAndAudioProperties(ArrayList<JsonNode> listofmedias) {
		
		ArrayList<JsonNode> listofaudiomedias = extractMedias(listofmedias, "audio");
		ArrayList<JsonNode> listofvideomedias = extractMedias(listofmedias, "video");
        
        for (int index = 0; index < listofvideomedias.size(); index++) {
            JsonNode media = listofvideomedias.get(index);
            String[] splitmimetype = media.get("mimeType").asText().split(";")[0].split("/");
            String extension = splitmimetype[1];
            
            for (int idx = 0; idx < listofaudiomedias.size(); idx++) {
                JsonNode audiomedia = listofaudiomedias.get(idx);
                if (audiomedia.get("mimeType").asText().contains(extension)) {
                    if (audiomedia.get("url") != null) {
                    	((ObjectNode) media).put("audioUrl", audiomedia.get("url").asText());
                    }
                    else if (audiomedia.get("signatureCipher") != null){
                    	((ObjectNode) media).put("audioSignatureCipher", audiomedia.get("signatureCipher").asText());
                    	((ObjectNode) media).put("audioId", audiomedia.get("videoId").asText());
                    	((ObjectNode) media).put("audioPlayerUrl", audiomedia.get("playerUrl").asText());
                    }
                    ((ObjectNode) media).put("audioContentLength", audiomedia.get("contentLength").asText());
                    ((ObjectNode) media).put("audioMimeType", audiomedia.get("mimeType").asText());
                    break;
                }
            }
        }
        
        return listofvideomedias;
	}
	
	
	int getLastIndex(String webpage, int firstindex, String begin, String end) {
			
		String[] splitvalue = webpage.substring(firstindex).split("");
		int fidx = 0;
		int lidx = fidx + 1;
		int accoladescount = 1;
		while(accoladescount > 0) {
			if(splitvalue[lidx].equals(begin))
				accoladescount++;
			else if(splitvalue[lidx].equals(end))
				accoladescount--;
			lidx++;
		}
		
		return firstindex + lidx;
	}
	
	
	String getValue(String key, String webpage) {
		
		int firstIndex = webpage.indexOf(key);
		int lastIndex = firstIndex + key.length();
		
		while(!webpage.substring(lastIndex, lastIndex + 1).equals(":"))
			lastIndex++;
		firstIndex = lastIndex + 1;
		lastIndex = firstIndex + 1;
		
		if(webpage.substring(firstIndex, lastIndex).equals("{"))
			lastIndex = getLastIndex(webpage, firstIndex, "{", "}");
		else if(webpage.substring(lastIndex, lastIndex + 1).equals("["))
			lastIndex = getLastIndex(webpage, firstIndex, "[", "]");
		else {
			String[] splitvalue = webpage.substring(firstIndex).split("");
			int idx = 0;
			while(!splitvalue[idx].equals(","))
				idx++;
			lastIndex = firstIndex + idx;
		}
		
		return webpage.substring(firstIndex, lastIndex);
	}
	
	/** get meta data of the player **/
	JsonNode getMetaData(String data) {
		
		JsonNode result = null;
		
        try {
			result = JSON.parse(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return result;
	}
	
	
	String ytInitialPlayerResponse(String webpage) {
		
		String result = null;
		
		String stringOfLocalisation = "var ytInitialPlayerResponse = {";
        int firstIndex = webpage.indexOf(stringOfLocalisation) + stringOfLocalisation.length() - 1;
        
        if (firstIndex != -1) {
        	result = webpage.substring(firstIndex);
            result = result.replace("/\\\\u0026/g", "&");
            result = result.replace("/,\\ /g", "-");
            result = result.replace("/\\\\\\\"/g", "");
        }
		
		return result;
	}
	
	/** get list of medias **/
	public ArrayList<JsonNode> getMedias(){
		
		ArrayList<JsonNode> result = null;
		String webpage = Common.getWebPage(this.getPageUrl());
		
		if(webpage.length() != 0) {
			String playerUrl = getPlayerUrl(webpage);
			String ytInitialPlayerResponse = ytInitialPlayerResponse(webpage);
			String streamingData = getValue("streamingData", ytInitialPlayerResponse);
			JsonNode streamingDataJson = getMetaData(streamingData);
			if(streamingDataJson != null) {
				String title = getValue("title", ytInitialPlayerResponse);
				
				ArrayList<JsonNode> mediaFormats = new ArrayList<JsonNode>();
				JsonNode streamingdataformats = streamingDataJson.get("formats");
				
				if(streamingdataformats != null) {
					for (JsonNode jsonNode : streamingdataformats)
						mediaFormats.add(jsonNode);
					mediaFormats = extractMedias(mediaFormats, "video");
					updateListOfMedias(mediaFormats, title, this.getVideoId(), playerUrl, this.getTempDirectory(), this.getImagesDirectory());
				}
				
				ArrayList<JsonNode> mediaAdaptativeFormats = new ArrayList<JsonNode>();
				JsonNode streamingDataAdaptativeFormats = streamingDataJson.get("adaptiveFormats");
				
				if(streamingDataAdaptativeFormats != null) {
					for (JsonNode jsonNode : streamingDataAdaptativeFormats)
						mediaAdaptativeFormats.add(jsonNode);
					updateListOfMedias(mediaAdaptativeFormats, title, this.getVideoId(), playerUrl, this.getTempDirectory(), this.getImagesDirectory());
					mediaAdaptativeFormats = combineVideoAndAudioProperties(mediaAdaptativeFormats);
				}
				/** concatenation of stream data formats and stream adaptative formats **/
				result = new ArrayList<>();
				for (int i = 0; i < mediaFormats.size(); i++)
					result.add(mediaFormats.get(i));
				for (int i = 0; i < mediaAdaptativeFormats.size(); i++)
					result.add(mediaAdaptativeFormats.get(i));
			}
		}
		
		return result;
	}
	
	

	private String getPageUrl() {
		return pageUrl;
	}

	private void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	private String getTempDirectory() {
		return tempDirectory;
	}

	private void setTempDirectory(String tempDirectory) {
		this.tempDirectory = tempDirectory;
	}

	private String getImagesDirectory() {
		return imagesDirectory;
	}

	private void setImagesDirectory(String imagesDirectory) {
		this.imagesDirectory = imagesDirectory;
	}

}
