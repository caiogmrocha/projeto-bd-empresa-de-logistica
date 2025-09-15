package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import org.springframework.data.domain.Page;
import java.util.List;

public class PageResponseDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;

    public PageResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
    }

    public List<T> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
}
