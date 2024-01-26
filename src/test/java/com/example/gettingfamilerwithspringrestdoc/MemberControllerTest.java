package com.example.gettingfamilerwithspringrestdoc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(documentationConfiguration(restDocumentation))
			.build();
		this.objectMapper = new ObjectMapper();
	}

	@Test
	void getMember() throws Exception {
		String memberId = "1";

		mockMvc.perform(MockMvcRequestBuilders.get("/members/{id}", memberId).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value("juwoong"))
			.andExpect(jsonPath("age").value(30))
			.andDo(MockMvcResultHandlers.print())
			.andDo(document("getMember", responseFields(fieldWithPath("name").description("member name"),
				fieldWithPath("age").description("member age"))));

	}

	@Test
	void getMembers() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/members").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].name").value("juwoong10"))
			.andExpect(jsonPath("[0].age").value(10))
			.andExpect(jsonPath("[1].name").value("juwoong20"))
			.andExpect(jsonPath("[1].age").value(20))
			.andDo(document("getMembers",
				responseFields(fieldWithPath("[].name").description("Member name"),
					fieldWithPath("[].age").description("Member age"))));

	}

	@Test
	void createMember() throws Exception {
		String content = objectMapper.writeValueAsString(new Member("juwoong", 30));

		mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON)
				.content(content)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("name").value("juwoong"))
			.andExpect(jsonPath("age").value(30))
			.andDo(MockMvcResultHandlers.print())
			.andDo(document("createMember", responseFields(fieldWithPath("name").description("member name"),
				fieldWithPath("age").description("member age"))));

	}

	@Test
	void putUpdateMember() throws Exception {
		String memberId = "1";
		Member updatedMember = new Member("updatedName", 25);
		String content = objectMapper.writeValueAsString(updatedMember);

		mockMvc.perform(put("/members/{id}", memberId).contentType(MediaType.APPLICATION_JSON)
				.content(content)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value("updatedName"))
			.andExpect(jsonPath("age").value(25))
			.andDo(MockMvcResultHandlers.print())
			.andDo(document("putUpdateMember", pathParameters(parameterWithName("id").description("memberId")),
				responseFields(fieldWithPath("name").description("member name"),
					fieldWithPath("age").description("member age"))));
	}

	@Test
	void patchUpdateMember() throws Exception {
		String memberId = "1";
		String updatedName = "updatedName";

		mockMvc.perform(
				patch("/members/{id}", memberId).param("memberName", updatedName).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(updatedName))
			.andExpect(jsonPath("age").value(10))  // Assuming age is not updated in this case
			.andDo(MockMvcResultHandlers.print())
			.andDo(document("patchUpdateMember", pathParameters(parameterWithName("id").description("memberId")),
				responseFields(fieldWithPath("name").description("member name"),
					fieldWithPath("age").description("member age"))));
	}

	@Test
	void deleteMember() throws Exception {
		String memberId = "1";

		mockMvc.perform(delete("/members/{id}", memberId))
			.andExpect(status().isNoContent())
			.andDo(MockMvcResultHandlers.print())
			.andDo(document("deleteMember", pathParameters(parameterWithName("id").description("memberId"))));
	}

}
