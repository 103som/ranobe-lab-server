package App.contollers;

import App.config.Config;
import App.dto.ChapterDTO;
import App.dto.RanobeDTO;
import App.dto.SearchedRanobeDTO;
import App.parsing.RanobeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WebController {
    public static final int MAX_RANOBES_FROM_WEBSITE = 20;

    @Autowired
    private Config config;
    private final RestTemplate restTemplate;

    @Autowired
    public WebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String ParseWebsite(String website_transported, String type)
    {
        Map<String, String> websites = new HashMap<>();
        switch (type)
        {
            case "book":
                websites.put("Rulate", config.getServers().get(0).ranobe_page_url);
                websites.put("RanobeHub", config.getServers().get(1).ranobe_page_url);
                websites.put("RanobeLib", config.getServers().get(2).ranobe_page_url);
                break;
            case "website":
                websites.put("Rulate", config.getServers().get(0).main_page_url);
                websites.put("RanobeHub", config.getServers().get(1).main_page_url);
                websites.put("RanobeLib", config.getServers().get(2).main_page_url);
                break;
            case "find":
                websites.put("Rulate", config.getServers().get(0).find_ranobe_page_url);
                websites.put("RanobeHub", config.getServers().get(1).find_ranobe_page_url);
                websites.put("RanobeLib", config.getServers().get(2).find_ranobe_page_url);
                break;
            default:
                return null;
        }

        return websites.get(website_transported);
    }

    @GetMapping("/get-ranobe-list")
    public String getExternalData() {
        ResponseEntity<String> response = restTemplate.getForEntity(config.getServers().getFirst().main_page_url, String.class);
        return response.getBody();
    }

    @GetMapping("/search-ranobe")
    public List<SearchedRanobeDTO> getRanobe(@RequestParam(name = "query") String name) {
        String encoded_name = URLEncoder.encode(name, StandardCharsets.UTF_8);

        String query = config.getServers().getFirst().find_ranobe_page_url + encoded_name;
        String html = restTemplate.getForObject(query, String.class);

        List<SearchedRanobeDTO> ranobes = RanobeParser.parseRanobesFromSearchList(html, MAX_RANOBES_FROM_WEBSITE);
        if (ranobes == null)
            return (new ArrayList<SearchedRanobeDTO>());

        for (SearchedRanobeDTO ranobe : ranobes)
            ranobe.setWebsite(config.getServers().getFirst().main_page_url);

        return (ranobes);
    }

    @GetMapping("/get-ranobe/{id}-{website}")
    public RanobeDTO getRanobe(@PathVariable("id") String id, @PathVariable("website") String website) {
        String url = ParseWebsite(website, "book");
        if (url == null)
            return (new RanobeDTO()); // Incorrect website.

        url += id;
        String html = restTemplate.getForObject(url, String.class);
        return RanobeParser.parseRanobe(html);
    }

    @GetMapping("/get-chapter/{book_id}-{chapter_id}-{website}")
    public ChapterDTO getChapter(@PathVariable("book_id") String book_id, @PathVariable("chapter_id") String chapter_id, @PathVariable("website") String website)
    {
        String website_url = ParseWebsite(website, "book");
        if (website_url == null)
            return (new ChapterDTO()); // Incorrect website.

        String url = (website_url + book_id + '/' + chapter_id + "/ready");

        String html = restTemplate.getForObject(url, String.class);
        ChapterDTO chapterDTO = RanobeParser.ParseChapter(html);
        String website_main_page_url = ParseWebsite(website, "website");
        if (website_main_page_url == null)
            return new ChapterDTO();

            if (chapterDTO.chapter_as_fb2_url != null)
            chapterDTO.chapter_as_fb2_url = website_main_page_url + chapterDTO.chapter_as_fb2_url;

        if (chapterDTO.chapter_as_pdf_url != null)
            chapterDTO.chapter_as_pdf_url = website_main_page_url + chapterDTO.chapter_as_pdf_url;

        if (chapterDTO.chapter_as_docx_url != null)
            chapterDTO.chapter_as_docx_url = website_main_page_url + chapterDTO.chapter_as_docx_url;

        return chapterDTO;
    }
}
