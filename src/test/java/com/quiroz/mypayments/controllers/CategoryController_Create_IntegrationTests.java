package com.quiroz.mypayments.controllers;

import static com.quiroz.mypayments.constants.PropertiesConstants.JSON_ROOT;
import static com.quiroz.mypayments.constants.PropertiesConstants.URL_API;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.quiroz.mypayments.MyPaymentsApplication;
import com.quiroz.mypayments.Utils.Utils;
import com.quiroz.mypayments.dto.requests.AddCategoryRequestDto;
import com.quiroz.mypayments.entities.Category;
import com.quiroz.mypayments.factories.CategoryFactory;
import com.quiroz.mypayments.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = MyPaymentsApplication.class)
@TestPropertySource(locations = "classpath:application-unittest.properties")
@AutoConfigureMockMvc
public class CategoryController_Create_IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryFactory categoryFactory;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    void testCreateCategory_ReturnHttpStatusCode201() throws Exception {

        AddCategoryRequestDto input = categoryFactory.createAddCategoryRequestDtoMock();

        String requestJson = Utils.convertObjectToJsonString(input);
        mockMvc.perform(post(URL_API.concat("/category"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath(JSON_ROOT + ".id", CoreMatchers.is(notNullValue())))
            .andExpect(jsonPath(JSON_ROOT + ".code", CoreMatchers.is(input.getCode())))
            .andExpect(jsonPath(JSON_ROOT + ".code", CoreMatchers.is(notNullValue())))
            .andExpect(jsonPath(JSON_ROOT + ".name", CoreMatchers.is(notNullValue())))
            .andExpect(jsonPath(JSON_ROOT + ".description", CoreMatchers.is(notNullValue())));
    }

    @Test
    @Transactional
    void testCreateCategory_Throws_BadRequest_GivenNameAndCodeExists() throws Exception {

        when(categoryRepository.findByNameAndCodeIgnoreCase(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Optional.of(mock(Category.class)));

        AddCategoryRequestDto input = categoryFactory.createAddCategoryRequestDtoMock();

        var expectedMessageError = String.format("Already exists a category with name: %s and code: %s",
            input.getName(), input.getCode());

        String requestJson = Utils.convertObjectToJsonString(input);
        mockMvc.perform(post(URL_API.concat("/category"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(JSON_ROOT + ".status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath(JSON_ROOT + ".timestamp", is(notNullValue())))
            .andExpect(jsonPath(JSON_ROOT + ".message", is(expectedMessageError)));
    }
}
