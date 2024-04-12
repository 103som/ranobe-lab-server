package App.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class SearchedRanobeDTO {

    public SearchedRanobeDTO() { meta_info = new MetaInfo(); }

    @Getter
    @Setter
    @JsonProperty("website_source")
    private String website;

    public static class BookInfo
    {
        @Setter
        @Getter
        @JsonProperty("id")
        private String id;

        @Setter
        @Getter
        @JsonProperty("logo")
        private String logo;

        @Setter
        @Getter
        @JsonProperty("title")
        private String title;

        @Getter
        @Setter
        @JsonProperty("description")
        private String description;
    }

    public static class MetaInfo {
        @Setter
        @Getter
        @JsonProperty("translator_id")
        private String translator;

        @Setter
        @Getter
        @JsonProperty("chapters_count")
        private String chapters_count;

        @Setter
        @Getter
        @JsonProperty("pages_count")
        private String pages_count;

        @Setter
        @Getter
        @JsonProperty("rating")
        private String rating;

        @Setter
        @Getter
        @JsonProperty("translation_rating")
        private String translation_rating;

        @Setter
        @Getter
        @JsonProperty("translation_status")
        private String translation_status;

        @Setter
        @Getter
        @JsonProperty("genres")
        private String genres;

        @Setter
        @Getter
        @JsonProperty("tags")
        private String tags;
    }

    @Setter
    @Getter
    @JsonProperty("book_information")
    public BookInfo book_info;

    @Setter
    @Getter
    @JsonProperty("meta_information")
    public MetaInfo meta_info;
}
