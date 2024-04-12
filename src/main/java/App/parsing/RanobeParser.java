package App.parsing;

import App.dto.ChapterDTO;
import App.dto.RanobeDTO;
import App.dto.SearchedRanobeDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.interfaces.EdECKey;
import java.util.ArrayList;
import java.util.List;

public class RanobeParser {
    public static SearchedRanobeDTO.MetaInfo parseMetaInfo(Element book_meta)
    {
        SearchedRanobeDTO.MetaInfo metaInfo = new SearchedRanobeDTO.MetaInfo();
        for (Element element : book_meta.children())
        {
            if (element.attr("title").contains("Кол-во глав"))
                metaInfo.setChapters_count(element.text());
            else if (element.attr("title").contains("Кол-во страниц"))
                metaInfo.setPages_count(element.text());
            else if (element.attr("title").contains("Рейтинг"))
                metaInfo.setRating(element.text());
            else if (element.attr("title").contains("Качество перевода"))
                metaInfo.setTranslation_rating(element.text());
            else if (element.attr("href").contains("users"))
                metaInfo.setTranslator(element.attr("href"));
            else if (element.text().contains("жанры:"))
                metaInfo.setGenres(element.text());
            else if (element.text().contains("тэги:"))
                metaInfo.setTags(element.text());
            else if (element.text().contains("перевода:"))
                metaInfo.setTranslation_status(element.children().getFirst().children().text());
        }

        return metaInfo;
    }

    public static SearchedRanobeDTO.BookInfo parseBookInfo(Document doc, int book_id)
    {
        SearchedRanobeDTO.BookInfo bookInfo = new SearchedRanobeDTO.BookInfo();
        bookInfo.setId(doc.select("p.book-tooltip > a").get(book_id).attr("href")); // Book ID.
        bookInfo.setTitle(doc.select("p.book-tooltip > a").get(book_id).text()); // Book name.
        bookInfo.setDescription(doc.select(".tooltip_templates > div").get(book_id).text()); // Book description.
        bookInfo.setLogo(doc.select(".th > img").get(book_id).attr("src")); // Book logo.

        return bookInfo;
    }

    public static List<SearchedRanobeDTO> parseRanobesFromSearchList(String html, int ranobes_count)
    {
        List<SearchedRanobeDTO> ranobes = new ArrayList<SearchedRanobeDTO>();

        Document doc = Jsoup.parse(html);
        if (doc.getElementsByClass("search-results").isEmpty())
        {
            return null; // Если в результате поиска ничего не найдено.
        }

        for (int i = 1; i < Math.min(doc.getElementsByClass("search-results").first().children().size(),ranobes_count); ++i)
        {
            ranobes.add(parseRanobe(doc, i));
        }

        return ranobes;
    }

    private static SearchedRanobeDTO parseRanobe(Document doc, int book_ind)
    {
        SearchedRanobeDTO ranobe = new SearchedRanobeDTO();

        ranobe.setBook_info(parseBookInfo(doc, book_ind));
        ranobe.setMeta_info(parseMetaInfo(doc.select(".meta").get(book_ind)));

        return ranobe;
    }

    public static RanobeDTO parseRanobe(String html)
    {
        Document doc = Jsoup.parse(html);
        RanobeDTO ranobeDTO = new RanobeDTO();

        // main_info.
        String description = doc.select("meta").get(8).attr("content");
        int index = description.indexOf("...");
        description = (index != -1) ? description.substring(0, index) : description;

        ranobeDTO.setDescription(description);
        ranobeDTO.setTitle(doc.select("h1").text());
        ranobeDTO.setLogo(doc.select("meta[property=og:image]").attr("content"));

        ranobeDTO.setTranslator(doc.select("p em a").first().text());
        ranobeDTO.setTranslation_status(doc.select("dl.info").get(1).children().get(1).text());
        ranobeDTO.setTranslation_method(doc.select("dl.info").get(1).children().get(3).text());
        ranobeDTO.setTranslate_size_avg(doc.select("dl.info").get(1).children().get(13).text());
        ranobeDTO.setTranslate_size(doc.select("dl.info").get(1).children().get(15).text());

        ranobeDTO.setRanobe_rating(doc.getElementsByClass("rating-block").get(0).text());
        ranobeDTO.setTranslation_rating(doc.getElementsByClass("rating-block").get(1).text());

        String genres = "";
        for (Element genre : doc.select("em a[href*=genres]"))
            genres += (genre.text() + ',');

        if (!genres.isEmpty())
            genres = genres.substring(0, genres.length() - 1);

        ranobeDTO.setGenres(genres);

        String tags = "";
        for (Element tag : doc.select("em a[href*=tags]"))
            tags += (tag.text() + ',');

        if (!tags.isEmpty())
            tags = tags.substring(0, tags.length() - 1);

        ranobeDTO.setTags(tags);

        if (doc.getElementsByClass("volume_helper").isEmpty())
        {
            Elements all_chapters = doc.getElementsByClass("chapter_row");
            if (all_chapters.isEmpty())
                return new RanobeDTO();

            RanobeDTO.Volumes volume = new RanobeDTO.Volumes("Название");
            for (Element chapter : all_chapters)
                volume.AddChapter(new RanobeDTO.Volumes.Chapter(chapter.children().select("td.t > a").text(),
                        chapter.children().select("td.t > a").attr("href")));

            ranobeDTO.AddVolume(volume);
            return ranobeDTO;
        }

        Elements all_volumes = doc.getElementsByClass("volume_helper");
        for (int i = 0; i < all_volumes.size(); ++i)
        {
            Element cur_volume = all_volumes.get(i);
            if (cur_volume.text().isEmpty())
                continue;

            Element nextVolume = (i + 1 < all_volumes.size()) ? all_volumes.get(i + 1) : null;
            Element nextRow = cur_volume.nextElementSibling(); // Переход к следующему элементу после текущего тома

            RanobeDTO.Volumes volume = new RanobeDTO.Volumes(cur_volume.text());
            while (nextRow != null && (nextVolume == null || !nextRow.equals(nextVolume))) {
                if (nextRow.hasClass("chapter_row")) {
                    volume.AddChapter(new RanobeDTO.Volumes.Chapter(nextRow.select("td.t > a").text(),
                                      nextRow.select("td.t > a").attr("href")));
                }
                nextRow = nextRow.nextElementSibling(); // Переходим к следующему элементу
            }

            ranobeDTO.AddVolume(volume);
        }

        return ranobeDTO;
    }

    public static ChapterDTO ParseChapter(String html)
    {
        return ParseLinks(Jsoup.parse(html).getElementsByClass("tools").select("a[href*=/download?format=]"));
    }

    private static ChapterDTO ParseLinks(Elements urls)
    {
        if (urls.isEmpty())
            return new ChapterDTO();

        ChapterDTO chapter = new ChapterDTO();
        for (Element url : urls)
        {
            if (url.text().contains(".fb2"))
                chapter.setChapter_as_fb2_url(url.attr("href"));
            else if (url.text().contains(".pdf"))
                chapter.setChapter_as_pdf_url(url.attr("href"));
            else if (url.text().contains(".docx"))
                chapter.setChapter_as_docx_url(url.attr("href"));
        }

        return chapter;
    }
}