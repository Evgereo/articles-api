package world.evgereo.articles.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class UriPageBuilder {
    private final String startUri;
    private Page<?> page;

    public UriPageBuilder(String startUri) {
        this.startUri = startUri;
    }

    public UriPageBuilder(String startUri, Page<?> page) {
        this.startUri = startUri;
        this.page = page;
    }

    public MultiValueMap<String, String> getAllPagesUri() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(1);
        StringBuilder stringBuilder = new StringBuilder();
        if (page.hasPrevious()) {
            stringBuilder.append(getFirstPageUri());
            if (page.getPageable().getPageNumber() > 1) {
                stringBuilder.append(", ");
                stringBuilder.append(getPreviousPageUri());
            }
        }
        if (page.hasNext()) {
            if (!stringBuilder.isEmpty()) stringBuilder.append(", ");
            stringBuilder.append(getLastPageUri());
            if (page.getPageable().getPageNumber() < page.getTotalPages() - 1) {
                stringBuilder.append(", ");
                stringBuilder.append(getNextPageUri());
            }
        }
        map.add("Link", stringBuilder.toString());
        return map;
    }

    private String getFirstPageUri() {
        assert page != null;
        return "<" + buildPageUri(0, page.getSize()) + ">; rel=\"first\"";
    }

    private String getPreviousPageUri() {
        assert page != null;
        return "<" + buildPageUri(page.getPageable().getPageNumber() - 1, page.getSize()) + ">; rel=\"prev\"";
    }

    private String getNextPageUri() {
        assert page != null;
        return "<" + buildPageUri(page.getPageable().getPageNumber() + 1, page.getSize()) + ">; rel=\"next\"";
    }

    private String getLastPageUri() {
        assert page != null;
        return "<" + buildPageUri(page.getTotalPages(), page.getSize()) + ">; rel=\"last\"";
    }

    private String buildPageUri(int page, int size) {
        return UriComponentsBuilder.newInstance()
                .scheme(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getScheme())
                .host(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getHost())
                .port(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPort())
                .path(startUri)
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUriString();
    }
}
