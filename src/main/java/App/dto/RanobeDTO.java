package App.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class RanobeDTO {
    public RanobeDTO() { volumes = new ArrayList<Volumes>(); }

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

    @Getter
    @Setter
    @JsonProperty("translator")
    private String translator;

    @Getter
    @Setter
    @JsonProperty("translation_status")
    private String translation_status;

    @Getter
    @Setter
    @JsonProperty("translation_method")
    private String translation_method;

    @Getter
    @Setter
    @JsonProperty("translate_size")
    private String translate_size;

    @Getter
    @Setter
    @JsonProperty("translate_size_avg")
    private String translate_size_avg;

    @Getter
    @Setter
    @JsonProperty("translation_rating")
    private String translation_rating;

    @Getter
    @Setter
    @JsonProperty("ranobe_rating")
    private String ranobe_rating;

    @Getter
    @Setter
    @JsonProperty("genres")
    private String genres;

    @Getter
    @Setter
    @JsonProperty("tags")
    private String tags;

    public static class Volumes
    {
        public static class Chapter
        {
            @Getter
            @Setter
            @JsonProperty("chapter_name")
            public String chapter_name;

            @Getter
            @Setter
            @JsonProperty("chapter_url")
            public String chapter_url;

            public Chapter() {}

            public Chapter(String chapter_name, String chapter_url)
            {
                this.chapter_name = chapter_name;
                this.chapter_url = chapter_url;
            }
        }

        @Getter
        @Setter
        @JsonProperty("volume_name")
        String name;

        @Getter
        @Setter
        @JsonProperty("chapters_list")
        public List<Chapter> chapters;

        public void AddChapter(Chapter chapter) { chapters.add(chapter); }

        public Volumes() { chapters = new ArrayList<Chapter>(); }
        public Volumes(String name) { chapters = new ArrayList<Chapter>(); this.name = name; }

    }

    public void AddVolume(Volumes volume) { volumes.add(volume); }

    @Setter
    @Getter
    @JsonProperty("volumes")
    private List<Volumes> volumes;
}
