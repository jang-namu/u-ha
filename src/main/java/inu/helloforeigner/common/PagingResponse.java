package inu.helloforeigner.common;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class PagingResponse<T> {
    private List<T> content;
    private PageableResponse pageable;

    private PagingResponse(List<T> content, PageableResponse pageable) {
        this.content = content;
        this.pageable = pageable;
    }

    public static <T> PagingResponse<T> of(List<T> content, Pageable pageable, long totalElements) {
        return new PagingResponse<>(content, PageableResponse.from(pageable, totalElements));
    }

    @Getter
    public static class PageableResponse {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;

        private PageableResponse(Pageable pageable, long totalElements) {
            this.page = pageable.getPageNumber();
            this.size = pageable.getPageSize();
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
        }

        public static PageableResponse from(Pageable pageable, long totalElements) {
            return new PageableResponse(pageable, totalElements);
        }
    }
}
