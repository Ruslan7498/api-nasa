import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Header {
    private final String date;
    //private final String explanation;
    private final String mediaType;
    private final String title;
    private final String url;

    public Header(
            @JsonProperty("date") String date,
            //@JsonProperty("explanation") String explanation,
            @JsonProperty("media_type") String mediaType,
            @JsonProperty("title") String title,
            @JsonProperty("url") String url) {
        this.date = date;
        //this.explanation = explanation;
        this.mediaType = mediaType;
        this.title = title;
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    /*
    public String getExplanation() {
        return explanation;
    }
     */

    public String getMediaType() {
        return mediaType;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "API_NASA" +
                "\n date: " + date +
                //"\n explanation: " + explanation +
                "\n media_type: " + mediaType +
                "\n title: " + title +
                "\n url: " + url;
    }
}
