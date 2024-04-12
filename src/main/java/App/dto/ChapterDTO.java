package App.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class ChapterDTO {
    @Setter
    @Getter
    @JsonProperty("chapter_as_pdf_url")
    public String chapter_as_pdf_url;

    @Setter
    @Getter
    @JsonProperty("chapter_as_fb2_url")
    public String chapter_as_fb2_url;

    @Setter
    @Getter
    @JsonProperty("chapter_as_docx_url")
    public String chapter_as_docx_url;
}
