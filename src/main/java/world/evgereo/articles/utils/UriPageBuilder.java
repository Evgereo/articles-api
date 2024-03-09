package world.evgereo.articles.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UriPageBuilder {
    private Page<?> page;

    public static String buildPageUri(int page, int size) {
        return UriComponentsBuilder.newInstance()
                .scheme(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getScheme())
                .host(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getHost())
                .port(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPort())
                .path(Objects.requireNonNull(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath()))
                .queryParam("page", page)
                .queryParam("size", size)
                .build().toUriString();
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
            if (page.getPageable().getPageNumber() < page.getTotalPages() - 2) {
                stringBuilder.append(", ");
                stringBuilder.append(getNextPageUri());
            }
        }
        if (!stringBuilder.isEmpty()) map.add("Link", stringBuilder.toString());
        return map;
    }

    public String getFirstPageUri() {
        assert page != null;
        return "<" + UriPageBuilder.buildPageUri(0, page.getSize()) + ">; rel=\"first\"";
    }

    public String getPreviousPageUri() {
        assert page != null;
        return "<" + UriPageBuilder.buildPageUri(page.getPageable().getPageNumber() - 1, page.getSize()) + ">; rel=\"prev\"";
    }

    public String getNextPageUri() {
        assert page != null;
        return "<" + UriPageBuilder.buildPageUri(page.getPageable().getPageNumber() + 1, page.getSize()) + ">; rel=\"next\"";
    }

    public String getLastPageUri() {
        assert page != null;
        return "<" + UriPageBuilder.buildPageUri(page.getTotalPages(), page.getSize()) + ">; rel=\"last\"";
    }
}
