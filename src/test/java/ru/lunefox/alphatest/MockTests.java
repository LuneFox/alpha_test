package ru.lunefox.alphatest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.lunefox.alphatest.model.gifs.Gif;
import ru.lunefox.alphatest.model.gifs.GifService;
import ru.lunefox.alphatest.model.rates.ExchangeRate;
import ru.lunefox.alphatest.model.rates.ExchangeRateService;

import java.util.Map;

public class MockTests {
    private static final WireMockServer server = new WireMockServer(9998);

    @BeforeAll
    public static void initServer() {
        server.start();
        WireMock.configureFor("localhost", 9998);
        prepareExchangeRateServiceMockResponse();
        prepareGifServiceMockResponse();
    }

    private static void prepareExchangeRateServiceMockResponse() {
        ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
        mockResponse.withStatus(200);
        mockResponse.withStatusMessage("OK");
        mockResponse.withHeader("Connection", "keep-alive");
        mockResponse.withBody("{\n" +
                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                "    \"timestamp\": 1654632000,\n" +
                "    \"base\": \"USD\",\n" +
                "    \"rates\": {\n" +
                "        \"JPY\": 50.00,\n" +
                "        \"RUB\": 30.00\n" +
                "    }\n" +
                "}");

        WireMock.stubFor(WireMock
                .get("/latest.json?app_id=test_app_id&base=USD")
                .willReturn(mockResponse));
    }

    private static void prepareGifServiceMockResponse() {
        ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
        mockResponse.withStatus(200);
        mockResponse.withStatusMessage("OK");
        mockResponse.withHeader("Connection", "keep-alive");
        mockResponse.withBody("{\n" +
                "    \"data\": {\n" +
                "        \"type\": \"gif\",\n" +
                "        \"id\": \"mock_id\",\n" +
                "        \"url\": \"https://giphy.com/mock_url/\",\n" +
                "        \"embed_url\": \"https://giphy.com/embed/mock_url/\"\n" +
                "    }\n" +
                "}");

        WireMock.stubFor(WireMock
                .get("/?api_key=test_key&tag=rich&rating=g")
                .willReturn(mockResponse));
    }

    @Test
    public void exchangeRateServiceMockTest() {
        ExchangeRateService service = new ExchangeRateService();
        service.setBase("USD");
        service.setServer("http://localhost:9998/");
        service.setAppId("test_app_id");

        ExchangeRate exchangeRate = service.getExchangeRate("latest.json");

        Assertions.assertEquals(exchangeRate.getBase(), "USD");
        Assertions.assertNotEquals(exchangeRate.getTimestamp(), 0);
        Assertions.assertNotNull(exchangeRate.getRates());
        Assertions.assertTrue(exchangeRate.getRates().containsKey("RUB"));
    }

    @Test
    public void gifServiceMockTest() {
        GifService gifService = new GifService();
        gifService.setServer("http://localhost:9998");
        gifService.setApiKey("test_key");
        gifService.setRichTag("rich");
        gifService.setRating("g");

        Gif gif = gifService.getGif(Gif.Tag.RICH);
        Map<String, Object> data = gif.getData();
        String embedUrl = (String) data.get("embed_url");

        Assertions.assertTrue(embedUrl.contains("https://giphy.com/embed"));
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }
}
