package com.getir.rig.controller;

import com.getir.rig.controller.dto.BookDto;
import com.getir.rig.model.Book;
import com.getir.rig.service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

@SpringBootTest
class BookControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private BookService bookService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "admin", authorities = "CUSTOMER")
    void testGetBook_whenPageablePassed_thenCreatesPageSuccessfully() throws Exception {
        // given
        final Page<BookDto> returnedPage = new PageImpl<>(new ArrayList<>());
        final Page<Book> spyPage = Mockito.spy(new PageImpl<>(new ArrayList<>()));

        Mockito.doReturn(returnedPage).when(spyPage).map(ArgumentMatchers.any());
        Mockito.doReturn(spyPage).when(bookService).findAll(ArgumentMatchers.any());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/book")
                        .param("page", "5")
                        .param("size", "10")
                        .param("sort", "id,desc")
                        .param("sort", "name,asc"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(bookService).findAll(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(5);
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(10);
        Assertions.assertThat(pageable.getSort().toString()).hasToString("id: DESC,name: ASC");
    }
}
