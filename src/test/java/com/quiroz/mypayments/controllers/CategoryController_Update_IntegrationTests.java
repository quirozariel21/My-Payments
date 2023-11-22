package com.quiroz.mypayments.controllers;

import static com.quiroz.mypayments.constants.PropertiesConstants.JSON_ROOT;
import static com.quiroz.mypayments.constants.PropertiesConstants.URL_API;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.quiroz.mypayments.MyPaymentsApplication;
import com.quiroz.mypayments.Utils.Utils;
import com.quiroz.mypayments.dto.requests.UpdateCategoryRequestDto;
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
public class CategoryController_Update_IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryFactory categoryFactory;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    void testUpdateCategory_ReturnHttpStatusCode200() throws Exception {

        UpdateCategoryRequestDto input = categoryFactory.createUpdateCategoryRequestDtoMock();
        Category category = Category.builder()
            .id(input.getId())
            .build();
        when(categoryRepository.findById(Mockito.eq(input.getId())))
            .thenReturn(Optional.of(category));

        String requestJson = Utils.convertObjectToJsonString(input);
        mockMvc.perform(patch(URL_API.concat("/category"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath(JSON_ROOT + ".id", CoreMatchers.is(notNullValue())))
            .andExpect(jsonPath(JSON_ROOT + ".code", CoreMatchers.is(input.getCode())))
            .andExpect(jsonPath(JSON_ROOT + ".code", CoreMatchers.is(notNullValue())))
            .andExpect(jsonPath(JSON_ROOT + ".name", CoreMatchers.is(notNullValue())))
            .andExpect(jsonPath(JSON_ROOT + ".description", CoreMatchers.is(notNullValue())));
    }

    @Test
    @Transactional
    void testCreateCategory_Throws_BadRequest_GivenNameAndCodeExists() throws Exception {
        UpdateCategoryRequestDto input = categoryFactory.createUpdateCategoryRequestDtoMock();

        when(categoryRepository.findById(Mockito.eq(input.getId())))
            .thenReturn(Optional.empty());

        var expectedMessageError = String.format("Category with id: %s not found.",
            input.getId());

        String requestJson = Utils.convertObjectToJsonString(input);
        mockMvc.perform(patch(URL_API.concat("/category"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath(JSON_ROOT + ".status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath(JSON_ROOT + ".timestamp", is(notNullValue())))
            .andExpect(jsonPath(JSON_ROOT + ".message", is(expectedMessageError)));
    }
}
