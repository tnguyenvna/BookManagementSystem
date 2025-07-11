package com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagianatedBookResponse {
    private List<BookResponse> contents;
    private int pageElementCount;
    private int pageSize;
}
