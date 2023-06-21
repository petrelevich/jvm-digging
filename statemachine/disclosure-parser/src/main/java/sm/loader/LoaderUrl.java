package sm.loader;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.model.Content;


public class LoaderUrl implements Loader, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(LoaderUrl.class);
    private final WebClient webClient;
    private final String url;

    public LoaderUrl(String url) {
        this.url = url;
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    @Override
    public Content load() {
        try {
            log.info("doLoad");
            WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);

            Page page = webClient.getPage(requestSettings);
            WebResponse response = page.getWebResponse();
            String content = response.getContentAsString();
            log.info("textContent:{}", content);

            return new Content(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        log.info("close LoaderUrl");
        webClient.close();
    }
}
