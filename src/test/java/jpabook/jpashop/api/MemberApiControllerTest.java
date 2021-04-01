package jpabook.jpashop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest  {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @BeforeEach
//    void setUp(@Autowired MemberApiController memberApiController){
//        mockMvc = MockMvcBuilders.standaloneSetup(memberApiController).build();
//    }

    @Test
    @DisplayName("회원_등록API_V1")
    void 회원_등록ApiV1() throws Exception {
        //given
        MemberDto memberDto = new MemberDto("회원1");

        final String jsonStr = objectMapper.writeValueAsString(memberDto);

        //when
        final ResultActions resultActions = mockMvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStr)
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;

    }

    @Test
    @DisplayName("회원_등록API_V2")
    void 회원_등록ApiV2() throws Exception {
        //given
        MemberDto memberDto = new MemberDto("회원2");

        final String jsonStr = objectMapper.writeValueAsString(memberDto);

        //when
        final ResultActions resultActions = mockMvc.perform(post("/api/v2/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStr)
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }


    @Test
    @DisplayName("회원정보_업데이트V2")
    void 회원정보_업데이트V2() throws Exception {
        //given
        MemberDto memberDto = new MemberDto("회원3");

        final String jsonStr = objectMapper.writeValueAsString(memberDto);
        //when
        final ResultActions resultActions = mockMvc.perform(post("/api/v2/members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStr)
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("회원3"));
    }

}