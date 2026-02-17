package com.example.bot.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class PrayerTimeService {

    @SneakyThrows
    public String getPrayerTimes(String city) {
        log.info("Namoz vaqtlari so'ralmoqda. Shahar: {}", city);

        String cityForUrl = city.contains("(") ?
                city.substring(city.indexOf("(") + 1, city.indexOf(")")).toLowerCase() :
                city.toLowerCase();

        String url = "https://namozvaqti.uz/shahar/" + cityForUrl;

        try (WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            HtmlPage page = webClient.getPage(url);

            List<HtmlElement> allP = page.getByXPath("//p");
            List<String> timeList = allP.stream()
                    .map(HtmlElement::asNormalizedText)
                    .filter(t -> t.matches("\\d{2}:\\d{2}"))
                    .toList();

            if (timeList.size() < 6) {
                return "⚠️ Ma'lumot topilmadi.";
            }

            // Bugungi sana
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            // Siz tanlagan stikerlar bilan formatlash
            return String.format(
                    "\uD83C\uDF19%s shahri uchun bugungi namoz vaqtlari:\n" +
                    "📅 Sana: %s\n\n" +
                    "\uD83D\uDD53 Bomdod : %s\n" +
                    "⛅ Quyosh : %s\n" +
                    "️\uD83C\uDF1E Peshin : %s\n" +
                    "\uD83D\uDD52 Asr : %s\n" +
                    "\uD83D\uDD54 Shom : %s\n" +
                    "\uD83D\uDD57 Xufton : %s",
                    city.toUpperCase(),
                    today,
                    timeList.get(0), timeList.get(1), timeList.get(2),
                    timeList.get(3), timeList.get(4), timeList.get(5)
            );
        } catch (Exception e) {
            log.error("Xatolik: ", e);
            return "⚠️ Tizim xatosi: " + e.getMessage();
        }
    }
}